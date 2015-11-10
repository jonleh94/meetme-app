package com.example.jansenmo.meetmeapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by jansenmo on 10.11.2015.
 */
public class LoginActivity extends Activity implements View.OnClickListener{

    private Button layoutbutton;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //button starting transmission
        layoutbutton = (Button) this.findViewById(R.id.login_button);
        layoutbutton.setOnClickListener(this);
    }



    public void onClick(View v){
        Button button = (Button)v;
        password = (EditText) findViewById(R.id.password);
        username = (EditText) findViewById(R.id.username);

        // save login data to shared preference
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString("username", username.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.commit();

        if(v == layoutbutton){
            Intent mapsActivity = new Intent(this, MapsActivity.class);
            startActivity(mapsActivity);
        }
    }




}
