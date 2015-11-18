package com.example.jansenmo.meetmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by mayf on 16.11.2015.
 */
public class TeamRankActivity extends AppCompatActivity{

    //TODO Toolbar
    Button userButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_rank_layout);

        userButton = (Button) findViewById(R.id.userButton);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userRank = new Intent(getApplicationContext(), UserRankActivity.class);
                startActivity(userRank);
            }
        });

    }
}
