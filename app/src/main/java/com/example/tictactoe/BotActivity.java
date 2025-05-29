package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class BotActivity extends AppCompatActivity {

    private final Button[] buttons = new Button[9];
    private boolean playerOne = true;
    private boolean game = true;
    private final String xText = "Teraz gra: X";
    private final String oText = "Teraz gra: O";
    private int level = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            level = extras.getInt("level");
        }

        TextView tvStatus = findViewById(R.id.tvStatus);
        KonfettiView kvKonfetti = findViewById(R.id.kvKonfetti);

        tvStatus.setText(xText);

        EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
        Party party =
                new PartyFactory(emitterConfig)
                        .angle(Angle.BOTTOM)
                        .spread(Spread.ROUND)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(0f, 15f)
                        .position(new Position.Relative(0.0, 0.0).between(new Position.Relative(1.0, 0.0)))
                        .build();

        buttons[0] = findViewById(R.id.bt1);
        buttons[1] = findViewById(R.id.bt2);
        buttons[2] = findViewById(R.id.bt3);
        buttons[3] = findViewById(R.id.bt4);
        buttons[4] = findViewById(R.id.bt5);
        buttons[5] = findViewById(R.id.bt6);
        buttons[6] = findViewById(R.id.bt7);
        buttons[7] = findViewById(R.id.bt8);
        buttons[8] = findViewById(R.id.bt9);

        for (Button field : buttons) {
            field.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (game) {
                        if (playerOne) {
                            field.setText("X");
                            tvStatus.setText(oText);
                        } else {
                            field.setText("O");
                            tvStatus.setText(xText);
                        }
                        field.setEnabled(false);
                        char winner = checkForWin(getBoard());
                        if (!(winner == '-')) {
                            String winnerText = winner + " wygrał!";
                            tvStatus.setText(winnerText);
                            game = false;
                            kvKonfetti.start(party);
                        } else if (checkForTie(getBoard())) {
                            String tieText = "Remis!";
                            tvStatus.setText(tieText);
                            game = false;
                        } else {
                            playerOne = !playerOne;
                            if(!playerOne){
                                if(level == 0){
                                    dumbBotPlays();
                                }else{
                                    smartBotPlays();
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private char[] getBoard(){
        char[] board = new char[9];
        for (int i = 0; i < buttons.length; i++) {
            if(buttons[i].isEnabled()){
                board[i] = '-';
            }else{
                board[i] = buttons[i].getText().toString().charAt(0);
            }
        }
        return board;
    }
    private char checkForWin(char[] board){
        if(board.length != 9) return '-';
        char[] players = {'X', 'O'};

        for (char player : players) {
            boolean zeroEqualsPlayer = board[0] == player;
            boolean oneEqualsPlayer = board[1] == player;
            boolean twoEqualsPlayer = board[2] == player;
            boolean threeEqualsPlayer = board[3] == player;
            boolean sizEqualsPlayer = board[6] == player;
            if (
                    zeroEqualsPlayer && board[0] == board[1] && board[1] == board[2] ||
                            threeEqualsPlayer && board[3] == board[4] && board[4] == board[5] ||
                            sizEqualsPlayer && board[6] == board[7] && board[7] == board[8] ||
                            zeroEqualsPlayer && board[0] == board[3] && board[3] == board[6] ||
                            oneEqualsPlayer && board[1] == board[4] && board[4] == board[7] ||
                            twoEqualsPlayer && board[2] == board[5] && board[5] == board[8] ||
                            zeroEqualsPlayer && board[0] == board[4] && board[4] == board[8] ||
                            twoEqualsPlayer && board[2] == board[4] && board[4] == board[6]
            ) {
                return player;
            }
        }
        return '-';
    }

    private boolean checkForTie(char[] board){
        int placed = 0;
        for (char tile : board) {
            if(tile != '-'){
                placed++;
            }
        }
        return placed == 9;
    }

    private void dumbBotPlays(){
        Integer[] availableFieldIds = new Integer[9];
        int selected = -1;
        for (int i = 0; i < buttons.length; i++) {
            if(buttons[i].isEnabled()){
                availableFieldIds[i] = i;
            }else{
                availableFieldIds[i] = -1;
            }
        }

        while(selected < 0){
            int rnd = new Random().nextInt(availableFieldIds.length);
            selected = availableFieldIds[rnd];
        }

        buttons[selected].performClick();
    }

    private void smartBotPlays(){
        int result = miniMax(getBoard(), 2);
        buttons[result].performClick();
    }

    private int miniMax(char[] node, int playerNum)
    {
        //int victor = checkWin(node);  returns 0 if game is ongoing, 1 for p1, 2 for p2, 3 for tie.
        char winner = checkForWin(node);
        int victor = 0;
        if(winner != '-'){
            victor = winner == 'X'? 1 : 2;
        } else if (checkForTie(node)) {
            victor = 3;
        }

        if(victor != 0) //game over .
            return score(victor);

        if(playerNum == 2) //AI
        {
            int bestVal = Integer.MIN_VALUE;
            int bestSpot = 0;
            for(int i = 0; i < node.length; i++)
            {
                if(node[i] != '-')
                    continue;
                node[i] = getSymbol(playerNum);
                int value = miniMax(node, 1);
                if(value > bestVal)
                {
                    bestVal = value;
                    bestSpot = i;
                }

                node[i] = '-';
            }
            return bestSpot;
        }
        else
        {
            int bestVal = Integer.MAX_VALUE;
            int bestSpot = 0;
            for(int i = 0; i < node.length; i++)
            {
                if(node[i] != '-')
                    continue;
                node[i] = getSymbol(playerNum);
                int value = miniMax(node, 2);
                if(value < bestVal)
                {
                    bestVal = value;
                    bestSpot = i;
                }
                node[i] = '-';
            }
            return bestSpot;
        }
    }

    private int score(int gameState)
    {
        if(gameState ==2) //O wins.
            return 10;
        else if(gameState==1) //X wins
            return -10;
        return 0;
    }
    
    private char getSymbol(int player){
        return player == 1 ? 'X' : 'O';
    }
}