package com.pe.cinefilos.object.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.pe.cinefilos.R;
import com.pe.cinefilos.object.entities.TriviaRespuesta;

import java.util.ArrayList;

public class TriviaRespuestaAdapter extends ArrayAdapter<TriviaRespuesta> {

    public int selectedPosition = -1;

    private static class ViewHolder {
        View view;
        CheckBox cbTriviaRespuesta;
    }

    public TriviaRespuestaAdapter(Context context, ArrayList<TriviaRespuesta> list) {
        super(context, R.layout.trivia_respuesta_fila, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final TriviaRespuesta triviaRespuesta = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.trivia_respuesta_fila, parent, false);
            viewHolder.cbTriviaRespuesta = (CheckBox) convertView.findViewById(R.id.cbTriviaRespuesta);
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
        viewHolder.cbTriviaRespuesta.setTag(position);
        viewHolder.cbTriviaRespuesta.setChecked(position == selectedPosition);
        viewHolder.cbTriviaRespuesta.setText(triviaRespuesta.respuesta);
        viewHolder.cbTriviaRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = (Integer)view.getTag();
                notifyDataSetChanged();
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}