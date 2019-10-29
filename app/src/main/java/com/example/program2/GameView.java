package com.example.program2;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

public class GameView extends RelativeLayout implements View.OnTouchListener {
    private int aheight, awidth, startX, startTouchX, currentCol;
    private ImageView board;
    private LayoutParams imgParams;
    private ImageView chips[] = new ImageView[42];
    private LayoutParams chipParams[] = new LayoutParams[42];
    private GestureDetector detector;
    private int PLAYER1ICON;
    private int PLAYER2ICON;
    private ConnectFourGame game = new ConnectFourGame();
    private int count = 0;
    private final static int PLAYER1 = 1;
    private final static int PLAYER2 = 2;
    private int turn = 1;
    private Context context;
    private Activity activity;
    private int width, height;
    private String Player1Name, Player2Name;
    private boolean firstRun = false;


    public GameView(Activity act, Context context, int width, int height, int p1, int p2, String p1n, String p2n) {
        super(context);
        activity = act;
        PLAYER1ICON = p1;
        PLAYER2ICON = p2;
        this.aheight = height;
        this.awidth = width;
        this.context = context;
        DoubleTap dt = new DoubleTap();
        detector = new GestureDetector(context, dt);
        game.InitBoard();
        board = new ImageView(context);
        imgParams = new LayoutParams(800, 600);
        board.setImageResource(R.drawable.board);
        this.width = awidth / 2 - 400;
        this.height = aheight / 3 - 300;
        imgParams.leftMargin = awidth / 2 - 400;
        imgParams.topMargin = aheight / 3 - 300;
        detector.setOnDoubleTapListener(dt);
        addView(board, imgParams);
        Player1Name = p1n;
        Player2Name = p2n;
        addChip();
    }

    //Changing settings of the view for landscape/portrait use.
    public void askForNewView(int type) {
        if (type == 0) {
            width = awidth / 2 - 400;
            height = aheight / 3 - 300;
            imgParams.leftMargin = width;
            imgParams.topMargin = height;
        } else {
            width = awidth / 2 - 400;
            height = aheight / 3 - 300;
            imgParams.leftMargin = width;
            imgParams.topMargin = height;
        }
    }

    //Returns chip's top margin
    public int getChipTopMargin() {
        return chipParams[count - 1].topMargin;
    }


    @Override
    //Moves the chip left or right only
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        detector.onTouchEvent(event);

