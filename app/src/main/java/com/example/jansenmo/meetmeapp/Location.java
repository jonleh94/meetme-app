package com.example.jansenmo.meetmeapp;

/**
 * Created by lehj on 12.11.2015.
 */
public class Location {
    String latitude;
    String longitude;
    String team;
    String username;
    String timestamp;

    public Location(String latitude, String longitude, String team, String username, String timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.team = team;
        this.username = username;
        this.timestamp = timestamp;
    }


}
