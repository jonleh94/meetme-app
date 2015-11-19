package com.example.jansenmo.meetmeapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

/**
 * Created by jansenmo on 17.11.2015.
 */
public class LogoutProcess {
    String port = "8087";
    String ip;
    String logoutUrl = "meetmeserver/api/user/logout/";
    Context context;
    public void logoutProcess(Context context) {
        this.context = context;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final String username = prefs.getString("username", null);

        ip = "192.168.1.78";

        new AsyncTask<String, String, String>() {


            @Override
            protected String doInBackground(String... params) {
                byte[] result = null;

                String str = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://" + ip + ":" + port + "/" + logoutUrl + username);
                try {

                    HttpResponse response = client.execute(post);
                    StatusLine statusLine = response.getStatusLine();
                    if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
                        result = EntityUtils.toByteArray(response.getEntity());
                        str = new String(result, "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                }

                return str;
            }

            @Override
            protected void onPostExecute(String responseString) {


            }
        }.execute();
    }
}
