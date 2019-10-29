package com.example.program2;

public class Player {
    private int numOfWins;
    private int numOfLosses;
    private String name;

    public Player(){
        numOfWins = 0;
        numOfLosses = 0;
        name = null;
    }
    public Player(String n, int wins, int losses){
        numOfLosses = losses;
        numOfWins = wins;
        name = n;
    }

    public void setNumOfWins(int wins){
        numOfWins = wins;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumOfLosses(int numOfLosses) {
        this.numOfLosses = numOfLosses;
    }

    public int getNumOfLosses() {
        return numOfLosses;
    }

    public int getNumOfWins() {
        return numOfWins;
    }

    public String getName() {
        return name;
    }
}
