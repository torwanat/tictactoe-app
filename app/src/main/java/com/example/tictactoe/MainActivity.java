package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private boolean bot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btOffline = findViewById(R.id.btOffline);
        Button btOnline = findViewById(R.id.btOnline);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch swBot = findViewById(R.id.swBot);
        RadioButton rbEasy = findViewById(R.id.rbEasy);
        RadioButton rbHard = findViewById(R.id.rbHard);


        swBot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bot = isChecked;
                rbHard.setEnabled(isChecked);
                rbEasy.setEnabled(isChecked);
            }
        });

        btOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(!bot){
                    intent = new Intent(MainActivity.this, OfflineActivity.class);
                }else{
                    intent = new Intent(MainActivity.this, BotActivity.class);
                    intent.putExtra("level", rbEasy.isChecked()? 0 : 1);
                }
                startActivity(intent);

            }
        });

        btOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OnlineActivity.class);
                startActivity(intent);
            }
        });
    }
}