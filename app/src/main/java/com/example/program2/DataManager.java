package com.example.program2;

/**
 * Created by mcvebm on 9/28/2017.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DataManager {
    private SQLiteDatabase db;
    public static final String TABLE_ROW_ID = "_id";
    public static final String TABLE_ROW_NAME = "name";
    public static final String TABLE_ROW_WINS = "wins";
    public static final String TABLE_ROW_LOSSES = "losses";
    public static final String DB_NAME = "connect4_db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_PLAYERS = "name_wins_losses";


    public DataManager (Context context) {
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        db = helper.getWritableDatabase();
    }
    public void Delete(String name) {
        String query = "DELETE FROM " + TABLE_PLAYERS + " WHERE " + TABLE_ROW_NAME +
                " = '" + name + "';";
        Log.i("MYAPP",   query);
        db.execSQL(query);
    }
    public void Delete(int id){
        String query = "DELETE FROM " + TABLE_PLAYERS + " WHERE " +
                TABLE_ROW_ID + " = '" + id + "';";
        Log.i("MYAPP", query);
        db.execSQL(query);
    }
    public void Insert(String name, int year, String major) {
        String query = "INSERT INTO " + TABLE_PLAYERS + " (" +
                TABLE_ROW_NAME + ", " + TABLE_ROW_WINS + ", " + TABLE_ROW_LOSSES + ") " +
                "VALUES (" + "'" + name +  "'" + ", " +
                year  +  ", " + "'" + major +  "'" + ");";

        Log.i("MYAPP",   query);
        db.execSQL(query);
    }
    public void Update(Player p){
        String query = "UPDATE " + TABLE_PLAYERS;
        query += " SET " + TABLE_ROW_WINS + " = '" + p.getNumOfWins() + "', ";
        query += TABLE_ROW_LOSSES + " = '" + p.getNumOfLosses() + "'";
        query += " WHERE " + TABLE_ROW_ID +  " = " + getIDOfName(p.getName());
        db.execSQL(query);
     }

    public void Insert(Player a) {
        String name = a.getName();
        int wins = a.getNumOfWins();
        int losses = a.getNumOfLosses();
        String query = "INSERT INTO " + TABLE_PLAYERS + " (" +
                TABLE_ROW_NAME + ", " + TABLE_ROW_WINS + ", " + TABLE_ROW_LOSSES + ") " +
                "VALUES (" + "'" + name +  "'" + ", " +
                wins + ", " +  "'" + losses +  "'" + ");";

        Log.i("MYAPP",   query);
        db.execSQL(query);
    }
    public Cursor SelectAll()
    {
        Cursor c = db.rawQuery("SELECT *" + " from " + TABLE_PLAYERS, null);
        return c;
    }
    public Cursor SearchName(String name)
    {
        String query = "SELECT " + TABLE_ROW_ID + ", " +
                TABLE_ROW_NAME + ", " + TABLE_ROW_WINS
                + ", " + TABLE_ROW_LOSSES + " from " +
                TABLE_PLAYERS   + " WHERE " +
                TABLE_ROW_NAME + " = '" + name + "';";

        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public Player SearchNamePlayer(String name){
        String query = "SELECT " + TABLE_ROW_ID + ", " +
                TABLE_ROW_NAME + ", " + TABLE_ROW_WINS
                + ", " + TABLE_ROW_LOSSES + " from " +
                TABLE_PLAYERS   + " WHERE " +
                TABLE_ROW_NAME + " = '" + name + "';";
        Cursor c = db.rawQuery(query, null);
        Player p = new Player();
        if(c.moveToNext()){
            p.setName(c.getString(1));
            p.setNumOfWins(c.getInt(2));
            p.setNumOfLosses(c.getInt(3));
        }
        return p;
    }

    public int getIDOfName(String name){
        String query = "SELECT " + TABLE_ROW_ID + ", " +
                TABLE_ROW_NAME + ", " + TABLE_ROW_WINS
                + ", " + TABLE_ROW_LOSSES + " from " +
                TABLE_PLAYERS   + " WHERE " +
                TABLE_ROW_NAME + " = '" + name + "';";
        Cursor c = db.rawQuery(query, null);
        int index = -1;
        if(c.moveToNext()){
            index = c.getInt(0);
        }
        return index;
    }

    public ArrayList<Player> getTopFive() {
        ArrayList<Player> p = new ArrayList<Player>();
        Cursor c = db.rawQuery("SELECT *" + " from " + TABLE_PLAYERS, null);
        while(c.moveToNext()){
            if(p.size() < 5){
                Player tempp = new Player();
                tempp.setName(c.getString(1));
                tempp.setNumOfWins(c.getInt(2));
                tempp.setNumOfLosses(c.getInt(3));
                p.add(tempp);
            }
            else{
                for(int i = 0; i < p.size(); i++){
                    if(p.get(i).getNumOfWins() < c.getInt(2)){
                        Player tempp = new Player();
                        tempp.setName(c.getString(1));
                        tempp.setNumOfWins(c.getInt(2));
                        tempp.setNumOfLosses(c.getInt(3));
                        p.add(tempp);
                        Player least = p.get(0);
                        for(int j = 0; j < p.size(); i++){
                            if(p.get(j).getNumOfWins() > least.getNumOfWins()){
                                least = p.get(j);
                            }
                        }
                        p.remove(least);
                    }
                }
            }
        }
        return p;
    }


    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
        public CustomSQLiteOpenHelper(Context context){
            super(context, DB_NAME, null, DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String newTableQueryString = "create table "
                    + TABLE_PLAYERS + " ("
                    + TABLE_ROW_ID
                    + " integer primary key autoincrement not null,"
                    + TABLE_ROW_NAME
                    + " text not null,"
                    + TABLE_ROW_WINS
                    + " integer not null,"
                    + TABLE_ROW_LOSSES
                    + " integer not null);" ;
            db.execSQL(newTableQueryString);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
