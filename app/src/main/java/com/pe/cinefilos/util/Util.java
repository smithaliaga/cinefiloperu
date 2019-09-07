package com.pe.cinefilos.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pe.cinefilos.R;
import com.pe.cinefilos.dialog.DialogMessage;
import com.pe.cinefilos.object.entities.EntityWSBase;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Util {
    public static void show_toast(Context ctx, String message) {
        show_toast(ctx, message, Toast.LENGTH_SHORT);
    }

    public static void show_toast(Context ctx, String message, int p_duration) {
        Toast toast = new Toast(ctx);
        toast = Toast.makeText(ctx, message, p_duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String getLanguage() {
        String language = "";
        try{
            Context context = App.getContext();
            language = new Shared(context).getLanguage();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return language;
    }

    public static EntityWSBase getEntityWSBase(Message msg){
        Gson gson = new Gson();
        return gson.fromJson(String.valueOf(msg.obj),EntityWSBase.class);
    }

    public static Dialog get_progress_dialog(Context p_context) {
        try {
            Dialog dialog = new Dialog(p_context, R.style.DialogCustom);
            dialog.setCancelable(false);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.addContentView(new ProgressBar(p_context), new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));

            return dialog;
        } catch (Exception err) {
            return null;
        }
    }

    public static DialogMessage dialog_msg(Activity p_context, String mensaje){
        DialogMessage dm = new DialogMessage(p_context);
        dm.setMessage(mensaje);
        return dm;
    }

    public static void trustAllCertificates() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                            return myTrustedAnchors;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception e) {
        }
    }

    public static String convertDateStringToFormatDateString(String sFecha, String formatoInicial, String foramtoFinal){
        String retorno = "";
        try{
            SimpleDateFormat sdfInicial = new SimpleDateFormat(formatoInicial);
            SimpleDateFormat sdfFinal = new SimpleDateFormat(foramtoFinal);

            Date fecha = sdfInicial.parse(sFecha);
            retorno  = sdfFinal.format(fecha);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return retorno;
    }
}
