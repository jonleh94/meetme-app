package com.example.jansenmo.meetmeapp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by lehj on 28.10.2015.
 */
public class Score {
    String username;
    String team;
    int score;


    public Score(String username, String team, int score) {
        this.username = username;
        this.team = team;
        this.score = score;
    }
}

