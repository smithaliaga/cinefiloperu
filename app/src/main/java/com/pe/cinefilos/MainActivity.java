package com.pe.cinefilos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pe.cinefilos.object.entities.EntityWSBase;
import com.pe.cinefilos.object.entities.UserAuthenticate;
import com.pe.cinefilos.service.ApiService;
import com.pe.cinefilos.service.Connection;
import com.pe.cinefilos.util.App;
import com.pe.cinefilos.util.Shared;
import com.pe.cinefilos.util.Util;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Dialog pd;
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pd = Util.get_progress_dialog(this);

        String currentUser = new Shared(getApplicationContext()).getUser();
        EditText username = (EditText) findViewById(R.id.username);
        username.setText(currentUser == null ? "":currentUser);
        if (username != null){
            EditText password = (EditText) findViewById(R.id.password);
            password.requestFocus();
        } else{
            username.requestFocus();
        }

        ImageView ivCall = (ImageView) findViewById(R.id.ivCall);
        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.CALL_PHONE)) {

                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    }
                } else {
                    realizarLlamada();
                }
            }
        });

        TextView tvRecuperaPass = (TextView)findViewById(R.id.link_recupera_pass);
        tvRecuperaPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RecuperarClaveActivity.class);
                startActivity(i);
            }
        });
        TextView tvRegistroUsuario = (TextView)findViewById(R.id.link_nuevo_reg);
        tvRegistroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegistroUsuarioActivity.class);
                startActivity(i);
            }
        });

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = ((TextView)findViewById(R.id.username)).getText().toString().trim();
                String clave = ((TextView)findViewById(R.id.password)).getText().toString().trim();

                if (usuario.isEmpty()){
                    Util.dialog_msg(MainActivity.this,"Ingrese su usuario").show();
                }
                else if (clave.isEmpty()){
                    Util.dialog_msg(MainActivity.this,"Ingrese su clave").show();
                }else{
                    showDialog();
                    Shared shared = new Shared(getApplicationContext());
                    shared.setUser(usuario);
                    ApiService.GetInstance().WS_UserAuthenticate(getApplicationContext(),handerUserAuthenticate, usuario, clave,"","", shared.getTokenFireBase());
                }
            }
        });
    }
    private void realizarLlamada(){
        try{
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:972939642"));
                startActivity(i);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    realizarLlamada();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private Handler handerUserAuthenticate = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try{
                UserAuthenticate response = Connection.process_handler(msg, getApplicationContext(), UserAuthenticate.class);
                if (response != null) {
                    EntityWSBase entityWSBase = Util.getEntityWSBase(msg);
                    if(entityWSBase.errorCode == 0){
                        Shared shared = new Shared(getApplicationContext());
                        shared.setToken(response.Token);

                        Intent i = new Intent(getApplicationContext(), PrincipalActivity.class);
                        startActivity(i);
                    } else{
                        Util.dialog_msg(MainActivity.this,entityWSBase.errorMessage).show();
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                hideDialog();
            }
        }
    };

    private void showDialog(){
        if (pd != null)
            pd.show();
    }

    private void hideDialog(){
        if (pd != null && pd.isShowing())
            pd.dismiss();
    }
}
