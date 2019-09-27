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
import android.widget.ListView;

import com.pe.cinefilos.PrincipalActivity;
import com.pe.cinefilos.R;
import com.pe.cinefilos.object.adapter.PeliculaAdapter;
import com.pe.cinefilos.object.entities.EntityWSBase;
import com.pe.cinefilos.object.entities.GetListMovie;
import com.pe.cinefilos.object.entities.Movie;
import com.pe.cinefilos.service.ApiService;
import com.pe.cinefilos.service.Connection;
import com.pe.cinefilos.util.Shared;
import com.pe.cinefilos.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListarPeliculasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListarPeliculasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListarPeliculasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listView;
    private Dialog pd;

    private OnFragmentInteractionListener mListener;

    public ListarPeliculasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListarPeliculasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListarPeliculasFragment newInstance(String param1, String param2) {
        ListarPeliculasFragment fragment = new ListarPeliculasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listar_peliculas, container, false);
        getActivity().setTitle("PELÍCULAS DE ESTRENO");
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pd = Util.get_progress_dialog(getContext());
        listView = (ListView) getView().findViewById(R.id.lvLista);

        /*
        List<Movie> peliculaList = new ArrayList<>();
        Movie pelicula = new Movie();
        pelicula.codigoPelicula = 1l;
        pelicula.nombre = "AVENGERS END GAME";
        pelicula.descripcion = "Los Vengadores restantes deben encontrar una manera de recuperar a sus aliados para un enfrentamiento épico con Thanos, el malvado que diezmó el planeta y el universo.";
        pelicula.image = "http://i.imgur.com/DvpvklR.png";
        pelicula.video = "http://techslides.com/demos/sample-videos/small.mp4";
        peliculaList.add(pelicula);

        pelicula = new Movie();
        pelicula.codigoPelicula = 1l;
        pelicula.nombre = "AVENGERS INFINITY WAR";
        pelicula.descripcion = "Los superhéroes se alían para vencer al poderoso Thanos, el peor enemigo al que se han enfrentado. Si Thanos logra reunir las seis gemas del infinito: poder, tiempo, alma, realidad, mente y espacio, nadie podrá detenerlo.";
        pelicula.image = "http://i.imgur.com/DvpvklR.png";
        pelicula.video = "http://techslides.com/demos/sample-videos/small.mp4";
        peliculaList.add(pelicula);

        pelicula = new Movie();
        pelicula.codigoPelicula = 1l;
        pelicula.nombre = "CAPITAN AMERICA CIVIL WAR";
        pelicula.descripcion = "Después de que otro incidente internacional, en el que se ven envueltos los Vengadores, produzca daños colaterales, la presión política obliga a poner en marcha un sistema para depurar responsabilidades.";
        pelicula.image = "http://i.imgur.com/DvpvklR.png";
        pelicula.video = "http://techslides.com/demos/sample-videos/small.mp4";
        peliculaList.add(pelicula);

        pelicula = new Movie();
        pelicula.codigoPelicula = 1l;
        pelicula.nombre = "NOMBRE4";
        pelicula.descripcion = "DESCRI4";
        pelicula.image = "https://en.wikipedia.org/wiki/Image#/media/File:Image_created_with_a_mobile_phone.png";
        pelicula.video = "http://techslides.com/demos/sample-videos/small.mp4";
        peliculaList.add(pelicula);

        PeliculaAdapter adapter = new PeliculaAdapter(getContext(), new ArrayList<Movie>(peliculaList));
        listView = (ListView) view.findViewById(R.id.lvLista);
        listView.setAdapter(adapter);
        */

        showDialog();
        ApiService.GetInstance().WS_GetListMovie(getContext(),handerGetListMovie,new Shared(getContext()).getToken());

        super.onViewCreated(view, savedInstanceState );
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

    private Handler handerGetListMovie = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try{
                GetListMovie response = Connection.process_handler(msg, getContext(), GetListMovie.class);
                if (response != null) {
                    EntityWSBase entityWSBase = Util.getEntityWSBase(msg);
                    if(entityWSBase.errorCode == 0){
                        PeliculaAdapter adapter = new PeliculaAdapter(getContext(), new ArrayList<Movie>(response.lista));
                        listView.setAdapter(adapter);
                    } else{
                        Util.dialog_msg(getActivity(),entityWSBase.errorMessage).show();
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
