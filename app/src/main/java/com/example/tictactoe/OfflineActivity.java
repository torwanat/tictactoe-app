package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class OfflineActivity extends AppCompatActivity {

    private final Button[] buttons = new Button[9];
    private boolean playerOne = true;
    private boolean game = true;
    private final String xText = "Teraz gra: X";
    private final String oText = "Teraz gra: O";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

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
                    if(game) {
                        if (playerOne) {
                            field.setText("X");
                            tvStatus.setText(oText);
                        } else {
                            field.setText("O");
                            tvStatus.setText(xText);
                        }
                        field.setEnabled(false);
                        String winner = checkForWin();
                        if (!winner.equals("")) {
                            String winnerText = winner + " wygrał!";
                            tvStatus.setText(winnerText);
                            game = false;
                            kvKonfetti.start(party);
                        }else if(checkForTie()){
                            String tieText = "Remis!";
                            tvStatus.setText(tieText);
                            game = false;
                        }else{
                            playerOne = !playerOne;
                        }
                    }
                }
            });
        }
    }

    private String checkForWin(){
        String[] placed = new String[9];
        String[] players = {"X", "O"};
        for (int i = 0; i < buttons.length; i++) {
            placed[i] = buttons[i].getText().toString();
        }

        for (String player : players) {
            boolean zeroEqualsPlayer = placed[0].equals(player);
            boolean oneEqualsPlayer = placed[1].equals(player);
            boolean twoEqualsPlayer = placed[2].equals(player);
            boolean threeEqualsPlayer = placed[3].equals(player);
            boolean sizEqualsPlayer = placed[6].equals(player);
            if (
                    zeroEqualsPlayer && placed[0].equals(placed[1]) && placed[1].equals(placed[2]) ||
                            threeEqualsPlayer && placed[3].equals(placed[4]) && placed[4].equals(placed[5]) ||
                            sizEqualsPlayer && placed[6].equals(placed[7]) && placed[7].equals(placed[8]) ||
                            zeroEqualsPlayer && placed[0].equals(placed[3]) && placed[3].equals(placed[6]) ||
                            oneEqualsPlayer && placed[1].equals(placed[4]) && placed[4].equals(placed[7]) ||
                            twoEqualsPlayer && placed[2].equals(placed[5]) && placed[5].equals(placed[8]) ||
                            zeroEqualsPlayer && placed[0].equals(placed[4]) && placed[4].equals(placed[8]) ||
                            twoEqualsPlayer && placed[2].equals(placed[4]) && placed[4].equals(placed[6])
            ) {
                return player;
            }
        }
        return "";
    }
    
    private boolean checkForTie(){
        int placed = 0;
        for (Button button : buttons) {
            if(!button.isEnabled()){
                placed++;
            }
        }
        return placed == 9;
    }
}