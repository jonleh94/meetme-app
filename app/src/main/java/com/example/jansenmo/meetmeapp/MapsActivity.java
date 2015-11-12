package com.example.jansenmo.meetmeapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationTracker {

    private GoogleMap mMap;
    private DrawerLayout drawer;
    double latitude; // latitude
    double longitude; // longitude
    ProviderLocationTracker gps;
    Context context = this;
    String usern;
    String meetC;
    EditText forUsername;
    EditText forCode;
    String ownCode;
    Dialog dialog;


    String ip;
    String port = "8087";
    String postMeetMe = "meetmeserver/api/meetme";
    String getMeetMe = "meetmeserver/api/meetme";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ip = ((NetworkSettings) this.getApplication()).getIpAddress();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Create floating button for MeetMe Process
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // meetme process dialog
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.meetme_prompt);
                dialog.setTitle("Add User");
                //get own meetMe Code
                getOwnCode();


                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        forUsername = (EditText) dialog.findViewById(R.id.usernameMeet);
                        forCode = (EditText) dialog.findViewById(R.id.codeMeet);
                        usern = forUsername.getText().toString();
                        meetC = forCode.getText().toString();
                        // send meetMe data to server
                        processMeetMe();
                        // close dialog
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        // Create navigation drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Find our drawer view
        NavigationView vDrawer = (NavigationView) findViewById(R.id.nav_view);
        // Setup drawer view
        setupDrawerContent(vDrawer);

        // get own location
        ownLocation();


    }

    public void getOwnCode() {
        context = this;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String username = prefs.getString("username", null);


        if (true) {
            new AsyncTask<String, String, String>() {


                @Override
                protected String doInBackground(String... uri) {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpResponse response;
                    String responseString = null;
                    try {
                        response = httpclient.execute(new HttpGet("http://" + ip + ":" + port + "/" + getMeetMe + "/" + username));
                        StatusLine statusLine = response.getStatusLine();
                        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            response.getEntity().writeTo(out);
                            responseString = out.toString();
                            out.close();
                        } else {
                            //Closes the connection.
                            response.getEntity().getContent().close();
                            throw new IOException(statusLine.getReasonPhrase());
                        }
                    } catch (ClientProtocolException e) {
                        //TODO Handle problems..
                    } catch (IOException e) {
                        //TODO Handle problems..
                    }
                    return responseString;
                }

                @Override
                protected void onPostExecute(String responseString) {
                    ownCode = responseString;
                    TextView text = (TextView) dialog.findViewById(R.id.ownCode);
                    text.setText("My MeetMe Code: " + ownCode);
                }
            }.execute();
        }
    }

    public void processMeetMe() {
        context = this;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String username = prefs.getString("username", null);


        if (true) {
            new AsyncTask<String, String, String>() {


                @Override
                protected String doInBackground(String... params) {
                    byte[] result = null;

                    String str = null;
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost("http://" + ip + ":" + port + "/" + postMeetMe + "/" + username + "/" + meetC + "/" + usern);// in this case, params[0] is URL
                    try {

                        HttpResponse response = client.execute(post);
                        StatusLine statusLine = response.getStatusLine();
                        if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
                            result = EntityUtils.toByteArray(response.getEntity());
                            str = new String(result, "UTF-8");
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                    }

                    return str;
                }

                @Override
                protected void onPostExecute(String responseString) {
                    Toast toast = new Toast(getApplicationContext());
                    //TODO check returned string +username?
                    //if(responseString.equals("Operation successful, updated SCORE and RANK for User")){
                    toast.makeText(MapsActivity.this, "One Point For You!", toast.LENGTH_SHORT).show();
                    /*} else {
                        toast.makeText(MapsActivity.this, responseString, toast.LENGTH_SHORT).show();
                    }*/

                }
            }.execute();
        }
    }

    public void ownLocation() {
        // check if network is available
        // if not use gps
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            gps = new ProviderLocationTracker(this, ProviderLocationTracker.ProviderType.NETWORK);
        } else {
            gps = new ProviderLocationTracker(this, ProviderLocationTracker.ProviderType.GPS);
        }

        LocationUpdateListener listener = new LocationUpdateListener() {
            @Override
            public void onUpdate(Location oldLoc, long oldTime, Location newLoc,
                                 long newTime) {

                // get userdata from login
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String username = prefs.getString("username", null);
                String password = prefs.getString("password", null);


                longitude = newLoc.getLongitude();
                latitude = newLoc.getLatitude();
                LatLng myPosition = new LatLng(latitude, longitude);
                Marker myposition = mMap.addMarker(new MarkerOptions().position(myPosition).title("Location for: " + username));


                // Send position to database

                String ipu =  ip;
                String port = "8087";

                String lat = Double.toString(latitude);
                String lng = Double.toString(longitude);
                SendOwnLocation sendOwnLocation = new SendOwnLocation();
                sendOwnLocation.execute("http://" + ipu + ":" + port + "/meetmeserver/api/geo/" + username + "/" + password + "/" + lat + "/" + lng);


                // Set focus on myPosition
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 18));
            }
        };
        gps.start(listener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {


        switch (menuItem.getItemId()) {
            case R.id.nav_map:
                Intent mapsActivity = new Intent(this, MapsActivity.class);
                startActivity(mapsActivity);
                break;
            case R.id.nav_ranking:
                Intent rankingActivity = new Intent(getApplicationContext(), RankingActivity.class);
                startActivity(rankingActivity);
                break;
            case R.id.nav_help:
                Intent helpActivity = new Intent(getApplicationContext(), helpActivity.class);
                startActivity(helpActivity);
                break;
            case R.id.nav_logout:
                Intent logoutActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logoutActivity);
                break;
            default:
                Intent defaultActivity = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(defaultActivity);
        }


        LayoutInflater inflater = getLayoutInflater();
        LinearLayout container = (LinearLayout) findViewById(R.id.content_frame);
        inflater.inflate(R.layout.activity_main, container);

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawer.closeDrawers();
    }


    @Override
    public void start() {

    }

    @Override
    public void start(LocationUpdateListener update) {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean hasLocation() {
        return false;
    }

    @Override
    public boolean hasPossiblyStaleLocation() {
        return false;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public Location getPossiblyStaleLocation() {
        return null;
    }
}
