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
import android.widget.ImageView;
import android.widget.Spinner;

import com.pe.cinefilos.dialog.DialogDatePicker;
import com.pe.cinefilos.object.entities.EntityWSBase;
import com.pe.cinefilos.object.entities.UserAuthenticate;
import com.pe.cinefilos.service.ApiService;
import com.pe.cinefilos.service.Connection;
import com.pe.cinefilos.util.Shared;
import com.pe.cinefilos.util.Util;

public class RegistroUsuarioActivity extends AppCompatActivity {

    private Dialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        setTitle("REGISTRO DE USUARIO");

        pd = Util.get_progress_dialog(this);

        ImageView ivFechaNacimiento = findViewById(R.id.btnFechaNacimiento);
        ivFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDatePicker dialogDatePicker = new DialogDatePicker();
                dialogDatePicker.setParametros((EditText) findViewById(R.id.txtFechaNacimiento),
                        ((EditText) findViewById(R.id.txtFechaNacimiento)).getText().toString(), getApplicationContext());
                dialogDatePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });

        Button btnRegistrar = findViewById(R.id.btnNuevoRegistro);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });
    }

    private void guardar() {
        try {
            Spinner spTipoDocumento = findViewById(R.id.spTipoDoc);
            EditText txtNroDoc = findViewById(R.id.txtNroDoc);
            EditText txtNombre = findViewById(R.id.txtNombre);
            EditText txtApellidos = findViewById(R.id.txtApellidos);
            EditText txtFechaNacimiento = findViewById(R.id.txtFechaNacimiento);
            Spinner spGenero = findViewById(R.id.spGenero);
            EditText txtCorreo = findViewById(R.id.txtCorreo);
            EditText txtDireccion = findViewById(R.id.txtDireccion);
            EditText txtUsuario = findViewById(R.id.txtUsuario);
            EditText txtClave = findViewById(R.id.txtPassword);
            EditText txtReClave = findViewById(R.id.txtRePassword);

            String tipoDocumento = spTipoDocumento.getSelectedItem().toString();
            String numeroDocumento = txtNroDoc.getText().toString().trim();
            String nombre = txtNombre.getText().toString().trim();
            String apellidos = txtApellidos.getText().toString().trim();
            String fechaNacimiento = txtFechaNacimiento.getText().toString().trim();
            String genero = spGenero.getSelectedItem().toString();
            String correo = txtCorreo.getText().toString().trim();
            String direccion = txtDireccion.getText().toString().trim();
            String usuario = txtUsuario.getText().toString().trim();
            String clave = txtClave.getText().toString().trim();
            String reClave = txtReClave.getText().toString().trim();


            if (tipoDocumento.toLowerCase().contains("seleccione")) {
                Util.dialog_msg(RegistroUsuarioActivity.this, "Seleccione un tipo de documento").show();
            } else if (numeroDocumento.isEmpty()) {
                Util.dialog_msg(RegistroUsuarioActivity.this, "Ingrese su número de documento").show();
            } else if (nombre.isEmpty()) {
                Util.dialog_msg(RegistroUsuarioActivity.this, "Ingrese su nomnbre").show();
            } else if (apellidos.isEmpty()) {
                Util.dialog_msg(RegistroUsuarioActivity.this, "Ingrese sus apellidos").show();
            } else if (fechaNacimiento.isEmpty()) {
                Util.dialog_msg(RegistroUsuarioActivity.this, "Seleccione su fecha de nacimiento").show();
            } else if (genero.toLowerCase().contains("seleccione")) {
                Util.dialog_msg(RegistroUsuarioActivity.this, "Seleccione su genero").show();
            } else if (correo.isEmpty()) {
                Util.dialog_msg(RegistroUsuarioActivity.this, "Ingrese su correo").show();
            } else if (direccion.isEmpty()) {
                Util.dialog_msg(RegistroUsuarioActivity.this, "Ingrese su dirección").show();
            } else if (usuario.isEmpty()) {
                Util.dialog_msg(RegistroUsuarioActivity.this, "Ingrese su usuario").show();
            } else if (clave.isEmpty()) {
                Util.dialog_msg(RegistroUsuarioActivity.this, "Ingrese una contraseña").show();
            } else if (reClave.isEmpty()) {
                Util.dialog_msg(RegistroUsuarioActivity.this, "Repetir contraseña").show();
            } else if (clave.compareTo(reClave) != 0) {
                Util.dialog_msg(RegistroUsuarioActivity.this, "Las contraseñas no coinciden").show();
            } else {
                showDialog();

                Shared shared = new Shared(getApplicationContext());
                shared.setUser(usuario);

                ApiService.GetInstance().WS_RegisterUser(getApplicationContext(), handerRegisterUser, numeroDocumento, nombre, apellidos,
                        Util.convertDateStringToFormatDateString(fechaNacimiento, "dd/MM/yyyy", "yyyyMMdd"), genero, correo, direccion, usuario, clave);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Handler handerRegisterUser = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                EntityWSBase response = Connection.process_handler(msg, getApplicationContext(), EntityWSBase.class);
                if (response != null) {
                    if (response.errorCode == 0) {
                        Util.dialog_msg(RegistroUsuarioActivity.this, "Registro exitoso, ya puede iniciar sesión");

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    } else {
                        Util.dialog_msg(RegistroUsuarioActivity.this, response.errorMessage).show();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                hideDialog();
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
