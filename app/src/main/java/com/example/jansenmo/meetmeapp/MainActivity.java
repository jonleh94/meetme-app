package com.example.jansenmo.meetmeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity
        implements LocationListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String HOSTNAME = "10.0.2.2";
    private static final int PORT = 8087;
    private static final String TAG = "HelloActivity";
    private LocationManager locationManager;
    private List<Location> positionen;
    private double lat;
    private double lng;
    private boolean geodaten;
    public boolean maperstellt;
    private GoogleMap googleMap;
    private Marker marker;
    private TextView anzeigeLaenge;
    private TextView anzeigeBreite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!isProviderEnabled()) {
            warnungUndBeenden();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Erstellen der Map
        LatLng User = new LatLng(lat, lng);
        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
                GoogleMapOptions options = new GoogleMapOptions();
                options.mapType(GoogleMap.MAP_TYPE_NORMAL);
                options.compassEnabled(false);
                googleMap.setMyLocationEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(User, 15));
                if (googleMap == null) {
                    Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
                }
                maperstellt = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        positionen = new ArrayList<Location>();
    }
//auslagern ebenso warnungsscreen
    //Überprüfung ob GPS angeschaltet ist (&Internet)
    public boolean isProviderEnabled() {
        boolean gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return (gps_enabled && network_enabled);
    }

    private void warnungUndBeenden() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Resources res = getResources();
        String text = "Bitte aktivieren Sie ihr GPS";
        builder.setMessage(text);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

        if (id == R.id.nav_map) {

        } else if (id == R.id.nav_profil) {

        } else if (id == R.id.nav_friends) {

        } else if (id == R.id.nav_ranking) {
            //Open ScoreBoard
            Intent intent;
            intent = new Intent(this, ranking.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
        } else if (id == R.id.nav_info) {

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

        lng = location.getLongitude();
        lat = location.getLatitude();

        anzeigeBreite.setText(Location.convert(lat, Location.FORMAT_DEGREES));
        anzeigeLaenge.setText(Location.convert(lng, Location.FORMAT_DEGREES));

        geodaten = true;
        if (maperstellt) {
            //einlesen der daten aus jason format und speichern als
            LatLng User = new LatLng(lat, lng);
            String username = "User1";
            if (marker != null) {
                marker.remove();      //löscht immer nur einen marker
            }
            marker = googleMap.addMarker(new MarkerOptions().position(User).title(username).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).draggable(true)); //später sollten hier die anderen User hin
        }
    }

    //Wenn GPS ausgeschaltet wird soll Meldung erscheinen
    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "GPS wurde ausgeschaltet, ermitteln des Standortes nicht möglich ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    //Wenn GPS angeschaltet wird soll Meldung erscheinen
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "GPS wurde angeschaltet, der Standort kann nun ermittlet werden ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

}
