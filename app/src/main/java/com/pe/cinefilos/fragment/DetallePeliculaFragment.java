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
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.pe.cinefilos.R;
import com.pe.cinefilos.object.entities.EntityWSBase;
import com.pe.cinefilos.object.entities.GetListMovie;
import com.pe.cinefilos.object.entities.Movie;
import com.pe.cinefilos.object.entities.UserAuthenticate;
import com.pe.cinefilos.service.ApiService;
import com.pe.cinefilos.service.Connection;
import com.pe.cinefilos.util.Shared;
import com.pe.cinefilos.util.Util;

public class DetallePeliculaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private Movie peliculaSelected;
    private OnFragmentInteractionListener mListener;
    private Dialog pd;

    public DetallePeliculaFragment(Movie pelicula) {
        // Required empty public constructor
        this.peliculaSelected = pelicula;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle_pelicula, container, false);
        getActivity().setTitle(peliculaSelected.nombre);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pd = Util.get_progress_dialog(getContext());
        //((PrincipalActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvTitulo = view.findViewById(R.id.tvTitulo);
        TextView tvDescripcion = view.findViewById(R.id.tvDescripcion);
        tvTitulo.setText(peliculaSelected.nombre);
        tvDescripcion.setText(peliculaSelected.descripcion);

        Uri uri = Uri.parse(peliculaSelected.video);
        VideoView vvPelicula = view.findViewById(R.id.vvPelicula);
        vvPelicula.setMediaController(new MediaController(getContext()));
        vvPelicula.setVideoURI(uri);
        vvPelicula.requestFocus();
        vvPelicula.start();

        super.onViewCreated( view, savedInstanceState );
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

}
