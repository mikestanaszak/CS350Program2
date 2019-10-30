package com.example.program2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private GameView gameView;
    private int height, width;
    private LinearLayout.LayoutParams paramv;
    private LinearLayout.LayoutParams paramh;
    private LinearLayout.LayoutParams paramOtherV, paramOtherH;
    private RelativeLayout.LayoutParams p;
    private LinearLayout lv;
    private LinearLayout lh;
    private Point size;
    private int Player1ID, Player2ID;
    private String Player1Name, Player2Name;
    private LinearLayout otherContentV, otherContentH;
    private Button quitP1, quitP2;
    private TextView tvPlayer1, tvPlayer2;
    private int turn = 0;
    DataManager db;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        db = new DataManager(this);
        Intent intent = getIntent();
        //Checking if one player
        if (intent.getIntExtra(MainActivity.GAMEMODE, 0) == 1) {
            Player1ID = intent.getIntExtra(MainActivity.PLAYER1ID, 0);
            Player1Name = intent.getStringExtra(MainActivity.PLAYER1NAME);
            Player2Name = "COMPUTER";
            //Assigning the computer a chip, doesn't matter which.
            for (int i = 0; i < 4; i++) {
                if (OnePlayer.ids[i] != Player1ID) {
                    Player2ID = OnePlayer.ids[i];
                }
            }
        }
        //Checking if two player
        else if (intent.getIntExtra(MainActivity.GAMEMODE, 0) == 2) {
            Player1ID = intent.getIntExtra(MainActivity.PLAYER1ID, 0);
            Player2ID = intent.getIntExtra(MainActivity.PLAYER2ID, 0);
            Player1Name = intent.getStringExtra(MainActivity.PLAYER1NAME);
            Player2Name = intent.getStringExtra(MainActivity.PLAYER2NAME);
        }
        super.onCreate(savedInstanceState);
        size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        //Have to add this if statement since if we start it landscape, x would represent the height rather than the width.
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            height = size.x;
            width = size.y;
        } else {
            height = size.y;
            width = size.x;
        }
        //Assigning the layouts and views... Just a bunch of attempting to beautify.
        gameView = new GameView(this, this, width, height, Player1ID, Player2ID, Player1Name, Player2Name);
        p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        gameView.setLayoutParams(p);
        lv = new LinearLayout(this);
        lh = new LinearLayout(this);
        otherContentH = new LinearLayout(this);
        otherContentV = new LinearLayout(this);
        lv.setGravity(Gravity.CENTER);
        lh.setGravity(Gravity.CENTER);
        paramv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lv.setOrientation(LinearLayout.VERTICAL);
        lh.setOrientation(LinearLayout.HORIZONTAL);
        lv.setLayoutParams(paramv);
        paramh = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramh.setLayoutDirection(LinearLayout.HORIZONTAL);
        lh.setLayoutParams(paramh);
        paramOtherV = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, size.y / 2);
        paramOtherH = new LinearLayout.LayoutParams(size.y / 2, ViewGroup.LayoutParams.MATCH_PARENT);
        otherContentV.setLayoutParams(paramOtherV);
        otherContentV.setOrientation(LinearLayout.VERTICAL);
        otherContentV.setGravity(Gravity.CENTER);
        otherContentH.setLayoutParams(paramOtherH);
        otherContentH.setOrientation(LinearLayout.HORIZONTAL);
        otherContentH.setGravity(Gravity.CENTER);
        otherContentH.setOrientation(LinearLayout.VERTICAL);
        //Sidebar stuff, including the names of the player and their quit buttons.
        tvPlayer1 = new TextView(this);
        tvPlayer1.setGravity(Gravity.CENTER);
        tvPlayer2 = new TextView(this);
        tvPlayer2.setGravity(Gravity.CENTER);
        tvPlayer1.setText(Player1Name);
        tvPlayer1.setTypeface(null, Typeface.BOLD);
        tvPlayer2.setText(Player2Name);
        quitP1 = new Button(this);
        quitP2 = new Button(this);
        quitP1.setText("Quit");
        quitP2.setText("Quit");
        quitP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity.this.ShowAlert(Player2Name);
            }
        });
        quitP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity.this.ShowAlert(Player1Name);
            }
        });
        if (intent.getIntExtra(MainActivity.GAMEMODE, 0) == 1) {
            quitP2.setEnabled(false);
        }
        tvPlayer2.setTextColor(Color.GRAY);
        //Setting the first view. all of these will have to be changed/cleared when we switch to a configs.

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            otherContentH.addView(tvPlayer1);
            otherContentH.addView(quitP1);
            otherContentH.addView(tvPlayer2);
            otherContentH.addView(quitP2);
            gameView.askForNewView(0);
            lh.addView(gameView);
            lh.addView(otherContentH);
            setContentView(lh);
        } else {
            gameView.askForNewView(1);
            lv.addView(gameView);
            otherContentV.addView(tvPlayer1);
            otherContentV.addView(quitP1);
            otherContentV.addView(tvPlayer2);
            otherContentV.addView(quitP2);
            lv.addView(otherContentV);
            setContentView(lv);
        }
    }



    public void ChangeTurn() {
        if (turn == 0) {
            tvPlayer1.setTypeface(null, Typeface.NORMAL);
            tvPlayer2.setTypeface(null, Typeface.BOLD);
            tvPlayer1.setTextColor(Color.GRAY);
            tvPlayer2.setTextColor(Color.BLACK);
            turn++;
        } else if (turn == 1) {
            tvPlayer2.setTypeface(null, Typeface.NORMAL);
            tvPlayer1.setTypeface(null, Typeface.BOLD);
            tvPlayer1.setTextColor(Color.BLACK);
            tvPlayer2.setTextColor(Color.GRAY);
            turn--;
        }
    }

    @Override
    //Making things look nice once the orientation changes to landscape/vertical
    //have to do it in a weird way since I'm opting to do everything programmatically here.
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lv.removeAllViews();
            otherContentV.removeAllViews();
            otherContentH.addView(tvPlayer1);
            otherContentH.addView(quitP1);
            otherContentH.addView(tvPlayer2);
            otherContentH.addView(quitP2);
            gameView.askForNewView(0);
            lh.addView(gameView);
            lh.addView(otherContentH);
            setContentView(lh);
        } else {
            lh.removeAllViews();
            gameView.askForNewView(1);
            otherContentH.removeAllViews();
            lv.addView(gameView);
            otherContentV.addView(tvPlayer1);
            otherContentV.addView(quitP1);
            otherContentV.addView(tvPlayer2);
            otherContentV.addView(quitP2);
            lv.addView(otherContentV);
            setContentView(lv);
        }
        super.onConfigurationChanged(newConfig);
    }


    //Alert when there is a winner, will call database manager to insert or add a win to the name and insert a loss at the other name.
    public void ShowAlert(String winner) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        highScoreInterface box = new highScoreInterface();
        Player p = db.SearchNamePlayer(Player1Name);
        String output = "";
        if (p.getName() == null) {
            p = new Player(Player1Name, 0, 0);
            db.Insert(p);
        }
        //Just a bunch of if statements for the output on the alert as well as the updating of
        //wins / losses of each player. Tie counts for a loss for both.
        if(winner.equals("TIE")){
            p.setNumOfLosses(p.getNumOfLosses() + 1);
        }
        else{
            if (p.getName().equals(winner)) {
                p.setNumOfWins(p.getNumOfWins() + 1);
            } else {
                p.setNumOfLosses(p.getNumOfLosses() + 1);
            }
        }
        db.Update(p);
        output = output + p.getName() + " Wins: " + p.getNumOfWins() + " Losses: " + p.getNumOfLosses() + "\n";
        p = db.SearchNamePlayer(Player2Name);
        if (p.getName() == null) {
            p = new Player(Player2Name, 0, 0);
            db.Insert(p);
        }
        if(winner.equals("TIE")){
            p.setNumOfLosses(p.getNumOfLosses() + 1);
        }
        else{
            if (p.getName().equals(winner)) {
                p.setNumOfWins(p.getNumOfWins() + 1);
            } else {
                p.setNumOfLosses(p.getNumOfLosses() + 1);
            }
        }
        db.Update(p);
        if(winner.equals("TIE")){
            alert.setTitle("DRAW!");
        }
        else{
            alert.setTitle(winner + " Won!");
        }
        output = output + p.getName() + " Wins: " + p.getNumOfWins() + " Losses: " + p.getNumOfLosses() + "\n";
        output += "Play again?";
        alert.setMessage(output);
        alert.setPositiveButton("Yes", box);
        alert.setNegativeButton("No", box);
        alert.show();
    }

    //ID == -1 is to not play again, else it just plays the game again with the same settings.
    private class highScoreInterface implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dlg, int ID) {
            if (ID == -1) {
                Intent newIntent = new Intent(GameActivity.this, GameActivity.class);
                Intent data = getIntent();
                newIntent.putExtras(data.getExtras());
                finish();
                startActivity(newIntent);
            } else {
                Intent newIntent = new Intent(GameActivity.this, MainActivity.class);
                finish();
                startActivity(newIntent);
            }
        }
    }
}
