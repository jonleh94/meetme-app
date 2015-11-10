package com.example.jansenmo.meetmeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mayf on 27.10.2015.
 */
public class RankingActivity extends AppCompatActivity{
    private DrawerLayout drawer;

    private Context context = this;
    private static URL requestUrl;
    public String[] rank = new String[10];
    public String[] score = new String[10];
    public String[] user = new String[10];

    String ip = "192.168.0.103";
    String port = "8087";
    String scoreboardList = "meetmeserver/api/leaderboard/list";

    Button teamButton, userButton;
    TableLayout stk;

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.table);

        // implement navigation drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Find our drawer view
        NavigationView vDrawer = (NavigationView) findViewById(R.id.nav_view);
        // Setup drawer view
        setupDrawerContent(vDrawer);

        teamButton = (Button) this.findViewById(R.id.teamButton);
        teamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stk.removeAllViews();
            }
        });

        userButton = (Button) this.findViewById(R.id.userButton);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stk.removeAllViews();
                getUserScoreboard();
            }
        });




        // call AsynTask to perform network operation on separate thread
        getUserScoreboard();
    }

    public void getUserScoreboard() {
        context = this;

        if (true) {
            new AsyncTask<String, String, ArrayList<Score>>() {
                ArrayList<Score> scoreArray = null;

                @Override
                protected ArrayList<Score> doInBackground(String... params) {
                    InputStream response = null;
                    try {
                        requestUrl = new URL("http://" +
                                ip
                                + ":"
                                + port
                                + "/"
                                + scoreboardList);

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
                                    scoreArray = mUserImportierer.readJsonStream(response); //store JSON Objects from JSON Array as OtherUser Objects in userArray
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
                    return scoreArray;
                }

                // wait for asynctask to finish
                protected void onPostExecute(ArrayList<Score> scoreArray) {
                    setLeaderboard(scoreArray);
                }

            }.execute();

        }
    }


    public void setLeaderboard(ArrayList<Score> userArray) {
        for (int i = 0; i < userArray.size(); i++) {
            rank[i] = String.valueOf(i + 1);
            score[i] = String.valueOf(userArray.get(i).score);
            user[i] = userArray.get(i).username;
        }
        init(rank, user, score);
    }

    public void init(String[] rank, String[] user, String[] score) {

        int headerFontSize = 37;
        stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("Rank");
        tv0.setTextSize(headerFontSize);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setPadding(25, 0, 25, 0);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("User");
        tv1.setTextSize(headerFontSize);
        tv1.setTypeface(null, Typeface.BOLD);
        tv1.setPadding(25, 0, 25, 0);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("Score");
        tv2.setTextSize(headerFontSize);
        tv2.setTypeface(null, Typeface.BOLD);
        tv2.setPadding(25, 0, 25, 0);
        tbrow0.addView(tv2);
        tbrow0.setPadding(0, 0, 0, 0);
        stk.addView(tbrow0);

        for (int i = 0; i < user.length; i++) {

            int fontSize = 20;
            TableRow tbrow = new TableRow(this);
            tbrow.setPadding(0, 0, 0, 0);
            if (i % 2 == 0) {
                tbrow.setBackgroundColor(Color.parseColor("#90c1ec"));
            }
            TextView t1v = new TextView(this);
            t1v.setText(rank[i]);
            t1v.setTextSize(fontSize);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText(user[i]);
            t2v.setTextSize(fontSize);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText(score[i]);
            t3v.setTextSize(fontSize);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            stk.addView(tbrow);
        }
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
                Intent profileActivity = new Intent(getApplicationContext(), RankingActivity.class);
                startActivity(profileActivity);
                break;
            case R.id.nav_help:
                Intent helpActivity = new Intent(getApplicationContext(), com.example.jansenmo.meetmeapp.helpActivity.class);
                startActivity(helpActivity);
                break;
            case R.id.nav_logout:
                Intent logoutActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logoutActivity);
                break;
            default:
                Intent defaultActivity = new Intent(this, MapsActivity.class);
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
}




