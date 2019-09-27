package com.pe.cinefilos.fragment;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pe.cinefilos.PrincipalActivity;
import com.pe.cinefilos.R;
import com.pe.cinefilos.object.adapter.PeliculaAdapter;
import com.pe.cinefilos.object.adapter.TriviaRespuestaAdapter;
import com.pe.cinefilos.object.entities.EntityWSBase;
import com.pe.cinefilos.object.entities.GetListMovie;
import com.pe.cinefilos.object.entities.GetListTrivia;
import com.pe.cinefilos.object.entities.Movie;
import com.pe.cinefilos.object.entities.RegisterIntentTrivia;
import com.pe.cinefilos.object.entities.TriviaRespuesta;
import com.pe.cinefilos.service.ApiService;
import com.pe.cinefilos.service.Connection;
import com.pe.cinefilos.util.Shared;
import com.pe.cinefilos.util.Util;

import java.util.ArrayList;
import java.util.List;

public class TriviaFragment extends Fragment {

    private ListView listView;
    private Dialog pd;
    private GetListTrivia getListTrivia;
    private int cantidadPreguntasMostradas = 0;

    private OnFragmentInteractionListener mListener;

    public TriviaFragment() {
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
        View view = inflater.inflate(R.layout.fragment_trivia, container, false);
        getActivity().setTitle("TRIVIA DEL DÍA");
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pd = Util.get_progress_dialog(getContext());
        listView = (ListView) getView().findViewById(R.id.lvLista);
        /*
        List<TriviaRespuesta> listaRespuesta = new ArrayList<>();
        TriviaRespuesta triviaRespuesta = new TriviaRespuesta();
        triviaRespuesta.codigoTriviaRespuesta = 1L;
        triviaRespuesta.respuesta = "Hulk";
        triviaRespuesta.estadoRespuesta = false;
        listaRespuesta.add(triviaRespuesta);

        triviaRespuesta = new TriviaRespuesta();
        triviaRespuesta.codigoTriviaRespuesta = 1L;
        triviaRespuesta.respuesta = "Iron Man";
        triviaRespuesta.estadoRespuesta = false;
        listaRespuesta.add(triviaRespuesta);

        triviaRespuesta = new TriviaRespuesta();
        triviaRespuesta.codigoTriviaRespuesta = 1L;
        triviaRespuesta.respuesta = "Thor";
        triviaRespuesta.estadoRespuesta = false;
        listaRespuesta.add(triviaRespuesta);

        TextView tvTitulo = view.findViewById(R.id.tvTitulo);
        tvTitulo.setText("QUIEN ES EL VENGADOR MAS FUERTE?");

        TriviaRespuestaAdapter adapter = new TriviaRespuestaAdapter(getContext(), new ArrayList<TriviaRespuesta>(listaRespuesta));
        listView = (ListView) view.findViewById(R.id.lvLista);
        listView.setAdapter(adapter);
        */

        Button btnAceptar = view.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TriviaRespuestaAdapter triviaRespuestaAdapter = (TriviaRespuestaAdapter) listView.getAdapter();
                TriviaRespuesta triviaRespuestaSelected = (TriviaRespuesta) listView.getItemAtPosition(triviaRespuestaAdapter.selectedPosition);

                if (triviaRespuestaSelected.estadoRespuesta) {
                    if (!actualizarPregunta()) {
                        showDialog();
                        ApiService.GetInstance().WS_RegisterIntentTrivia(getContext(), handlerRegisterIntentTrivia, new Shared(getContext()).getToken(), getListTrivia.codigoTrivia, triviaRespuestaSelected.estadoRespuesta);
                    }
                } else {
                    showDialog();
                    ApiService.GetInstance().WS_RegisterIntentTrivia(getContext(), handlerRegisterIntentTrivia, new Shared(getContext()).getToken(), getListTrivia.codigoTrivia, triviaRespuestaSelected.estadoRespuesta);
                }
            }
        });

        showDialog();
        ApiService.GetInstance().WS_GetListTrivia(getContext(), handerGetListTrivia, new Shared(getContext()).getToken());

        super.onViewCreated(view, savedInstanceState);
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

    private Handler handerGetListTrivia = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                getListTrivia = Connection.process_handler(msg, getContext(), GetListTrivia.class);
                if (getListTrivia != null) {
                    if (getListTrivia.errorCode == 0) {
                        ApiService.GetInstance().WS_GetTriviaUser(getContext(), handlerGetTriviaUser, new Shared(getContext()).getToken(), getListTrivia.codigoTrivia);
                        actualizarPregunta();
                    } else {
                        Util.dialog_msg(getActivity(), getListTrivia.errorMessage).show();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                hideDialog();
            }
        }
    };

    private Handler handlerRegisterIntentTrivia = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                RegisterIntentTrivia registerIntentTrivia = Connection.process_handler(msg, getContext(), RegisterIntentTrivia.class);
                if (registerIntentTrivia != null) {
                    if (registerIntentTrivia.codigoTriviaUsuario != null){
                        if (registerIntentTrivia.estadoRespuesta){
                            FragmentManager fragmentManager = ((PrincipalActivity) getContext()).getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.contenedor, new TriviaResultadoFragment(registerIntentTrivia.codigoTriviaUsuario)).addToBackStack("my_fragment").commit();
                        }else{
                            ((Button) getView().findViewById(R.id.btnAceptar)).setEnabled(false);
                            ((ListView) getView().findViewById(R.id.lvLista)).setEnabled(false);
                            Util.dialog_msg(getActivity(), "Lo sentimos pero su respuesta fue incorrecta, intentelo en otra oportunidad").show();
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                hideDialog();
            }
        }
    };

    private Handler handlerGetTriviaUser = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                RegisterIntentTrivia registerIntentTrivia = Connection.process_handler(msg, getContext(), RegisterIntentTrivia.class);
                if (registerIntentTrivia != null) {
                    if (registerIntentTrivia.errorCode == 0) {
                        if (registerIntentTrivia.codigoTriviaUsuario != null){
                            if (registerIntentTrivia.estadoRespuesta){
                                FragmentManager fragmentManager = ((PrincipalActivity) getContext()).getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.contenedor, new TriviaResultadoFragment(registerIntentTrivia.codigoTriviaUsuario)).addToBackStack("my_fragment").commit();
                            }else{
                                ((Button) getView().findViewById(R.id.btnAceptar)).setEnabled(false);
                                ((ListView) getView().findViewById(R.id.lvLista)).setEnabled(false);
                                Util.dialog_msg(getActivity(), "Lo sentimos pero su respuesta fue incorrecta, intentelo en otra oportunidad").show();
                            }
                        }
                    } else {
                        Util.dialog_msg(getActivity(), registerIntentTrivia.errorMessage).show();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                hideDialog();
            }
        }
    };

    private boolean actualizarPregunta() {
        if (getListTrivia.preguntas != null && getListTrivia.preguntas.size() > cantidadPreguntasMostradas) {
            TextView tvTitulo = getView().findViewById(R.id.tvTitulo);
            tvTitulo.setText(getListTrivia.preguntas.get(cantidadPreguntasMostradas).pregunta);
            TriviaRespuestaAdapter adapter = new TriviaRespuestaAdapter(getContext(), new ArrayList<TriviaRespuesta>(getListTrivia.preguntas.get(cantidadPreguntasMostradas).respuestas));
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(adapter);

            cantidadPreguntasMostradas++;
            return true;
        } else {
            return false;
        }
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
