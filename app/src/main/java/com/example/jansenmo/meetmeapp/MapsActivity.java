package com.example.jansenmo.meetmeapp;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationTracker, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private DrawerLayout drawer;
    double latitude; // latitude
    double longitude; // longitude
    private Marker mymarker;
    //Marker usermarker;
    ProviderLocationTracker gps;
    Context context = this;
    String usern;
    String meetC;
    EditText forUsername;
    EditText forCode;
    String ownCode;
    Dialog dialog;
    Dialog scoreDialog;
    Circle myCircle;
    Circle circle;
    String ip;
    String port = "8087";
    String postMeetMe = "meetmeserver/api/meetme";
    String getMeetMe = "meetmeserver/api/meetme";
    String getOtherLoc = "meetmeserver/api/geo/list";
    String getFriendsList = "meetmeserver/api/friend/list";
    URL requestUrl;
    LatLng myPosition = null;
    LatLng userPosition = null;

    public int count = 0;
    public ArrayList<Double> lats = new ArrayList<>();
    public ArrayList<Double> lngs = new ArrayList<>();
    public ArrayList<String> users = new ArrayList<>();
    public ArrayList<String> team = new ArrayList<>();

    ArrayList<com.example.jansenmo.meetmeapp.Location> globalLocationArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        ip = ((NetworkSettings) this.getApplication()).getIpAddress();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ownLocation();
                } catch (Exception e) {
                    // Do nothing
                }
            }


        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(myPosition) // Sets the center of the map to
                        .zoom(18)                   // Sets the zoom
                        .bearing(0) // Sets the orientation of the camera to east
                        .tilt(30)    // Sets the tilt of the camera to 30 degrees
                        .build();    // Creates a CameraPosition from the builder


                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        cameraPosition));
                return false;
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
                    if (responseString.equals("true")) {
                        toast.makeText(MapsActivity.this, "One Point For You!", toast.LENGTH_SHORT).show();
                    } else if (responseString.equals("wrongcode")) {
                        toast.makeText(MapsActivity.this, "Wrong MeetMe-Code! Try again!", toast.LENGTH_SHORT).show();
                    } else if (responseString.equals("wrongteam")) {
                        toast.makeText(MapsActivity.this, usern + " is not in your team!", toast.LENGTH_SHORT).show();
                    } else if (responseString.equals("friends")) {
                        toast.makeText(MapsActivity.this, "You already met " + usern + "!", toast.LENGTH_SHORT).show();
                    } else if (responseString.equals("toofar")) {
                        toast.makeText(MapsActivity.this, usern + " is too far away!", toast.LENGTH_SHORT).show();
                    }

                   /* final AlertDialog.Builder alertadd = new AlertDialog.Builder(MapsActivity.this);
                    LayoutInflater factory = LayoutInflater.from(MapsActivity.this);
                    final View view = factory.inflate(R.layout.getpoint_animation, null);
                    alertadd.setView(view);

                    final AlertDialog alert = alertadd.create();
                    alert.show();


                    final Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        public void run() {
                            alert.dismiss();
                            t.cancel();
                        }
                    }, 2000);

                    // close dialog
                    dialog.dismiss();

*/
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

                // ValueAnimator vAnimator = new ValueAnimator();


                longitude = newLoc.getLongitude();
                latitude = newLoc.getLatitude();
                myPosition = new LatLng(latitude, longitude);

                if (mymarker != null) {
                    mMap.clear();
                    mymarker.remove();
                   /* if (vAnimator.isStarted() && circle == null) {
                        vAnimator.end();

                    }*/
                    circle.remove();
                }
                mymarker = mMap.addMarker(new MarkerOptions().position(myPosition).title(username).icon(BitmapDescriptorFactory.defaultMarker(90)).draggable(true));

                // add radar

                circle = mMap.addCircle(new CircleOptions()
                                .center(myPosition)
                                .radius(100)
                                .fillColor(0x55547AFA)
                                .strokeColor(Color.TRANSPARENT)
                                .strokeWidth(1)
                );
                // get location of other user
                otherLocation();

