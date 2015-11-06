package com.example.jansenmo.meetmeapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mayf on 27.10.2015.
 */
public class ranking extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;


    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.table);

        String[] rank = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] score = {"user1", "user2", "user3", "user4", "user5", "user6", "user7", "user8", "user9", "user10"};
        String[] user = {"score1", "score2", "score3", "score4", "score5", "score6", "score7", "score8", "score9", "score10"};

        init(rank, score, user);
    }

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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {

        } else if (id == R.id.nav_profil) {

            Intent intent;
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);

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

    public void init(String[] rank, String[] user, String[] score) {

        int headerFontSize=37;
        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
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
        tbrow0.setPadding(0,0,0,0);
        stk.addView(tbrow0);

        for (int i = 0; i < user.length; i++) {

            int fontSize = 20;
            TableRow tbrow = new TableRow(this);
            tbrow.setPadding(0,0,0,0);
            if(i%2 == 0){
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
}




