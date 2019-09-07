package com.pe.cinefilos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.StrictMode;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.pe.cinefilos.fragment.DetallePeliculaFragment;
import com.pe.cinefilos.fragment.ListarPeliculasFragment;
import com.pe.cinefilos.fragment.MisDatosFragment;
import com.pe.cinefilos.fragment.TriviaFragment;
import com.pe.cinefilos.fragment.TriviaResultadoFragment;
import com.pe.cinefilos.fragment.UbicacionCinesFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;

import java.util.List;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListarPeliculasFragment.OnFragmentInteractionListener,
        MisDatosFragment.OnFragmentInteractionListener,
        TriviaFragment.OnFragmentInteractionListener,
        UbicacionCinesFragment.OnFragmentInteractionListener,
        DetallePeliculaFragment.OnFragmentInteractionListener,
        TriviaResultadoFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, new MisDatosFragment()).commit();

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            return;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("entro onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_mis_datos) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new MisDatosFragment()).commit();
        } else if (id == R.id.nav_lista_peliculas) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new ListarPeliculasFragment()).commit();
        } else if (id == R.id.nav_trivia) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new TriviaFragment()).commit();
        } else if (id == R.id.nav_ubicacion_cines) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new UbicacionCinesFragment()).commit();
        } else if (id == R.id.nav_share) {
            String message = "Prueba Cinefilos, es ideal para mantenerte al día en las películas de estreno y ganar premios. Descarga gratuita en https://cineperu.herokuapp.com";
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);

            startActivity(Intent.createChooser(share, "Compartir Cinefilos"));
        } else if (id == R.id.nav_cerrar_sesion) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
