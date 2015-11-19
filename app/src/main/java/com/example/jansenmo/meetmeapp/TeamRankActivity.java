package com.example.jansenmo.meetmeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mayf on 16.11.2015.
 */
public class TeamRankActivity extends AppCompatActivity{

    private DrawerLayout drawer;
    Context context = this;


    Button userButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_rank_layout);


        // Create navigation drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Find our drawer view
        NavigationView vDrawer = (NavigationView) findViewById(R.id.nav_view);
        // Setup drawer view
        setupDrawerContent(vDrawer);

        userButton = (Button) findViewById(R.id.userButton);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userRank = new Intent(getApplicationContext(), UserRankActivity.class);
                startActivity(userRank);
            }
        });



    }

    public void setTeamScores(float blScore, float reScore){

        TextView blTxt = (TextView)findViewById(R.id.scoreTeamBlue);
        blTxt.append(String.valueOf(blScore));

        TextView reTxt = (TextView)findViewById(R.id.scoreTeamRed);
        reTxt.append(String.valueOf(reScore));

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