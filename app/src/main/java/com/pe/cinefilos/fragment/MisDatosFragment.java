package com.pe.cinefilos.fragment;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.pe.cinefilos.R;
import com.pe.cinefilos.RegistroUsuarioActivity;
import com.pe.cinefilos.object.entities.GetListTrivia;
import com.pe.cinefilos.object.entities.UserGetInformacion;
import com.pe.cinefilos.service.ApiService;
import com.pe.cinefilos.service.Connection;
import com.pe.cinefilos.util.Shared;
import com.pe.cinefilos.util.Util;

public class MisDatosFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Dialog pd;

    public MisDatosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mis_datos, container, false);
        getActivity().setTitle("MIS DATOS");
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pd = Util.get_progress_dialog(getContext());

        CheckBox chkCambiarClave = getView().findViewById(R.id.chkCambiarClave);
        chkCambiarClave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                EditText txtClave = getView().findViewById(R.id.txtPassword);
                EditText txtReClave = getView().findViewById(R.id.txtRePassword);
                if (b) {
                    txtReClave.setEnabled(true);
                    txtClave.setEnabled(true);
                } else {
                    txtReClave.setEnabled(false);
                    txtClave.setEnabled(false);
                }
            }
        });

        Button btnGuardar = getView().findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });

        showDialog();
        ApiService.GetInstance().WS_UserGetInformation(getContext(), handerUserGetInformacion, new Shared(getContext()).getToken());

        super.onViewCreated(view, savedInstanceState);
    }

    private void guardar() {
        EditText txtCorreo = getView().findViewById(R.id.txtCorreo);
        EditText txtDireccion = getView().findViewById(R.id.txtDireccion);
        CheckBox chkCambiarClave = getView().findViewById(R.id.chkCambiarClave);
        EditText txtPassword = getView().findViewById(R.id.txtPassword);
        EditText txtRePassword = getView().findViewById(R.id.txtRePassword);

        String correo = txtCorreo.getText().toString().trim();
        String direccion = txtDireccion.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String repassword = txtRePassword.getText().toString().trim();

        if (correo.isEmpty()) {
            Util.dialog_msg(getActivity(), "Ingrese su correo").show();
        } else if (direccion.isEmpty()) {
            Util.dialog_msg(getActivity(), "Ingrese su dirección").show();
        } else if (chkCambiarClave.isChecked() && (password.compareTo(repassword) != 0)) {
            Util.dialog_msg(getActivity(), "Las contraseñas no coinciden").show();
        } else {
            showDialog();
            ApiService.GetInstance().WS_UserUpdateInformation(getContext(), handerUserUpdateInformation, new Shared(getContext()).getToken(), correo, direccion, (chkCambiarClave.isChecked() ? password : ""));
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private Handler handerUserGetInformacion = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                UserGetInformacion userGetInformacion = Connection.process_handler(msg, getContext(), UserGetInformacion.class);
                if (userGetInformacion != null) {
                    if (userGetInformacion.errorCode == 0) {

                        Spinner spTipoDocumento = getView().findViewById(R.id.spTipoDoc);
                        EditText txtNroDoc = getView().findViewById(R.id.txtNroDoc);
                        EditText txtNombre = getView().findViewById(R.id.txtNombre);
                        EditText txtApellidos = getView().findViewById(R.id.txtApellidos);
                        EditText txtFechaNacimiento = getView().findViewById(R.id.txtFechaNacimiento);
                        EditText txtGenero = getView().findViewById(R.id.txtGenero);
                        EditText txtCorreo = getView().findViewById(R.id.txtCorreo);
                        EditText txtDireccion = getView().findViewById(R.id.txtDireccion);

                        txtNroDoc.setText(userGetInformacion.persona.dni);
                        txtNombre.setText(userGetInformacion.persona.nombres);
                        txtApellidos.setText(userGetInformacion.persona.apellidos);
                        txtFechaNacimiento.setText(userGetInformacion.persona.fechaNacimiento);
                        txtGenero.setText(userGetInformacion.persona.genero);
                        txtCorreo.setText(userGetInformacion.persona.email);
                        txtDireccion.setText(userGetInformacion.persona.direccion);

                    } else {
                        Util.dialog_msg(getActivity(), userGetInformacion.errorMessage).show();

                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                hideDialog();
            }
        }
    };

    private Handler handerUserUpdateInformation = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                UserGetInformacion userGetInformacion = Connection.process_handler(msg, getContext(), UserGetInformacion.class);
                if (userGetInformacion != null) {
                    if (userGetInformacion.errorCode == 0) {
                        Util.dialog_msg(getActivity(), "Actualización de datos exitoso").show();
                    } else {
                        Util.dialog_msg(getActivity(), userGetInformacion.errorMessage).show();

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
