package com.pe.cinefilos.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pe.cinefilos.R;
import com.pe.cinefilos.object.entities.Cine;
import com.pe.cinefilos.object.entities.GetListCine;
import com.pe.cinefilos.service.ApiService;
import com.pe.cinefilos.service.Connection;
import com.pe.cinefilos.util.Shared;
import com.pe.cinefilos.util.Util;

import java.util.ArrayList;
import java.util.List;

public class UbicacionCinesFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    private OnFragmentInteractionListener mListener;
    private Dialog pd;
    private List<Cine> listaCine = new ArrayList<>();

    public UbicacionCinesFragment() {
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

        View view = inflater.inflate(R.layout.fragment_ubicacion_cines, container, false);
        getActivity().setTitle("UBICACIÓN DE CINES");

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pd = Util.get_progress_dialog(getContext());

        showDialog();
        ApiService.GetInstance().WS_GetListCine(getContext(), handlerGetListCine, new Shared(getContext()).getToken());

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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    try{
                        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, (LocationListener) this);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 5, (LocationListener) this);
                    }catch (SecurityException ex){
                        ex.printStackTrace();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

        if (listaCine.isEmpty()){
            LatLng cinefilosRisso = new LatLng(-12.085507, -77.034625);
            LatLng cinefilosSalaverry = new LatLng(-12.089494, -77.052604);
            mMap.addMarker(new
                    MarkerOptions().position(cinefilosRisso).title("Cinefilos Risso"));
            mMap.addMarker(new
                    MarkerOptions().position(cinefilosSalaverry).title("Cinefilos Salaverry"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cinefilosSalaverry, 12.0f));
        } else {
            for(Cine cine : listaCine){
                LatLng ubicacion = new LatLng(cine.latitud, cine.longitud);
                mMap.addMarker(new
                        MarkerOptions().position(ubicacion).title(cine.nombre));
            }
        }

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                // MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, (LocationListener) this);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 5, (LocationListener) this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng myCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myCurrentLocation, 14.0f));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        System.out.println("onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String s) {
        System.out.println("onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String s) {
        System.out.println("onProviderDisabled");
    }

    private Handler handlerGetListCine = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                GetListCine getListCine = Connection.process_handler(msg, getContext(), GetListCine.class);
                if (getListCine != null) {
                    if (getListCine.errorCode == 0) {
                        listaCine = new ArrayList<>(getListCine.lista);
                        for(Cine cine : listaCine){
                            LatLng ubicacion = new LatLng(cine.latitud, cine.longitud);
                            mMap.addMarker(new
                                    MarkerOptions().position(ubicacion).title(cine.nombre));
                        }
                    } else {
                        Util.dialog_msg(getActivity(), getListCine.errorMessage).show();
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
