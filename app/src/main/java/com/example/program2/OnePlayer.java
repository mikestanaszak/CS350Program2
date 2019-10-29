package com.example.program2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class OnePlayer extends AppCompatActivity {
    //Activity when OnePlayer is clicked
    MyImageButton[] buttons = new MyImageButton[4];
    public static final int[] ids = {R.drawable.blue, R.drawable.green, R.drawable.red, R.drawable.yellow};
    private int currentSelected = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_player);
        LinearLayout ll = findViewById(R.id.lloneP1);
        for(int i = 0; i < 2; i++){
            MyImageButton imgB = new MyImageButton(this, ids[i]);
            imgB.setImageResource(ids[i]);
            imgB.setBackgroundColor(Color.TRANSPARENT);
            imgB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyImageButton btn = (MyImageButton) view;
                    btn.setBackgroundColor(Color.BLACK);
                    for(int i = 0; i < 4; i++){
                        if(btn != buttons[i]){
                            buttons[i].setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                    currentSelected = btn.getImageID();
                }
            });
            ll.addView(imgB);
            buttons[i] = imgB;
        }
        ll = findViewById(R.id.lloneP2);
        for(int i = 2; i < 4; i++){
            MyImageButton imgB = new MyImageButton(this, ids[i]);
            imgB.setImageResource(ids[i]);
            imgB.setBackgroundColor(Color.TRANSPARENT);
            imgB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyImageButton btn = (MyImageButton) view;
                    btn.setBackgroundColor(Color.BLACK);
                    for(int i = 0; i < 4; i++){
                        if(btn != buttons[i]){
                            buttons[i].setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                    currentSelected = btn.getImageID();
                }
            });
            ll.addView(imgB);
            buttons[i] = imgB;
        }

        Button button = findViewById(R.id.btnOnePlayer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = findViewById(R.id.etPlayerNameOnePlayer);
                if(et.getText().toString() != " "){
                    if(currentSelected != -1){
                        Intent intent = new Intent(OnePlayer.this, GameActivity.class);
                        intent.putExtra(MainActivity.GAMEMODE, 1);
                        et = findViewById(R.id.etPlayerNameOnePlayer);
                        intent.putExtra(MainActivity.PLAYER1NAME, et.getText().toString());
                        intent.putExtra(MainActivity.PLAYER1ID, currentSelected);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(OnePlayer.this, "Please select a color", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(OnePlayer.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}