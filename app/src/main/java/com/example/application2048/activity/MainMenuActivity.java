package com.example.application2048.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.application2048.R;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void quit(View v){
        this.finish();
    }

    public void newGame(View v){
        Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void manageScores(View v){
        Intent intent = new Intent(MainMenuActivity.this, ManageScoresActivity.class);
        startActivity(intent);
    }
}