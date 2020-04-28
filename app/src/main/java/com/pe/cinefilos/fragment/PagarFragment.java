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
import com.pe.cinefilos.object.entities.GetMontoPago;
import com.pe.cinefilos.object.entities.Horario;
import com.pe.cinefilos.object.entities.Movie;
import com.pe.cinefilos.object.entities.RealizarPago;
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

        List<Long> butacas = new ArrayList<>();
        for(Butaca butaca : asientosSelected) {
            butacas.add(butaca.codigoButaca);
        }

        showDialog();
        ApiService.GetInstance().WS_GetMontoPago(getContext(), handlerGetMontoPago, new Shared(getContext()).getToken(), horarioSelected.getCodigoPadre() , butacas);

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
                break;
        }
        return true;

    }

    private Handler handlerGetMontoPago = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                GetMontoPago getMontoPago = Connection.process_handler(msg, getContext(), GetMontoPago.class);
                if (getMontoPago != null) {
                    if (getMontoPago.errorCode == 0) {

                        EditText txtMontoPagar = getView().findViewById(R.id.txtMontoPagar);
                        txtMontoPagar.setText(String.valueOf(getMontoPago.montoPago.total));

                    } else {
                        Util.dialog_msg(getActivity(), getMontoPago.errorMessage).show();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                hideDialog();
            }
        }
    };

    private Handler handlerRealizarPago = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                RealizarPago realizarPago = Connection.process_handler(msg, getContext(), RealizarPago.class);
                if (realizarPago != null) {
                    if (realizarPago.errorCode == 0) {

                        FragmentManager fragmentManager = ((PrincipalActivity)getContext()).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contenedor, new ConstanciaFragment(peliculaSelected, cineSelected, horarioSelected, asientosSelected, realizarPago.pago)).addToBackStack("my_fragment").commit();

                    } else {
                        Util.dialog_msg(getActivity(), realizarPago.errorMessage).show();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                hideDialog();
            }
        }
    };

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
        } else if (numeroTarjeta.length() < 14) {
            Util.dialog_msg(getActivity(), "El número de tarjeta debe tener una longitud de 14 dígitos como mínimo").show();
        } else if (mesVenc.isEmpty()) {
            Util.dialog_msg(getActivity(), "Ingrese mes de vencimiento").show();
        } else if (anioVenc.isEmpty()) {
            Util.dialog_msg(getActivity(), "Ingrese año de vencimiento").show();
        } else if (anioVenc.length() != 4) {
            Util.dialog_msg(getActivity(), "El año de vencimiento debe tener 4 dígitos").show();
        } else if (cVV.isEmpty()) {
            Util.dialog_msg(getActivity(), "Ingrese CVV").show();
        } else if (cVV.length() < 3) {
            Util.dialog_msg(getActivity(), "El CVV debe tener una longitud de 3 carácteres").show();
        }else {

            List<Long> butacas = new ArrayList<>();
            for(Butaca butaca : asientosSelected) {
                butacas.add(butaca.codigoButaca);
            }

            showDialog();
            ApiService.GetInstance().WS_RealizarPago(getContext(), handlerRealizarPago, new Shared(getContext()).getToken(), horarioSelected.getCodigoPadre() , butacas, numeroTarjeta, cVV, anioVenc, mesVenc);
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

    private void showDialog() {
        if (pd != null)
            pd.show();
    }

    private void hideDialog() {
        if (pd != null && pd.isShowing())
            pd.dismiss();
    }
}
