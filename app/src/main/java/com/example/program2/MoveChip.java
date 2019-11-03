package com.example.program2;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.TimerTask;

public class MoveChip extends TimerTask {
    private GameView gameView;
    private ImageView iv;
    private int r;
    private int h;
    private int col;
    private int player;
    private Activity activity;
    //Have to pass activity in order to call runOnUiThread to edit the ImageViews
    public MoveChip(GameView v, int height, int row, Activity act){
        gameView = v;
        r = row;
        h = height;
        activity = act;
    }

    @Override
    public void run() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Calling function in main, just to edit easier how much it is moving by and allow it to be more accurate
                gameView.moveChip();
                //Math to check it is in the correct row, if not it keeps going.
                //Checks to see if the board is full before attempting to add another chip
                if(gameView.getChipTopMargin() > (h / 3 - 300) + ((r) * 100) + 2){
                    if(!gameView.isFull()){
                        gameView.addChip();
                        //In order to time it correctly when we add a chip, we have to send a
                        // add chip when we finish moving the previous into place.
                    }
                    cancel();
                }
                gameView.postInvalidate();
            }
        });
    }
}