        if (v instanceof ImageView) {
            int index = -1;
            for (int i = 0; i < chips.length; i++) {
                //Matching the view to the correct ImageView of the chip that we create
                if (chips[i] == v) {
                    index = i;
                }
            }
            if (index != -1) {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //Set the inits
                        startX = chipParams[index].leftMargin;
                        startTouchX = (int) event.getRawX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //Only allow to move in X direction
                        if (event.getRawX() >= imgParams.leftMargin - 200 && event.getRawX() <= imgParams.leftMargin + 1000)
                            chipParams[index].leftMargin = startX + (int) event.getRawX() - startTouchX;
                        v.setLayoutParams(chipParams[index]);
                        break;
                    case MotionEvent.ACTION_UP:
                        //Log.i("TEST", "ACTION_UP");
                        //Snap to a column in here
                        //Making everything relative to the screen size so on different screens it will work
                        //Setting the current col for when we want to see if it is a valid move
                        //I know this probably isn't the best way to do this, but it works
                        if (chipParams[index].leftMargin + 45 > 0 && chipParams[index].leftMargin + 45 <= width + 115) {
                            chipParams[index].leftMargin = width + 15;
                            currentCol = 0;
                        } else if (chipParams[index].leftMargin + 45 > width + 115 && chipParams[index].leftMargin + 45 <= width + 230) {
                            chipParams[index].leftMargin = width + 129;
                            currentCol = 1;
                        } else if (chipParams[index].leftMargin + 45 > width + 230 && chipParams[index].leftMargin + 45 <= width + 345) {
                            chipParams[index].leftMargin = width + 243;
                            currentCol = 2;
                        } else if (chipParams[index].leftMargin + 45 > width + 345 && chipParams[index].leftMargin + 45 <= width + 460) {
                            chipParams[index].leftMargin = width + 357;
                            currentCol = 3;
                        } else if (chipParams[index].leftMargin + 45 > width + 460 && chipParams[index].leftMargin + 45 <= width + 575) {
                            chipParams[index].leftMargin = width + 470;
                            currentCol = 4;
                        } else if (chipParams[index].leftMargin + 45 > width + 575 && chipParams[index].leftMargin + 45 <= width + 690) {
                            chipParams[index].leftMargin = width + 584;
                            currentCol = 5;
                        } else if (chipParams[index].leftMargin + 45 > width + 690 && chipParams[index].leftMargin + 45 <= width + 1000) {
                            chipParams[index].leftMargin = width + 695;
                            currentCol = 6;
                        }
//                        else{
//                            Log.i("TEST", "ERROR");
//                        }
                        chips[index].setLayoutParams(chipParams[index]);
                        break;
                }
            }
        }
        return true;
    }

    public void moveChip() {
        chipParams[count - 1].topMargin = chipParams[count - 1].topMargin + 2;
        chips[count - 1].setLayoutParams(chipParams[count - 1]);
    }

    public void computerMove(){
        boolean validMove = false;
        Random r = new Random();
        while(!validMove){
            int cCol = r.nextInt(7);
            int row = game.DropChipIntoColumn(cCol, PLAYER2);
            if (row != -1) {
                chipParams[count - 1].leftMargin = width + 15 + ((800 / 7) * cCol);
                chips[count - 1].setLayoutParams(chipParams[count - 1]);
                Timer t = new Timer();
                t.schedule(new MoveChip(GameView.this, aheight, row, activity), 0, 1);
                Log.i("GAME", "Computer dropped a chip into row " + row + " and column " + cCol);
                game.DrawBoard();
                chips[count - 1].setOnTouchListener(null);
                turn--;
                validMove = true;
                CheckWinner(row, currentCol, 2);
            }
        }
    }

    //Checks the winner of the most recent move... just calls the game.GameOver function and
    // then will end the game based on where its at.
    public void CheckWinner(int row, int col, int player) {
        int check = game.GameOver(row, col, player);
        if (check == 1) {
            GameActivity p = (GameActivity) activity;
            p.ShowAlert(Player1Name);
        } else if (check == 2) {
            GameActivity p = (GameActivity) activity;
            p.ShowAlert(Player2Name);
        }
        else if(isFull()){
            GameActivity p = (GameActivity) activity;
            p.ShowAlert("TIE");
        }
    }

    //Adding chips to the board after blah is called. We bring board to front after so that it
    // looks nice.
    public void addChip() {
        chips[count] = new ImageView(context);
        chipParams[count] = new LayoutParams(90, 90);
        chips[count].setLayoutParams(chipParams[count]);
        switch (turn) {
            case 1:
                chips[count].setImageResource(PLAYER1ICON);
                break;
            case 2:
                chips[count].setImageResource(PLAYER2ICON);
                break;
        }
        chips[count].setOnTouchListener(this);
        chipParams[count].leftMargin = width + 357;
        chipParams[count].topMargin = height - 90;
        addView(chips[count]);
        currentCol = 0;
        count++;
        board.bringToFront();
        if (firstRun) {
            GameActivity act = (GameActivity) activity;
            act.ChangeTurn();
        } else {
            firstRun = true;
        }
        if(turn == 2 && Player2Name.equals("COMPUTER")){
            computerMove();
        }
    }

    private class DoubleTap implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            //Dropping the chip into the column to the row
            if (turn == PLAYER1) {
                int row = game.DropChipIntoColumn(currentCol, PLAYER1);
                if (row != -1) {
                    Timer t = new Timer();
                    t.schedule(new MoveChip(GameView.this, aheight, row, activity), 0, 1);
                    Log.i("GAME", "Player one dropped a chip into row " + row + " and column " + currentCol);
                    game.DrawBoard();
                    chips[count - 1].setOnTouchListener(null);
                    turn++;
                    CheckWinner(row, currentCol, 1);
                }
            } else {
                int row = game.DropChipIntoColumn(currentCol, PLAYER2);
                if (row != -1) {
                    Timer t = new Timer();
                    t.schedule(new MoveChip(GameView.this, aheight, row, activity), 0, 1);
                    Log.i("GAME", "Player two dropped a chip into row " + row + " and column " + currentCol);
                    game.DrawBoard();
                    chips[count - 1].setOnTouchListener(null);
                    turn--;
                    CheckWinner(row, currentCol, 2);
                }
            }
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            return false;
        }
    }

    public boolean isFull() {
        return game.isBoardFull();
    }
}
