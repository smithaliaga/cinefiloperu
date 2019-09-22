package com.pe.cinefilos.service;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.pe.cinefilos.MainActivity;
import com.pe.cinefilos.R;
import com.pe.cinefilos.exception.AsyncConnectionException;
import com.pe.cinefilos.object.entities.EntityWSBase;
import com.pe.cinefilos.util.AESManager;
import com.pe.cinefilos.util.App;
import com.pe.cinefilos.util.Util;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

public class Connection {
    public static final int CONNECTION_OK = 0;
    public static final int CONNECTION_FAILED = 1;

    DefaultHttpClient httpClient;

    String response = null;

    String url;
    Map<String, Object> parameters;
    Handler handler;

    public Connection(String p_url, Handler p_handler, Map<String, Object> p_parameters) {
        url = p_url;
        parameters = p_parameters;
        handler = p_handler;

        HttpParams basic_http_params = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(basic_http_params, 60 * 1000);
        HttpConnectionParams.setSoTimeout(basic_http_params, 60 * 1000);

        httpClient = new DefaultHttpClient(basic_http_params);
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);
    }

    public void startAsync() {
        new Thread(new SendRequestRunnable(HTTP_METHOD.POST)).start();
    }

    public Message startSync() {
        return new SendRequestRunnable(HTTP_METHOD.POST).runSync();
    }

    public enum HTTP_METHOD {
        GET,
        POST,
        BITMAP
    }

    public void startAsync(HTTP_METHOD p_method) {
        new Thread(new SendRequestRunnable(p_method)).start();
    }

    public String get_response() {
        return response;
    }

    class SendRequestRunnable implements Runnable {
        HTTP_METHOD http_method;

        public SendRequestRunnable(HTTP_METHOD p_http_method) {
            http_method = p_http_method;
        }

        public Message runSync() {
            Message message = new Message();
            HttpUriRequest UriRequest = null;

            try {
                switch (http_method) {
                    case POST:
                        UriRequest = new HttpPost(url);
                        UriRequest.setHeader("Content-Type", "application/json");

                        String json_parameters = new Gson().toJson(parameters);
                        ((HttpPost) UriRequest).setEntity(new StringEntity(json_parameters == null ? "" : json_parameters, "UTF-8"));
                        break;

                    case GET:
                        String uri = url + mapToQueryString(parameters);
                        UriRequest = new HttpGet(uri);
                        break;

                    case BITMAP:
                        InputStream in = new java.net.URL(url).openStream();
                        message.obj = BitmapFactory.decodeStream(in);
                        message.arg1 = CONNECTION_OK;
                        return message;
                }

                HttpResponse http_response = httpClient.execute(UriRequest, new BasicHttpContext());

                if (http_response != null) {
                    message.obj = EntityUtils.toString(http_response.getEntity());
                    message.arg1 = CONNECTION_OK;
                } else {
                    message.arg1 = CONNECTION_FAILED;
                }
            } catch (Exception err) {
                message.arg1 = CONNECTION_FAILED;
            }

            return message;
        }

        @Override
        public void run() {
            Message message = runSync();
            handler.sendMessage(message);
        }
    }

    public String mapToQueryString(Map<String, Object> map) {
        StringBuilder string = new StringBuilder();

        if (map != null) {
            if (map.size() > 0) {
                string.append("?");
            }

            for (Entry<String, Object> entry : map.entrySet()) {
                string.append(entry.getKey());
                string.append("=");
                string.append(entry.getValue());
                string.append("&");
            }
        }
        return string.toString();
    }

    public static <T> T process_handler(Message p_message, Context p_context, Class<T> p_return_class_type) {
        try {
            switch (p_message.arg1) {
                case Connection.CONNECTION_OK:
                    String json_response = AESManager.decrypt((String) p_message.obj);
                    System.out.println("json_response:" + json_response);
                    if (json_response == null || json_response.isEmpty()) {
                        throw new AsyncConnectionException(p_context.getResources().getString(R.string.connection_noconnection));
                    } else {
                        T response = new Gson().fromJson(json_response, p_return_class_type);
                        if (response instanceof EntityWSBase) {

                            switch (((EntityWSBase) response).errorCode) {
                                case 100: {
                                    Intent intent = new Intent(p_context, MainActivity.class);
                                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    p_context.startActivity(intent);
                                    throw new AsyncConnectionException(p_context.getResources().getString(R.string.session_expired));
                                }
                                default: {
                                    return response;
                                }
                            }
                        } /////////////
                    }

                case Connection.CONNECTION_FAILED: {
                    throw new AsyncConnectionException(p_context.getResources().getString(R.string.connection_noconnection)); //
                }
            }
        } catch (AsyncConnectionException ex) {
            ex.printStackTrace();
            Util.show_toast(p_context, ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            Util.show_toast(p_context, p_context.getResources().getString(R.string.connection_noconnection));
        }
        return null;
    }

    public static <T> T process_handler_without_toast_error(Message p_message, Context p_context, Class<T> p_return_class_type) {
        try {
            switch (p_message.arg1) {
                case Connection.CONNECTION_OK:

                    String json_response = AESManager.decrypt((String) p_message.obj);

                    if (json_response == null || json_response.isEmpty()) {
                        throw new AsyncConnectionException(p_context.getResources().getString(R.string.connection_noconnection));
                    }

                    //Type type = new TypeToken<T>(){}.getType();

                    T response = new Gson().fromJson(json_response, p_return_class_type);

                    if (response instanceof EntityWSBase) {
                        switch (((EntityWSBase) response).errorCode) {
                            case 0:
                                return response;

                            case 100: {
                                Intent intent = new Intent(p_context, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                p_context.startActivity(intent);
                                break;
                            }
                            default:

                                //throw new AsyncConnectionException( ( (EntityWSBase) response ).ErrorMessage );
                        }
                    }

                case Connection.CONNECTION_FAILED: {
                    throw new AsyncConnectionException(p_context.getResources().getString(R.string.connection_noconnection)); //
                }
            }
        } catch (AsyncConnectionException err) {

        } catch (Exception ex) {
            Util.show_toast(p_context, p_context.getResources().getString(R.string.connection_noconnection));
        }
        return null;
    }
}