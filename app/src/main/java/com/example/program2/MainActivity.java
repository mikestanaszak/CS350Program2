package com.example.program2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String GAMEMODE = "pkg_gamemode";
    public static final String PLAYER1ID = "pkg_player1";
    public static final String PLAYER2ID = "pkg_player2";
    public static final String PLAYER1NAME = "pkg_player1name";
    public static final String PLAYER2NAME = "pkg_player2name";
    DataManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DataManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        Button btn = findViewById(R.id.onePlayer);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OnePlayer.class);
                startActivity(intent);
                finish();
            }
        });
        btn = findViewById(R.id.twoPlayer);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TwoPlayer.class);
                startActivity(intent);
                finish();
            }
        });
        btn = findViewById(R.id.highScores);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DataViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    }
}
