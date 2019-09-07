package com.pe.cinefilos.dialog;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DialogDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    EditText edit_text;
    String 	         str_date;
    SimpleDateFormat date_format;
    int year, month, day;
    public void setParametros(EditText p_edit_text, String p_date, Context p_context){
        edit_text   = p_edit_text;
        str_date    = p_date;
        date_format = new SimpleDateFormat("dd/MM/yyyy", Locale.US );

    }

    public DialogDatePicker(){}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState )
    {
        Calendar c = Calendar.getInstance( );

        try
        {
            Date date = date_format.parse( str_date );

            if( date == null )
            {
                throw new ParseException( "Parse result is null", 0 );
            }

            c.setTime( date );
        }
        catch ( ParseException e )
        {
            //Do nothing
        }

        year  = c.get( Calendar.YEAR );
        month = c.get( Calendar.MONTH );
        day   = c.get( Calendar.DAY_OF_MONTH );

        return new DatePickerDialog( getActivity( ), this, year, month, day );
    }

    @SuppressLint( "SimpleDateFormat" )
    public void onDateSet(DatePicker view, int year, int month, int day )
    {
        Calendar cal = Calendar.getInstance( );
        cal.set( Calendar.YEAR, year );
        cal.set( Calendar.MONTH, month );
        cal.set( Calendar.DAY_OF_MONTH, day );

        String str_date = date_format.format( cal.getTime( ) );
        edit_text.setText( str_date );
    }
}