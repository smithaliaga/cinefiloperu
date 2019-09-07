package com.pe.cinefilos.object.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.pe.cinefilos.PrincipalActivity;
import com.pe.cinefilos.R;
import com.pe.cinefilos.fragment.DetallePeliculaFragment;
import com.pe.cinefilos.object.entities.Movie;
import com.pe.cinefilos.util.ImageDownloaderTask;

import java.util.ArrayList;

public class PeliculaAdapter extends ArrayAdapter<Movie> {

    private static class ViewHolder {
        View view;
        TextView tvNombre, tvDescripcion;
        ImageView ivImagen;
    }

    public PeliculaAdapter(Context context, ArrayList<Movie> list) {
        super(context, R.layout.pelicula_fila, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Movie pelicula = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.pelicula_fila, parent, false);
            viewHolder.tvNombre = (TextView) convertView.findViewById(R.id.tvNombre);
            viewHolder.tvDescripcion = (TextView) convertView.findViewById(R.id.tvDescripcion);
            viewHolder.ivImagen = (ImageView) convertView.findViewById(R.id.ivImagen) ;
            viewHolder.view = convertView;
            //viewHolder.home = (TextView) convertView.findViewById(R.id.tvHome);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.tvNombre.setText(pelicula.nombre);
        viewHolder.tvDescripcion.setText(pelicula.descripcion);
        new ImageDownloaderTask(viewHolder.ivImagen).execute(pelicula.image);

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = ((PrincipalActivity)getContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contenedor, new DetallePeliculaFragment(pelicula)).addToBackStack("my_fragment").commit();
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}