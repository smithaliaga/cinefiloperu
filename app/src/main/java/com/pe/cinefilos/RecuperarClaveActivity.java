package com.pe.cinefilos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pe.cinefilos.object.entities.EntityWSBase;
import com.pe.cinefilos.object.entities.UserAuthenticate;
import com.pe.cinefilos.service.ApiService;
import com.pe.cinefilos.service.Connection;
import com.pe.cinefilos.util.Shared;
import com.pe.cinefilos.util.Util;

public class RecuperarClaveActivity extends AppCompatActivity {

    private Dialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_clave);
        pd = Util.get_progress_dialog(this);

        final Button btnRecupera = findViewById(R.id.btnRecuperaPass);
        btnRecupera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                EditText txtUsuario = findViewById(R.id.txtUsuario);
                ApiService.GetInstance().WS_RecoveryPassword(getApplicationContext(), handerRecoveryPassword, txtUsuario.getText().toString());
            }
        });
    }

    private Handler handerRecoveryPassword = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            boolean estadoEvento = false;
            try {
                EntityWSBase response = Connection.process_handler(msg, getApplicationContext(), EntityWSBase.class);
                if (response != null) {
                    if (response.errorCode == 0) {
                        estadoEvento = true;
                    } else {
                        Util.dialog_msg(RecuperarClaveActivity.this, response.errorMessage).show();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                hideDialog();
            }
            if (estadoEvento) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        }
    };

    private void showDialog() {
        if (pd != null)
            pd.show();
    }

    private void hideDialog() {
        if (pd != null && pd.isShowing())
            pd.dismiss();
    }
}
