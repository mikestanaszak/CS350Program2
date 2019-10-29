package com.example.program2;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.ArrayList;

//Parts taken from McVey's "TestScrollView"
public class DataViewActivity extends AppCompatActivity {
    DataManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DataManager(this);
        updateView(0, null);
    }

    //Mode: 0 - Show all
    //Mode: 1 - Show top 5
    //Mode: 2 - Search for name
    public void updateView(final int mode, String searchName) {
        LinearLayout ll = new LinearLayout(this);
        ScrollView scrollView = new ScrollView(this);
        GridLayout grid = new GridLayout(this);
        grid.setRowCount(1);
        grid.setColumnCount(4);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        Cursor c = null;
        ArrayList<Player> topFive = new ArrayList<Player>();
        switch (mode) {
            case 0:
                c = db.SelectAll();
                break;
            case 1:
                topFive = db.getTopFive();
                break;
            case 2:
                c = db.SearchName(searchName);
                break;
        }

        TextView labelNames = new TextView(this);
        TextView labelWins = new TextView(this);
        TextView labelLosses = new TextView(this);
        TextView labelDelete = new TextView(this);
        labelDelete.setText("");
        labelNames.setText("Name    ");
        labelLosses.setText("Losses");
        labelWins.setText("Wins    ");
        labelLosses.setTextSize(20);
        labelLosses.setTextColor(Color.BLACK);
        labelNames.setTextSize(20);
        labelNames.setTextColor(Color.BLACK);
        labelWins.setTextSize(20);
        labelWins.setTextColor(Color.BLACK);
        grid.addView(labelNames);
        grid.addView(labelWins);
        grid.addView(labelLosses);
        grid.addView(labelDelete);
        if(mode == 0 || mode == 2){
            while (c.moveToNext()) {
                TextView name = new TextView(this);
                TextView wins = new TextView(this);
                TextView losses = new TextView(this);
                Button deleteButton = new Button(this);
                deleteButton.setText("Delete");
                name.setTextSize(20);
                name.setTextColor(Color.BLACK);
                wins.setTextSize(20);
                wins.setTextColor(Color.BLACK);
                losses.setTextSize(20);
                losses.setTextColor(Color.BLACK);
                int idt = c.getInt(0);
                final String namet = c.getString(1);
                int winst = c.getInt(2);
                int losst = c.getInt(3);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.Delete(namet);
                        updateView(mode, null);
                    }
                });
                name.setGravity(Gravity.CENTER);
                name.setText("" + namet + " ");
                wins.setGravity(Gravity.CENTER);
                wins.setText("" + winst + " ");
                losses.setGravity(Gravity.CENTER);
                losses.setText("" + losst + " ");
                grid.addView(name);
                grid.addView(wins);
                grid.addView(losses);
                grid.addView(deleteButton);
                grid.setRowCount(grid.getRowCount() + 1);
            }
        }
        //Putting top five into the scrollView
        else{
            while(topFive.size() > 0){
                Player top = topFive.get(0);
                for(int i = 0; i < topFive.size(); i++){
                    if(top.getNumOfWins() < topFive.get(i).getNumOfWins()){
                        top = topFive.get(i);
                    }
                }
                topFive.remove(top);
                TextView name = new TextView(this);
                TextView wins = new TextView(this);
                TextView losses = new TextView(this);
                Button deleteButton = new Button(this);
                deleteButton.setText("Delete");
                name.setTextSize(20);
                name.setTextColor(Color.BLACK);
                wins.setTextSize(20);
                wins.setTextColor(Color.BLACK);
                losses.setTextSize(20);
                losses.setTextColor(Color.BLACK);
                losses.setGravity(Gravity.CENTER);
                wins.setGravity(Gravity.CENTER);
                name.setGravity(Gravity.CENTER);
                name.setText(top.getName() + "   ");
                wins.setText(top.getNumOfWins() + "");
                losses.setText(top.getNumOfLosses() + "");
                final Player finalTop = top;
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.Delete(finalTop.getName());
                        updateView(mode, null);
                    }
                });
                grid.addView(name);
                grid.addView(wins);
                grid.addView(losses);
                grid.addView(deleteButton);
                grid.setRowCount(grid.getRowCount() + 1);
            }
        }
        scrollView.addView(grid);
        Button backBtn = new Button(this);
        backBtn.setText("Back");
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DataViewActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button viewTop5 = new Button(this);
        viewTop5.setText("View Top 5");
        viewTop5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateView(1, null);
            }
        });
        Button viewAll = new Button(this);
        viewAll.setText("View All");
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateView(0, null);
            }
        });
        final EditText et = new EditText(this);
        et.setWidth(width / 2);
        et.setText("");
        Button search = new Button(this);
        search.setText("Search Name");
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateView(2, et.getText().toString());
            }
        });
        ll.addView(scrollView);
        ll.addView(viewTop5);
        ll.addView(viewAll);
        ll.addView(et);
        ll.addView(search);
        ll.addView(backBtn);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        setContentView(ll);
    }
}