/*
                vAnimator.setRepeatCount(ValueAnimator.INFINITE);
                vAnimator.setRepeatMode(ValueAnimator.RESTART);  // PULSE
                vAnimator.setIntValues(0, 100);
                vAnimator.setDuration(1500);
                vAnimator.setEvaluator(new IntEvaluator());
                vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float animatedFraction = valueAnimator.getAnimatedFraction();
                        // Log.e("", "" + animatedFraction);
                        circle.setRadius(animatedFraction * 100);
                    }
                });
                vAnimator.start();
*/
                // Send position to database
                String ipu = ip;
                String port = "8087";

                String lat = Double.toString(latitude);
                String lng = Double.toString(longitude);
                SendOwnLocation sendOwnLocation = new SendOwnLocation();
                sendOwnLocation.execute("http://" + ipu + ":" + port + "/meetmeserver/api/geo/" + username + "/" + password + "/" + lat + "/" + lng);


                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(myPosition) // Sets the center of the map to
                        .zoom(18)                   // Sets the zoom
                        .bearing(0) // Sets the orientation of the camera to east
                        .tilt(30)    // Sets the tilt of the camera to 30 degrees
                        .build();    // Creates a CameraPosition from the builder

                // Set focus on myPosition
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        cameraPosition));
            }
        };
        gps.start(listener);
    }


    public void otherLocation() {

        context = this;


        if (true) {
            new AsyncTask<String, String, ArrayList<com.example.jansenmo.meetmeapp.Location>>() {
                ArrayList<com.example.jansenmo.meetmeapp.Location> locationArray = null;

                @Override
                protected ArrayList<com.example.jansenmo.meetmeapp.Location> doInBackground(String... params) {
                    InputStream response = null;
                    try {
                        requestUrl = new URL("http://" +
                                ip
                                + ":"
                                + port
                                + "/"
                                + getOtherLoc);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    if (requestUrl != null) {
                        HttpURLConnection urlConnection = null;
                        try {
                            urlConnection = (HttpURLConnection) requestUrl.openConnection();
                            response = urlConnection.getInputStream();
                            if (response != null) {
                                ResponseImporter mUserImportierer = new ResponseImporter(); //create new JSON Parser Object
                                try {
                                    locationArray = mUserImportierer.readJsonStream(response); //store JSON Objects from JSON Array as OtherUser Objects in userArray
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            response.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            urlConnection.disconnect();
                        } finally {
                            urlConnection.disconnect();
                        }
                    }
                    return locationArray;
                }

                // wait for asynctask to finish
                protected void onPostExecute(ArrayList<com.example.jansenmo.meetmeapp.Location> locationArray) {
                    setUserMarkers(locationArray);
                }
            }.execute();

        }


    }

    public void setUserMarkers(ArrayList<com.example.jansenmo.meetmeapp.Location> locationArray) {


        for (int i = 0; i < locationArray.size(); i++) {
            lats.add(Double.valueOf(locationArray.get(i).latitude));
            lngs.add(Double.valueOf(locationArray.get(i).longitude));
            users.add(locationArray.get(i).username);
            team.add(locationArray.get(i).team);

        }
        initMarkers(locationArray, lats, lngs, users, team);
    }

    public void initMarkers(ArrayList<com.example.jansenmo.meetmeapp.Location> locationArray, ArrayList<Double> lats, ArrayList<Double> lngs, ArrayList<String> users, ArrayList<String> team) {

        globalLocationArray = locationArray;
        for (int i = 0; i < users.size(); i++) {

            userPosition = new LatLng(lngs.get(i), lats.get(i));
            /*
            if (usermarker != null) {
                usermarker.remove();
            }
            */
            // get userdata from login
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            final String username = prefs.getString("username", null);


            if (!users.get(i).equals(username)) {

                // ---BEGIN






/*
                for (int x = 0; x < locationArray.size(); x++) {

                    for (int y = 0; y < thisList.length(); y++) {

                    }

                }

                if (team.get(i) == "blue") {
                    usermarker = mMap.addMarker(new MarkerOptions().position(userPosition).title(users.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).draggable(true));
                } else if (team.get(i) == "red") {
                    usermarker = mMap.addMarker(new MarkerOptions().position(userPosition).title(users.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).draggable(true));
                } else {
                    usermarker = mMap.addMarker(new MarkerOptions().position(userPosition).title(users.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).draggable(true));
                }
*/

                // ---END

                // Define OnClick Action
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        if (!marker.getTitle().toString().equals(username)) {


                            // meetme process dialog
                            dialog = new Dialog(context);
                            dialog.setContentView(R.layout.meetme_prompt);
                            dialog.setTitle("Add " + marker.getTitle().toString());
                            //get own meetMe Code
                            getOwnCode();


                            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //forUsername = (EditText) dialog.findViewById(R.id.usernameMeet);
                                    forCode = (EditText) dialog.findViewById(R.id.codeMeet);
                                    usern = marker.getTitle().toString();
                                    meetC = forCode.getText().toString();
                                    // send meetMe data to server
                                    processMeetMe();
                                    // close dialog
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        } else {
                            Toast toast = new Toast(getApplicationContext());
                            toast.makeText(MapsActivity.this, "My Location", toast.LENGTH_SHORT);
                        }

                        return true;
                    }
                });
            }
        }
        getFriends();
    }

    public void getFriends() {
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
                        response = httpclient.execute(new HttpGet("http://" + ip + ":" + port + "/" + getFriendsList + "/" + username));
                        StatusLine statusLine = response.getStatusLine();
                        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            response.getEntity().writeTo(out);
                            responseString = out.toString();
                            Log.i("DEBUG", responseString);
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
                    Log.i("DEBUG", "Got Friends List");
                    processFriendsList(responseString);
                }
            }.execute();

        }
    }

    public void processFriendsList(String responseString) {
        ArrayList<String> friendsArrayList = new ArrayList<>();
        responseString = responseString.replaceAll("\\[", "");
        responseString = responseString.replaceAll("\\]", "");
        while (responseString.indexOf(",") > 0) {
            String temp = responseString.substring(0, responseString.indexOf(","));
            friendsArrayList.add(temp);
            responseString = responseString.substring(responseString.indexOf(",") + 2, responseString.length());
        }
        friendsArrayList.add(responseString);

        boolean markerSet = false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String username = prefs.getString("username", null);

        for (int x = 0; x < globalLocationArray.size(); x++) {
            if (!(globalLocationArray.get(x).username).equals(username)) {

                for (int y = 0; y < friendsArrayList.size(); y++) {

                    if ((globalLocationArray.get(x).username).equals(friendsArrayList.get(y))) {
                        if ((globalLocationArray.get(x).team).equals("blue")) {
                            LatLng tempPos = new LatLng((Double.parseDouble(globalLocationArray.get(x).longitude)), (Double.parseDouble(globalLocationArray.get(x).latitude)));
                            mMap.addMarker(new MarkerOptions().position(tempPos).title(globalLocationArray.get(x).username).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).draggable(true));
                            markerSet = true;
                            break;
                        } else {
                            Log.i("DEBUG", "Set RED: "+globalLocationArray.get(x).username);
                            LatLng tempPosRed = new LatLng((Double.parseDouble(globalLocationArray.get(x).longitude)), (Double.parseDouble(globalLocationArray.get(x).latitude)));
                            mMap.addMarker(new MarkerOptions().position(tempPosRed).title(globalLocationArray.get(x).username).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).draggable(true));
                            markerSet = true;
                            break;
                        }
                    } else {
                        markerSet = false;
                    }

                }

                if (!markerSet) {
                    Log.i("DEBUG", x + "---" + globalLocationArray.get(x).username);
                    float distance;
                    LatLng tempPos = new LatLng((Double.parseDouble(globalLocationArray.get(x).longitude)), (Double.parseDouble(globalLocationArray.get(x).latitude)));

                    Location locOwn = new Location("locOwn");
                    locOwn.setLatitude((circle.getCenter().latitude));
                    locOwn.setLongitude((circle.getCenter().longitude));

                    Location locFor = new Location("LocFor");
                    locFor.setLatitude((Double.parseDouble(globalLocationArray.get(x).longitude)));
                    locFor.setLongitude((Double.parseDouble(globalLocationArray.get(x).latitude)));


                    distance = locOwn.distanceTo(locFor);

                    if (distance > circle.getRadius()) {
                        Log.i("DEBUG","SET YELLOW: "+globalLocationArray.get(x).username);
                        mMap.addMarker(new MarkerOptions().position(tempPos).title(globalLocationArray.get(x).username).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).draggable(true));
                    } else {
                        if ((globalLocationArray.get(x).team).equals("blue")) {
                            mMap.addMarker(new MarkerOptions().position(tempPos).title(globalLocationArray.get(x).username).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).draggable(true));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(tempPos).title(globalLocationArray.get(x).username).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).draggable(true));

                        }
                    }

                }
            }
        }

    }

    // method definition
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    @Override
    public boolean onMarkerClick(Marker usermarker) {

        //if (this.usermarker.equals(usermarker)) {

        // }
        return false;
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
                Intent rankingActivity = new Intent(getApplicationContext(), UserRankActivity.class);
                startActivity(rankingActivity);
                break;
            case R.id.nav_profil:
                Intent profilActivity = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(profilActivity);
                break;
            case R.id.nav_help:
                Intent helpActivity = new Intent(getApplicationContext(), helpActivity.class);
                startActivity(helpActivity);
                break;
            case R.id.nav_logout:
                LogoutProcess log = new LogoutProcess();
                log.logoutProcess(context);
                Intent logoutActivity = new Intent(getApplicationContext(), LoginActivity.class);
                logoutActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                logoutActivity.putExtra("EXIT", true);
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
