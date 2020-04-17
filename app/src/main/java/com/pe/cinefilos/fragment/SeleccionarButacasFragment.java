package com.pe.cinefilos.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.pe.cinefilos.PrincipalActivity;
import com.pe.cinefilos.R;
import com.pe.cinefilos.bean.BeanButaca;
import com.pe.cinefilos.bean.BeanCombo;
import com.pe.cinefilos.bean.BeanFilaButaca;
import com.pe.cinefilos.object.adapter.ComboAdapter;
import com.pe.cinefilos.object.entities.Butaca;
import com.pe.cinefilos.object.entities.Cine;
import com.pe.cinefilos.object.entities.GetListButaca;
import com.pe.cinefilos.object.entities.GetListCine;
import com.pe.cinefilos.object.entities.GetListHorario;
import com.pe.cinefilos.object.entities.Horario;
import com.pe.cinefilos.object.entities.Movie;
import com.pe.cinefilos.service.ApiService;
import com.pe.cinefilos.service.Connection;
import com.pe.cinefilos.util.Shared;
import com.pe.cinefilos.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SeleccionarButacasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private Movie peliculaSelected;
    private OnFragmentInteractionListener mListener;
    private Dialog pd;
    private ComboAdapter cineAdapter;
    private ComboAdapter horarioAdapter;
    private Spinner spCine;
    private Spinner spHorario;
    private BeanCombo cineSelected;
    private BeanCombo horarioSelected;
    private Map<Long, Butaca> mapButaca = new HashMap<>();

    private GradientDrawable drawableWhite = new GradientDrawable();
    private GradientDrawable drawableYellow = new GradientDrawable();
    private GradientDrawable drawableGray = new GradientDrawable();
    private GradientDrawable drawableGreen = new GradientDrawable();

    public SeleccionarButacasFragment(Movie pelicula) {
        // Required empty public constructor
        this.peliculaSelected = pelicula;
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
        View view = inflater.inflate(R.layout.fragment_seleccionar_butacas, container, false);
        getActivity().setTitle(peliculaSelected.nombre);

        return view;
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

                List<Butaca> asientosSeleccionados = new ArrayList<>();

                for (Butaca butaca : mapButaca.values()) {
                    if (butaca.codigoEstadoButaca == 3) {
                        asientosSeleccionados.add(butaca);
                    }
                }

                FragmentManager fragmentManager = ((PrincipalActivity)getContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contenedor, new PagarFragment(peliculaSelected, cineSelected, horarioSelected, asientosSeleccionados)).addToBackStack("my_fragment").commit();
                break;
        }
        return true;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pd = Util.get_progress_dialog(getContext());
        //((PrincipalActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spCine = view.findViewById(R.id.spCine);
        spHorario = view.findViewById(R.id.spHorario);

        drawableWhite.setShape(GradientDrawable.RECTANGLE);
        drawableWhite.setStroke(5, Color.BLACK);
        drawableWhite.setColor(Color.WHITE);

        drawableYellow.setShape(GradientDrawable.RECTANGLE);
        drawableYellow.setStroke(5, Color.BLACK);
        drawableYellow.setColor(Color.YELLOW);

        drawableGray.setShape(GradientDrawable.RECTANGLE);
        drawableGray.setStroke(5, Color.BLACK);
        drawableGray.setColor(Color.LTGRAY);

        drawableGreen.setShape(GradientDrawable.RECTANGLE);
        drawableGreen.setStroke(5, Color.BLACK);
        drawableGreen.setColor(Color.GREEN);

        spCine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                cineSelected = cineAdapter.getItem(position);
                showDialog();
                ApiService.GetInstance().WS_GetListHorario(getContext(), handlerGetListHorario, new Shared(getContext()).getToken(), cineSelected.getCodigo() , peliculaSelected.codigoPelicula);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        spHorario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                horarioSelected = horarioAdapter.getItem(position);
                showDialog();
                ApiService.GetInstance().WS_GetListButaca(getContext(), handlerGetListButaca, new Shared(getContext()).getToken(), horarioSelected.getCodigoPadre());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        showDialog();
        ApiService.GetInstance().WS_GetListCine(getContext(), handlerGetListCine, new Shared(getContext()).getToken());

        TableLayout leyendaTable = (TableLayout) view.findViewById(R.id.leyenda_table);
        leyendaTable.setGravity(Gravity.CENTER);

        TableRow leyendaRow = new TableRow(this.getContext());
        leyendaRow.setGravity(Gravity.CENTER);

        final Button leyendaVacioBtn = new Button(this.getContext());
        leyendaVacioBtn.setLayoutParams (new TableRow.LayoutParams(280, 150));
        leyendaVacioBtn.setPadding(0,0,0,0);
        leyendaVacioBtn.requestLayout();
        leyendaVacioBtn.setBackground(drawableWhite);
        leyendaVacioBtn.setText("Vacío");
        leyendaVacioBtn.setTextSize(13);
        leyendaVacioBtn.setAllCaps(false);
        leyendaRow.addView(leyendaVacioBtn);

        final Button leyendaCompradoBtn = new Button(this.getContext());
        leyendaCompradoBtn.setLayoutParams (new TableRow.LayoutParams(280 ,150));
        leyendaCompradoBtn.setPadding(0,0,0,0);
        leyendaCompradoBtn.requestLayout();
        leyendaCompradoBtn.setBackground(drawableYellow);
        leyendaCompradoBtn.setText("Comprado");
        leyendaCompradoBtn.setTextSize(13);
        leyendaCompradoBtn.setAllCaps(false);
        leyendaRow.addView(leyendaCompradoBtn);

        final Button leyendaSeleccionadoBtn = new Button(this.getContext());
        leyendaSeleccionadoBtn.setLayoutParams (new TableRow.LayoutParams(280, 150));
        leyendaSeleccionadoBtn.setPadding(0,0,0,0);
        leyendaSeleccionadoBtn.requestLayout();
        leyendaSeleccionadoBtn.setBackground(drawableGreen);
        leyendaSeleccionadoBtn.setText("Seleccion.");
        leyendaSeleccionadoBtn.setTextSize(13);
        leyendaSeleccionadoBtn.setAllCaps(false);
        leyendaRow.addView(leyendaSeleccionadoBtn);

        leyendaTable.addView(leyendaRow);


        /*
        TableLayout tlayout = (TableLayout) view.findViewById(R.id.main_table);
        tlayout.setGravity(Gravity.CENTER);

        TableRow row = null;

        final List<BeanFilaButaca> listaFilaButaca = obtenerButacas();

        for (BeanFilaButaca beanFilaButaca : listaFilaButaca){

            row = new TableRow(this.getContext());

            for (final BeanButaca beanButaca : beanFilaButaca.getLista()){

                mapButaca.put(beanButaca.getId(), beanButaca);

                final Button btn = new Button(this.getContext());

                btn.setLayoutParams (new TableRow.LayoutParams(160, 160));
                btn.setPadding(0,0,0,0);
                btn.requestLayout();
                //btn.setBackgroundResource(R.drawable.butaca_negra);

                if (beanButaca.getEstado() == 0) {
                    // nulo
                    btn.setBackground(drawableGray);
                    btn.setText("");
                } else if (beanButaca.getEstado() == 1) {
                    // vacio
                    btn.setBackground(drawableWhite);
                    btn.setText(beanButaca.getNombre());
                } else if (beanButaca.getEstado() == 2) {
                    // comprado
                    btn.setBackground(drawableYellow);
                    btn.setText(beanButaca.getNombre());
                }

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        BeanButaca beanButacaSeleccionada = mapButaca.get(beanButaca.getId());

                        if (beanButacaSeleccionada.getEstado() == 0 || beanButacaSeleccionada.getEstado() == 2) {
                            return;
                        }

                        if (beanButacaSeleccionada.getEstado() == 3){

                            btn.setBackground(drawableWhite);
                            beanButacaSeleccionada.setEstado(1);
                            mapButaca.put(beanButacaSeleccionada.getId(), beanButacaSeleccionada);

                        } else if (beanButacaSeleccionada.getEstado() == 1){

                            btn.setBackground(drawableGreen);
                            beanButacaSeleccionada.setEstado(3);
                            mapButaca.put(beanButacaSeleccionada.getId(), beanButacaSeleccionada);

                        }

                    }
                });

                row.addView(btn);

            }

            tlayout.addView(row);
        }
        */
        super.onViewCreated( view, savedInstanceState );
    }

    /*
    private List<BeanFilaButaca> obtenerButacas() {
        List<BeanFilaButaca> listaFilaButaca = new ArrayList<>();

        List<String> letras = new ArrayList<>();
        letras.add("A");
        letras.add("B");
        letras.add("C");
        letras.add("D");
        letras.add("E");
        letras.add("F");
        letras.add("G");
        letras.add("H");
        letras.add("I");
        letras.add("J");
        letras.add("K");
        letras.add("L");
        letras.add("M");
        letras.add("N");
        letras.add("O");

        int cantidadFilas = 15;
        int cantidadColumnas = 20;

        for (int f = 1; f < cantidadFilas + 1; f ++) {

            BeanFilaButaca beanFilaButaca = new BeanFilaButaca();
            beanFilaButaca.setLista(new ArrayList<BeanButaca>());

            int cont = 0;
            for (int c = 1; c < cantidadColumnas + 1; c ++) {

                BeanButaca beanButaca = new BeanButaca();
                beanButaca.setFila(letras.get(f - 1));
                beanButaca.setId(f + "-" + c);

                //beanButaca.setEsVacio(false);

                if (c == 5 || c == 16){
                    beanButaca.setNombre("");
                    beanButaca.setEstado(0);
                } else {
                    cont ++;
                    beanButaca.setNombre(letras.get(f - 1) + cont);
                    beanButaca.setEstado(1);
                }

                // estado
                // 0: Nulo
                // 1: Vacio
                // 2: Comprado
                // 3: Seleccionado

                //agregamos la columna
                beanFilaButaca.getLista().add(beanButaca);
            }

            //agregamos la fila
            listaFilaButaca.add(beanFilaButaca);
        }

        StringBuilder sp = new StringBuilder();
        sp.append("INSERT INTO butaca (nombre, codigo_estado_butaca, codigo_sala, fila) values ");
        for (BeanFilaButaca bean1 : listaFilaButaca){

            for (BeanButaca bean2 : bean1.getLista()){
                sp.append("('" + bean2.getNombre() + "',1,1,'" + bean2.getFila() + "'),");
            }

        }
        sp.append(";");
        System.out.println(sp.toString().substring(0, 700));
        System.out.println(sp.toString().substring(700, sp.toString().length()));
        return listaFilaButaca;
    }
    */

    private Handler handlerGetListCine = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                GetListCine getListCine = Connection.process_handler(msg, getContext(), GetListCine.class);
                if (getListCine != null) {
                    if (getListCine.errorCode == 0) {
                        List<Cine> listaCine = new ArrayList<>(getListCine.lista);

                        List<BeanCombo> listaCombo = new ArrayList<>();
                        for (Cine cine : listaCine) {
                            listaCombo.add(new BeanCombo(cine.codigoCine, cine.nombre));
                        }

                        cineAdapter = new ComboAdapter(getContext(), android.R.layout.simple_spinner_item, listaCombo);
                        spCine.setAdapter(cineAdapter);

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

    private Handler handlerGetListHorario = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                GetListHorario getListHorario = Connection.process_handler(msg, getContext(), GetListHorario.class);
                if (getListHorario != null) {
                    if (getListHorario.errorCode == 0) {
                        List<Horario> listaHorario = new ArrayList<>(getListHorario.lista);

                        List<BeanCombo> listaCombo = new ArrayList<>();
                        for (Horario horario : listaHorario) {
                            listaCombo.add(new BeanCombo(horario.codigoHorario, horario.descripcionHorario, horario.codigoSala, horario.nombreSala));
                        }

                        horarioAdapter = new ComboAdapter(getContext(), android.R.layout.simple_spinner_item, listaCombo);
                        spHorario.setAdapter(horarioAdapter);

                    } else {
                        Util.dialog_msg(getActivity(), getListHorario.errorMessage).show();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                hideDialog();
            }
        }
    };

    private Handler handlerGetListButaca = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                GetListButaca getListButaca = Connection.process_handler(msg, getContext(), GetListButaca.class);
                if (getListButaca != null) {
                    if (getListButaca.errorCode == 0) {
                        ///////
                        List<Butaca> listaButaca = new ArrayList<>(getListButaca.lista);

                        Map<String, List<Butaca>> mapFilaButaca = new TreeMap<>();
                        for (Butaca butaca : listaButaca) {

                            List<Butaca> listaButacaMap = mapFilaButaca.get(butaca.filaButaca);
                            if (listaButacaMap == null) {
                                listaButacaMap = new ArrayList<>();
                                listaButacaMap.add(butaca);
                                mapFilaButaca.put(butaca.filaButaca, listaButacaMap);
                            } else {
                                listaButacaMap.add(butaca);
                                mapFilaButaca.replace(butaca.filaButaca, listaButacaMap);
                            }

                        }

                        TableRow leyendaRow = new TableRow(getContext());
                        leyendaRow.setGravity(Gravity.CENTER);

                        /*************AGREGAMOS LAS BUTACAS DINAMICAMENTE**********/

                        TableLayout tlayout = (TableLayout) getView().findViewById(R.id.main_table);
                        tlayout.setGravity(Gravity.CENTER);

                        TableRow row = null;

                        Iterator it = mapFilaButaca.entrySet().iterator();
                        while (it.hasNext()) {

                            Map.Entry<String, List<Butaca>> pair = (Map.Entry) it.next();

                            row = new TableRow(getContext());

                            for (final Butaca butaca : pair.getValue()) {

                                mapButaca.put(butaca.codigoButaca, butaca);

                                final Button btn = new Button(getContext());

                                btn.setLayoutParams(new TableRow.LayoutParams(160, 160));
                                btn.setPadding(0, 0, 0, 0);
                                btn.requestLayout();
                                //btn.setBackgroundResource(R.drawable.butaca_negra);

                                if (butaca.codigoEstadoButaca == 0) {
                                    // nulo
                                    btn.setBackground(drawableGray);
                                    btn.setText("");
                                } else if (butaca.codigoEstadoButaca == 1) {
                                    // vacio
                                    btn.setBackground(drawableWhite);
                                    btn.setText(butaca.nombreButaca);
                                } else if (butaca.codigoEstadoButaca == 2) {
                                    // comprado
                                    btn.setBackground(drawableYellow);
                                    btn.setText(butaca.nombreButaca);
                                }

                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Butaca beanButacaSeleccionada = mapButaca.get(butaca.codigoButaca);

                                        if (beanButacaSeleccionada.codigoEstadoButaca == 0 || beanButacaSeleccionada.codigoEstadoButaca == 2) {
                                            return;
                                        }

                                        if (beanButacaSeleccionada.codigoEstadoButaca == 3) {

                                            btn.setBackground(drawableWhite);
                                            beanButacaSeleccionada.codigoEstadoButaca = new Long(1);
                                            mapButaca.put(beanButacaSeleccionada.codigoButaca, beanButacaSeleccionada);

                                        } else if (beanButacaSeleccionada.codigoEstadoButaca == 1) {

                                            btn.setBackground(drawableGreen);
                                            beanButacaSeleccionada.codigoEstadoButaca = new Long(3);
                                            mapButaca.put(beanButacaSeleccionada.codigoButaca, beanButacaSeleccionada);

                                        }

                                    }
                                });

                                row.addView(btn);

                            }

                            tlayout.addView(row);
                        }
                        ///////
                    } else {
                        Util.dialog_msg(getActivity(), getListButaca.errorMessage).show();
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
