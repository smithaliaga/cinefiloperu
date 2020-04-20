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
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.pe.cinefilos.R;
import com.pe.cinefilos.bean.BeanCombo;
import com.pe.cinefilos.object.entities.Butaca;
import com.pe.cinefilos.object.entities.GetMontoPago;
import com.pe.cinefilos.object.entities.MontoPago;
import com.pe.cinefilos.object.entities.Movie;
import com.pe.cinefilos.object.entities.Pago;
import com.pe.cinefilos.object.entities.UserGetInformacion;
import com.pe.cinefilos.service.ApiService;
import com.pe.cinefilos.service.Connection;
import com.pe.cinefilos.util.Shared;
import com.pe.cinefilos.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ConstanciaFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Dialog pd;
    private Movie peliculaSelected;
    private BeanCombo cineSelected;
    private BeanCombo horarioSelected;
    private List<Butaca> asientosSelected;
    private String tarjetaOfuscada;
    private Pago pago;

    public ConstanciaFragment(Movie pelicula, BeanCombo cine, BeanCombo horario, List<Butaca> asientosSelected, Pago pago) {
        // Required empty public constructor
        this.peliculaSelected = pelicula;
        this.cineSelected = cine;
        this.horarioSelected = horario;
        this.asientosSelected = asientosSelected;
        this.tarjetaOfuscada = tarjetaOfuscada;
        this.pago = pago;
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
        View view = inflater.inflate(R.layout.fragment_constancia, container, false);
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


        //showDialog();
        //ApiService.GetInstance().WS_GetMontoPago(getContext(), handlerGetMontoPago, new Shared(getContext()).getToken(), horarioSelected.getCodigoPadre() , butacas);

        super.onViewCreated(view, savedInstanceState);
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
