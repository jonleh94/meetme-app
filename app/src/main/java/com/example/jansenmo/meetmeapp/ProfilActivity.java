package com.example.jansenmo.meetmeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mayf on 17.11.2015.
 */

//TODO Toolbar!!!
public class ProfilActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    Context context = this;
    TableRow teamColor;
    TableLayout friends;

    String ip;
    String port = "8087";

    String getFriendsList = "meetmeserver/api/friend/list";
    String getTeamColor = "meetmeserver/api/user/get/team";

    TableLayout stk;

    TableRow tbrow;
    ImageView avatar;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_activity);


        ip = ((NetworkSettings) this.getApplication()).getIpAddress();

        // Create navigation drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Find our drawer view
        NavigationView vDrawer = (NavigationView) findViewById(R.id.nav_view);
        // Setup drawer view
        setupDrawerContent(vDrawer);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String username = prefs.getString("username", null);

        TextView userTxt = (TextView) findViewById(R.id.usernameText);
        userTxt.setText(username);


        teamColor = (TableRow) findViewById(R.id.teamColor);
        avatar = (ImageView) findViewById(R.id.imageView3);
        friends = (TableLayout) findViewById(R.id.friendsTable);

        //TODO pick and set TeamColor!!!

        getTeamColor();
        //teamColor.setBackgroundColor();


        //TODO MO!!!!! Add friends from Backend into TableLayout!!!
        getFriendsList();


    }
    public void getTeamColor(){

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
                        response = httpclient.execute(new HttpGet("http://" + ip + ":" + port + "/" + getTeamColor + "/" + username));
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
                    if(responseString.equals("blue")){
                        avatar.setImageResource(R.mipmap.contacts_blue);
                    } else{
                        avatar.setImageResource(R.mipmap.contacts_red);
                    }

                }
            }.execute();

        }

    }

    public void getFriendsList() {
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
        stk = (TableLayout) findViewById(R.id.friendsTable);
        for (int i = 0; i < friendsArrayList.size(); i++) {


            int fontSize = 20;
            tbrow = new TableRow(this);
            tbrow.setPadding(0, 0, 0, 0);
            tbrow.setGravity(Gravity.CENTER);
            TextView t1v = new TextView(this);
            t1v.setText(friendsArrayList.get(i));
            t1v.setTextSize(fontSize);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            stk.addView(tbrow);

        }
    }

    /*
        -----DRAWER---BEGIN----
     */

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

    /*
        -----DRAWER---END----
     */
}
