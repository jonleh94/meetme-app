package com.example.jansenmo.meetmeapp;

import android.app.Application;

/**
 * Created by jansenmo on 12.11.2015.
 */
public class NetworkSettings extends Application {

    String ip = "192.168.0.103";

    public String getIpAddress() {
        return ip;
    }
}
