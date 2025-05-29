package com.example.tictactoe;

public class Lobby {
    public String board = "---------";
    public String turn = "X";
    public boolean open = true;
    public int revenge = 0; //amount of players that want to play again

    public Lobby(){
        //for firebase
    }

}
