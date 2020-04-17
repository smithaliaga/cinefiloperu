package com.pe.cinefilos.fragment;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.pe.cinefilos.PrincipalActivity;
import com.pe.cinefilos.R;
import com.pe.cinefilos.bean.BeanButaca;
import com.pe.cinefilos.bean.BeanCombo;
import com.pe.cinefilos.object.entities.Butaca;
import com.pe.cinefilos.object.entities.Cine;
import com.pe.cinefilos.object.entities.Horario;
import com.pe.cinefilos.object.entities.Movie;
import com.pe.cinefilos.object.entities.UserGetInformacion;
import com.pe.cinefilos.service.ApiService;
import com.pe.cinefilos.service.Connection;
import com.pe.cinefilos.util.Shared;
import com.pe.cinefilos.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PagarFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Dialog pd;
    private Movie peliculaSelected;
    private BeanCombo cineSelected;
    private BeanCombo horarioSelected;
    private List<Butaca> asientosSelected;

    public PagarFragment(Movie pelicula, BeanCombo cine, BeanCombo horario, List<Butaca> asientosSelected ) {
        // Required empty public constructor
        this.peliculaSelected = pelicula;
        this.cineSelected = cine;
        this.horarioSelected = horario;
        this.asientosSelected = asientosSelected;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pagar, container, false);
        getActivity().setTitle(peliculaSelected.nombre);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pd = Util.get_progress_dialog(getContext());

        EditText txtNumeroAsientos = view.findViewById(R.id.txtNumeroAsientos);
        EditText txtMontoPagar = view.findViewById(R.id.txtMontoPagar);
        EditText txtCine = view.findViewById(R.id.txtCine);
        EditText txtHorario = view.findViewById(R.id.txtHorario);
        EditText txtSala = view.findViewById(R.id.txtSala);

        txtNumeroAsientos.setText(String.valueOf(asientosSelected.size()));
        txtCine.setText(cineSelected.getDescripcion());
        txtHorario.setText(horarioSelected.getDescripcion());
        txtSala.setText(horarioSelected.getDescripcionPadre());

        Button btnGuardar = getView().findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.principal, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                guardar();
                //FragmentManager fragmentManager = ((PrincipalActivity)getContext()).getSupportFragmentManager();
                //fragmentManager.beginTransaction().replace(R.id.contenedor, new PagarFragment(peliculaSelected, cineSelected, horarioSelected, asientosSeleccionados)).addToBackStack("my_fragment").commit();
                break;
        }
        return true;

    }

    private void guardar() {
        EditText txtNumeroTarjeta = getView().findViewById(R.id.txtNumeroTarjeta);
        EditText txtMesVenc = getView().findViewById(R.id.txtMesVenc);
        EditText txtAnioVenc = getView().findViewById(R.id.txtAnioVenc);
        EditText txtCVV = getView().findViewById(R.id.txtCVV);

        String numeroTarjeta = txtNumeroTarjeta.getText().toString().trim();
        String mesVenc = txtMesVenc.getText().toString().trim();
        String anioVenc = txtAnioVenc.getText().toString().trim();
        String cVV = txtCVV.getText().toString().trim();

        if (numeroTarjeta.isEmpty()) {
            Util.dialog_msg(getActivity(), "Ingrese un número de tarjeta").show();
        } else if (mesVenc.isEmpty()) {
            Util.dialog_msg(getActivity(), "Ingrese mes de vencimiento").show();
        } else if (anioVenc.isEmpty()) {
            Util.dialog_msg(getActivity(), "Ingrese año de vencimiento").show();
        } else if (cVV.isEmpty()) {
            Util.dialog_msg(getActivity(), "Ingrese CVV").show();
        } else {
            showDialog();
            //ApiService.GetInstance().WS_UserUpdateInformation(getContext(), handerUserUpdateInformation, new Shared(getContext()).getToken(), correo, direccion, (chkCambiarClave.isChecked() ? password : ""));
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
