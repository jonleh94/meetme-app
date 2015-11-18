package com.example.jansenmo.meetmeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by mayf on 17.11.2015.
 */

//TODO Toolbar!!!
public class ProfilActivity extends AppCompatActivity{
    TableRow teamColor;
    TableLayout friends;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);

        teamColor = (TableRow) findViewById(R.id.teamColor);
        friends = (TableLayout) findViewById(R.id.friendsTable);

        //TODO pick and set TeamColor!!!
        //teamColor.setBackgroundColor();


        for(int i = 0; i < 5; i++){
            //TODO MO!!!!! Add friends from Backend into TableLayout!!!
        }
    }
}
