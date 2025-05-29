package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
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

public class OnlineActivity extends AppCompatActivity {

    private final Button[] buttons = new Button[9];
    private boolean game = false;
    private final String xText = "Teraz gra: X";
    private final String oText = "Teraz gra: O";
    private String lobbyId = "";
    private String playerSign = "";
    private DatabaseReference dbRef;
    private Lobby currentLobby;
    private TextView tvStatus;
    private Timer botTimer;
    private Button btOneMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        tvStatus = findViewById(R.id.tvStatus);
        String waitText = "Oczekiwanie na drugiego gracza";
        tvStatus.setText(waitText);

        btOneMore = findViewById(R.id.btOneMore);

        botTimer = new Timer();
        botTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(OnlineActivity.this, BotActivity.class);
                startActivity(intent);
            }
        }, 20*1000);

        buttons[0] = findViewById(R.id.bt1);
        buttons[1] = findViewById(R.id.bt2);
        buttons[2] = findViewById(R.id.bt3);
        buttons[3] = findViewById(R.id.bt4);
        buttons[4] = findViewById(R.id.bt5);
        buttons[5] = findViewById(R.id.bt6);
        buttons[6] = findViewById(R.id.bt7);
        buttons[7] = findViewById(R.id.bt8);
        buttons[8] = findViewById(R.id.bt9);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://tictactoe-98b52-default-rtdb.europe-west1.firebasedatabase.app/");
        dbRef = database.getReference("lobbies");

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(lobbyId.equals("")) return;

                Lobby lobby = snapshot.getValue(Lobby.class);
                String key = snapshot.getKey();
                assert lobby != null;
                assert key != null;
                if(lobbyId.equals(key)){
                    currentLobby = lobby;
                    botTimer.cancel();

                    if(currentLobby.revenge == 2){
                        startOneMoreGame();
                    }else{
                        boardUpdated();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!lobbyId.equals("")) return;

                for (DataSnapshot lobbySnapshot: snapshot.getChildren()) {
                    Lobby lobby = lobbySnapshot.getValue(Lobby.class);
                    String key = lobbySnapshot.getKey();
                    assert lobby != null;
                    assert key != null;
                    if(lobby.open){
                        lobbyId = key;
                        playerSign = "O";
                        lobby.open = false;
                        dbRef.child(key).setValue(lobby);
                        break;
                    }
                }

                if(lobbyId.equals("")){
                    DatabaseReference newRef = dbRef.push();
                    lobbyId = newRef.getKey();
                    playerSign = "X";
                    newRef.setValue(new Lobby());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btOneMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btOneMore.setEnabled(false);
                currentLobby.revenge += 1;
                dbRef.child(lobbyId).setValue(currentLobby);
            }
        });

        for (int i = 0; i < buttons.length; i++) {
            Button field = buttons[i];
            int finalI = i;
            field.setOnClickListener(view -> {
                if(game) {
                    moveMade(finalI);
                }
            });
        }
    }

    private void makeKonfetti(){
        KonfettiView kvKonfetti = findViewById(R.id.kvKonfetti);
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
        kvKonfetti.start(party);

    }

    private void startOneMoreGame(){
        currentLobby.revenge = 0;
        currentLobby.board = "---------";
        currentLobby.turn = "X";
        dbRef.child(lobbyId).setValue(currentLobby);
    }

    private void boardUpdated(){
        if(currentLobby.revenge == 0){
            btOneMore.setVisibility(View.GONE);
            btOneMore.setEnabled(true);
        }

        String board = currentLobby.board;
        for(int i = 0; i < board.length(); i++){
            char placed = board.charAt(i);
            if(placed != '-'){
                buttons[i].setText(String.valueOf(placed));
                buttons[i].setEnabled(false);
            }else{
                buttons[i].setText("");
                buttons[i].setEnabled(true);
            }
        }

        String winner = checkForWin();
        if(!winner.equals("")){
            String winnerText = winner + " wygrał!";
            tvStatus.setText(winnerText);
            btOneMore.setVisibility(View.VISIBLE);
            if(playerSign.equals(winner)){
                makeKonfetti();
            }
            game = false;
            return;
        }

        if(checkForTie()){
            String tieText = "Remis!";
            tvStatus.setText(tieText);
            btOneMore.setVisibility(View.VISIBLE);
            game = false;
            return;
        }

        game = currentLobby.turn.equals(playerSign);

        String yourTurnText = "Twoja tura";
        String opponentsTurnText = "Tura przeciwnika";
        if(game){
            tvStatus.setText(yourTurnText);
        }else{
            tvStatus.setText(opponentsTurnText);
        }
    }

    private void moveMade(int fieldIndex){
        StringBuilder newBoard = new StringBuilder(currentLobby.board);
        newBoard.setCharAt(fieldIndex, playerSign.charAt(0));
        currentLobby.board = String.valueOf(newBoard);
        currentLobby.turn = playerSign.equals("X")? "O" : "X";

        dbRef.child(lobbyId).setValue(currentLobby);
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