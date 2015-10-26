package com.example.jansenmo.meetmeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;


//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.UiSettings;


/**
 * Created by kraftna on 13.10.2015.
 */
public class Map extends Activity{

    private GoogleMap googleMap;
    //double latitude = 1.926;
    //double longitude = 73.400;
    static final LatLng TutorialsPoint = new LatLng(48.7738 , 9.17);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        Intent i = getIntent();
       // String latitude = i.getStringExtra("latitude");
       // String longitude = i.getStringExtra("longitude");


        double lat = getIntent().getDoubleExtra("lat", 0);
        double lng = getIntent().getDoubleExtra("lng", 0);

      //  double lat = Double.valueOf(latitude.trim()).doubleValue();
      //  double lng = Double.valueOf(longitude.trim()).doubleValue();

        LatLng User = new LatLng(lat, lng);
        //Button zurückButton = (Button) this.findViewById(R.id.button1);

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
            }
            Marker marker = googleMap.addMarker(new MarkerOptions().position(User).title("User 1 bzw. noch ich selbst")); //später sollten hier die anderen User hin
            //.snippet("unter dem Titel der Text wie z.B schon getroffen, noch unbekannt etc.")
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
}