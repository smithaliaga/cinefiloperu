package com.pe.cinefilos.service;

import android.content.Context;
import android.os.Handler;

import com.pe.cinefilos.R;
import com.pe.cinefilos.util.AESManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiService {

    private static ApiService apiService = null;
    private static Connection conn = null;

    public static ApiService GetInstance(){
        if (apiService == null)
            apiService = new ApiService();
        return apiService;
    }

    public void WS_UserAuthenticate(Context p_context, Handler p_handler, String usuario, String clave, String idioma, String infoDispositivo, String tokenFirebase) {
        String METHOD_NAME = p_context.getResources().getString(R.string.WS_UserAuthenticate);
        String URL = p_context.getResources().getString(R.string.WS_url);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("usuario", usuario);
        parameters.put("clave", clave);
        parameters.put("idioma", idioma);
        parameters.put("infoDispositivo", infoDispositivo);
        parameters.put("tokenFirebase", tokenFirebase);

        parameters = AESManager.encryptParams(parameters);

        conn = new Connection(URL + "/" + METHOD_NAME, p_handler, parameters);
        conn.startAsync();
    }

    public void WS_ProcessQR(Context p_context, Handler p_handler, String token, String idDiscount) {
        String METHOD_NAME = p_context.getResources().getString(R.string.WS_ProcessQR);
        String URL = p_context.getResources().getString(R.string.WS_url);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("Token", token);
        parameters.put("IdDiscount", idDiscount);

        parameters = AESManager.encryptParams(parameters);

        conn = new Connection(URL + "/" + METHOD_NAME, p_handler, parameters);
        conn.startAsync();
    }

    public void WS_GetListMovie(Context p_context, Handler p_handler, String token) {
        String METHOD_NAME = p_context.getResources().getString(R.string.WS_GetListMovie);
        String URL = p_context.getResources().getString(R.string.WS_url);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("token", token);

        parameters = AESManager.encryptParams(parameters);

        conn = new Connection(URL + "/" + METHOD_NAME, p_handler, parameters);
        conn.startAsync();
    }

    public void WS_GetListTrivia(Context p_context, Handler p_handler, String token) {
        String METHOD_NAME = p_context.getResources().getString(R.string.WS_GetListTrivia);
        String URL = p_context.getResources().getString(R.string.WS_url);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("token", token);

        parameters = AESManager.encryptParams(parameters);

        conn = new Connection(URL + "/" + METHOD_NAME, p_handler, parameters);
        conn.startAsync();
    }

    public void WS_SendException(Context p_context, Handler p_handler, String exception, String sessionId) {
        String METHOD_NAME = p_context.getResources().getString(R.string.WS_SendException);
        String URL = p_context.getResources().getString(R.string.WS_url);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("exception", exception);
        parameters.put("sessionId", sessionId);

        parameters = AESManager.encryptParams(parameters);

        conn = new Connection(URL + "/" + METHOD_NAME, p_handler, parameters);
        conn.startAsync();
    }

    public void WS_RegisterIntentTrivia(Context p_context, Handler p_handler, String token, long codigoTrivia, boolean estadoRespuesta) {
        String METHOD_NAME = p_context.getResources().getString(R.string.WS_RegisterIntentTrivia);
        String URL = p_context.getResources().getString(R.string.WS_url);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("token", token);
        parameters.put("codigoTrivia", codigoTrivia);
        parameters.put("estadoRespuesta", estadoRespuesta);

        parameters = AESManager.encryptParams(parameters);

        conn = new Connection(URL + "/" + METHOD_NAME, p_handler, parameters);
        conn.startAsync();
    }

    public void WS_GetTriviaUser(Context p_context, Handler p_handler, String token, long codigoTrivia) {
        String METHOD_NAME = p_context.getResources().getString(R.string.WS_GetTriviaUser);
        String URL = p_context.getResources().getString(R.string.WS_url);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("token", token);
        parameters.put("codigoTrivia", codigoTrivia);

        parameters = AESManager.encryptParams(parameters);

        conn = new Connection(URL + "/" + METHOD_NAME, p_handler, parameters);
        conn.startAsync();
    }

    public void WS_RegisterUser(Context p_context, Handler p_handler, String dni, String nombres, String apellidos, String fechaNacimiento, String genero, String email,
                                 String direccion, String usuario, String clave) {
        String METHOD_NAME = p_context.getResources().getString(R.string.WS_RegisterUser);
        String URL = p_context.getResources().getString(R.string.WS_url);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("dni", dni);
        parameters.put("nombres", nombres);
        parameters.put("apellidos", apellidos);
        parameters.put("fechaNacimiento", fechaNacimiento);
        parameters.put("genero", genero);
        parameters.put("email", email);
        parameters.put("direccion", direccion);
        parameters.put("usuario", usuario);
        parameters.put("clave", clave);

        parameters = AESManager.encryptParams(parameters);

        conn = new Connection(URL + "/" + METHOD_NAME, p_handler, parameters);
        conn.startAsync();
    }

    public void WS_UserGetInformation(Context p_context, Handler p_handler, String token) {
        String METHOD_NAME = p_context.getResources().getString(R.string.WS_UserGetInformation);
        String URL = p_context.getResources().getString(R.string.WS_url);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("token", token);

        parameters = AESManager.encryptParams(parameters);

        conn = new Connection(URL + "/" + METHOD_NAME, p_handler, parameters);
        conn.startAsync();
    }

    public void WS_UserUpdateInformation(Context p_context, Handler p_handler, String token, String email, String direccion, String clave) {
        String METHOD_NAME = p_context.getResources().getString(R.string.WS_UserUpdateInformation);
        String URL = p_context.getResources().getString(R.string.WS_url);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("token", token);
        parameters.put("email", email);
        parameters.put("direccion", direccion);
        parameters.put("clave", clave);

        parameters = AESManager.encryptParams(parameters);

        conn = new Connection(URL + "/" + METHOD_NAME, p_handler, parameters);
        conn.startAsync();
    }

    public void WS_RecoveryPassword(Context p_context, Handler p_handler, String usuario) {
        String METHOD_NAME = p_context.getResources().getString(R.string.WS_RecoveryPassword);
        String URL = p_context.getResources().getString(R.string.WS_url);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("usuario", usuario);

        parameters = AESManager.encryptParams(parameters);

        conn = new Connection(URL + "/" + METHOD_NAME, p_handler, parameters);
        conn.startAsync();
    }

    public void WS_GetListCine(Context p_context, Handler p_handler, String token) {
        String METHOD_NAME = p_context.getResources().getString(R.string.WS_GetListCine);
        String URL = p_context.getResources().getString(R.string.WS_url);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("token", token);

        parameters = AESManager.encryptParams(parameters);

        conn = new Connection(URL + "/" + METHOD_NAME, p_handler, parameters);
        conn.startAsync();
    }

    public void WS_GetListHorario(Context p_context, Handler p_handler, String token, long codigoCine, long codigoPelicula) {
        String METHOD_NAME = p_context.getResources().getString(R.string.WS_GetListHorario);
        String URL = p_context.getResources().getString(R.string.WS_url);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("token", token);
        parameters.put("codigoPelicula", codigoPelicula);
        parameters.put("codigoCine", codigoCine);

        parameters = AESManager.encryptParams(parameters);

        conn = new Connection(URL + "/" + METHOD_NAME, p_handler, parameters);
        conn.startAsync();
    }

    public void WS_GetListButaca(Context p_context, Handler p_handler, String token, long codigoSala) {
        String METHOD_NAME = p_context.getResources().getString(R.string.WS_GetListButaca);
        String URL = p_context.getResources().getString(R.string.WS_url);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("token", token);
        parameters.put("codigoSala", codigoSala);

        parameters = AESManager.encryptParams(parameters);

        conn = new Connection(URL + "/" + METHOD_NAME, p_handler, parameters);
        conn.startAsync();
    }

    public void WS_GetMontoPago(Context p_context, Handler p_handler, String token, long codigoSala, List<Long> butacas) {
        String METHOD_NAME = p_context.getResources().getString(R.string.WS_GetMontoPago);
        String URL = p_context.getResources().getString(R.string.WS_url);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("token", token);
        parameters.put("codigoSala", codigoSala);
        parameters.put("butacas", butacas);

        parameters = AESManager.encryptParams(parameters);

        conn = new Connection(URL + "/" + METHOD_NAME, p_handler, parameters);
        conn.startAsync();
    }

    public void WS_RealizarPago(Context p_context, Handler p_handler, String token, long codigoSala, List<Long> butacas, String numeroTarjeta, String cvv, String anioVencimiento, String mesVencimiento) {
        String METHOD_NAME = p_context.getResources().getString(R.string.WS_RealizarPago);
        String URL = p_context.getResources().getString(R.string.WS_url);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("token", token);
        parameters.put("codigoSala", codigoSala);
        parameters.put("numeroTarjeta", numeroTarjeta);
        parameters.put("cvv", cvv);
        parameters.put("anioVencimiento", anioVencimiento);
        parameters.put("mesVencimiento", mesVencimiento);

        parameters = AESManager.encryptParams(parameters);

        conn = new Connection(URL + "/" + METHOD_NAME, p_handler, parameters);
        conn.startAsync();
    }

}
