package com.example.program2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TwoPlayer extends AppCompatActivity {
    //Activity for when TwoPlayers is clicked.
    MyImageButton[] buttons = new MyImageButton[4];
    public static final int[] ids = {R.drawable.blue, R.drawable.green, R.drawable.red, R.drawable.yellow};
    private int currentSelected = -1;
    private Intent intent;
    private final String COUNTER = "pkg_counter";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);
        LinearLayout ll = findViewById(R.id.lltwoP1);
        intent = getIntent();
        for(int i = 0; i < 2; i++){
            MyImageButton imgB = new MyImageButton(this, ids[i]);
            imgB.setImageResource(ids[i]);
            imgB.setBackgroundColor(Color.TRANSPARENT);
            if(imgB.getImageID() != intent.getIntExtra(MainActivity.PLAYER1ID, 0)){
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
            }
            else{
                imgB.setEnabled(false);
            }
            ll.addView(imgB);
            buttons[i] = imgB;
        }
        ll = findViewById(R.id.lltwoP2);
        for(int i = 2; i < 4; i++){
            MyImageButton imgB = new MyImageButton(this, ids[i]);
            imgB.setImageResource(ids[i]);
            imgB.setBackgroundColor(Color.TRANSPARENT);
            if(imgB.getImageID() != intent.getIntExtra(MainActivity.PLAYER1ID, 0)) {
                imgB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyImageButton btn = (MyImageButton) view;
                        btn.setBackgroundColor(Color.BLACK);
                        for (int i = 0; i < 4; i++) {
                            if (btn != buttons[i]) {
                                buttons[i].setBackgroundColor(Color.TRANSPARENT);
                            }
                        }
                        currentSelected = btn.getImageID();
                    }
                });
            }
            else{
                imgB.setEnabled(false);
            }
            ll.addView(imgB);
            buttons[i] = imgB;
        }

        if(intent.getIntExtra(COUNTER, 0) == 0){
            TextView tv = findViewById(R.id.tvPlayerTitle);
            tv.setText("Player 1");
        }
        else{
            TextView tv = findViewById(R.id.tvPlayerTitle);
            tv.setText("Player 2");
        }

        Button button = findViewById(R.id.btnTwoPlayer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = findViewById(R.id.etNameTwoPlayer);
                if(et.getText().toString() != " ") {
                    if(currentSelected != -1){
                        if(intent.getIntExtra(COUNTER, 0) == 0){
                            Intent in = new Intent(TwoPlayer.this, TwoPlayer.class);
                            in.putExtra(MainActivity.PLAYER1ID, currentSelected);
                            et = findViewById(R.id.etNameTwoPlayer);
                            in.putExtra(MainActivity.PLAYER1NAME, et.getText().toString());
                            in.putExtra(COUNTER, 1);
                            startActivity(in);
                            finish();
                        }
                        else{
                            Intent in = new Intent(TwoPlayer.this, GameActivity.class);
                            in.putExtra(MainActivity.GAMEMODE, 2);
                            in.putExtra(MainActivity.PLAYER1ID, intent.getIntExtra(MainActivity.PLAYER1ID, 0));
                            in.putExtra(MainActivity.PLAYER1NAME, intent.getStringExtra(MainActivity.PLAYER1NAME));
                            in.putExtra(MainActivity.PLAYER2ID, currentSelected);
                            et = findViewById(R.id.etNameTwoPlayer);
                            in.putExtra(MainActivity.PLAYER2NAME, et.getText().toString());
                            startActivity(in);
                            finish();
                        }
                    }
                    else{
                        Toast.makeText(TwoPlayer.this, "Please select a color", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(TwoPlayer.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
