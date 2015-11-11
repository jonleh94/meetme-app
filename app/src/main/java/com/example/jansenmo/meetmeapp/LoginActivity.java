package com.example.jansenmo.meetmeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
 * Created by jansenmo on 10.11.2015.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private Button layoutbutton;
    private EditText username;
    private EditText password;
    private Boolean validateCode = null;
    private Context context = this;
    String ip = "192.168.0.103";
    String port = "8087";
    String usern;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //button starting transmission
        layoutbutton = (Button) this.findViewById(R.id.login_button);
        layoutbutton.setOnClickListener(this);
    }


    public void onClick(View v) {
        password = (EditText) findViewById(R.id.password);
        username = (EditText) findViewById(R.id.username);

        usern = username.getText().toString();
        pass = password.getText().toString();
        // save login data to shared preference
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString("username", username.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.commit();


        if (v == layoutbutton) {
            // Get GetRequest to validate user input
            getCheck();

        }
    }

    public void getCheck() {
        context = this;

        if (true) {
            new AsyncTask<String, String, String>() {


                @Override
                protected String doInBackground(String... uri) {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpResponse response;
                    String responseString = null;
                    try {
                        response = httpclient.execute(new HttpGet("http://" + ip + ":" + port + "/meetmeserver/api/user/check/" + usern + "/" + pass));
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
                    processValidation(responseString);
                }
            }.execute();
        }
    }

    // Check get response
    public void processValidation(String checkString) {
        validateCode = Boolean.valueOf(checkString);
        Toast toast = new Toast(getApplicationContext());
        if (validateCode) {
            Intent mapsActivity = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(mapsActivity);
        } else if (!validateCode) {
            toast.makeText(LoginActivity.this, "Unknown User or Wrong Password", toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
