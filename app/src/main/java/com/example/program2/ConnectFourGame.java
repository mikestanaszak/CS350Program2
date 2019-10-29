package com.example.program2;

import android.util.Log;

import java.util.Random;

/**
 * Created by mcvebm on 10/22/2017.
 */

//No changes were made to ConnectFourGame

public class ConnectFourGame {
    public static final String ME = "CF: ";

    public static final int COLUMNS = 7;
    public static final int ROWS = 6;
    public static final int BLACK = 1;  // player 1
    public static final int RED = 2;    // player 2
    public static final int EMPTY = 0;
    public static final int DRAW = -1;
    public static final int INVALID = -1;
    private int[][] B;

    public ConnectFourGame()
    {
        B = new int[ROWS][COLUMNS];
        InitBoard();
        DrawBoard();
    }

    public void InitBoard()
    {
        for (int k = 0; k < ROWS; k++)
            for (int m = 0; m < COLUMNS; m++)
                B[k][m] = EMPTY;
    }

    public boolean isBoardFull()
    {
        boolean isFull = true;
        for (int j = 0; j < ROWS; j++)
            if (B[0][j] == EMPTY)
                isFull = false;
        return isFull;
    }

    private boolean isMoveValid(int col)
    {
        return ((col >= 0) && (col < COLUMNS) && (B[0][col] == EMPTY));
    }

    public void DrawBoard()
    {
        String x;
        Log.i (ME,"-----------------------------");
        for (int r = 0; r < ROWS; r++)
        {
            x="r"+ r+ ": ";
            for (int c = 0; c < COLUMNS; c++)
                x +=  B[r][c]+" ";
            Log.i(ME, x);
        }
        Log.i (ME,"-----------------------------");
    }

    public int DropChipIntoColumn(int col, int color)
    {
        if (!isMoveValid(col))
        {
            Log.i(ME, "unable to place chip in column " + col);
            return INVALID;
        }

        int row = 0;
        while (row < ROWS && B[row][col] == EMPTY)
            row++;

        if (row >= 1)
        {
            B[row - 1][col] = color;
            return row - 1;
        }
        else
            return INVALID;
    }

    //	GameOver()
    //	Returns:	true / color of winner if game is over
    //	Returns:	DRAW
    //  Returns;    0/false if otherwise
    public int GameOver(int lastRow, int lastCol, int color)
    {
        // check lastRow for four in a row
        int num = 0;
        for (int c = 0; c < COLUMNS; c++)
            if (B[lastRow][c] == color)
            {
                num++;
                if (num == 4)
                    return color;
            }
            else
                num = 0;
        // check lastCol for four in a column
        num = 0;
        for (int r = 0; r < ROWS; r++)
            if (B[r][lastCol] == color)
            {
                num++;
                if (num == 4)
                    return color;
            }
            else
                num = 0;

        // check for the diagonal from upper left to lower right
        num = 0;
        int diff;
        if (lastRow <= lastCol)
        {
            diff = lastCol - lastRow;
            for (int r = 0; r < ROWS && r+diff < COLUMNS; r++)
                if (B[r][r+diff] == color)
                {
                    num++;
                    if (num == 4)
                        return color;
                }
                else
                    num = 0;
        }
        else
        {
            diff = lastRow - lastCol;
            for (int c = 0; c < COLUMNS && c+diff < ROWS; c++)
                if (B[c+diff][c] == color)
                {
                    num++;
                    if (num == 4)
                        return color;
                }
                else
                    num = 0;
        }
        // check the diagonal from lower left to upper right
        num = 0;
        int sum;

        sum = lastRow + lastCol;
        for (int r = 0; r < ROWS && sum - r < COLUMNS && sum - r >= 0; r++)
            if (B[r][sum-r] == color)
            {
                num++;
                if (num == 4)
                    return color;
            }
            else
                num = 0;


        // check for a full board
        if (isBoardFull())
            return DRAW;
        return 0;
    }
}

