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

    /**
     * Mata la aplicación.
     * @param v
     */
    public void quit(View v){
        this.finish();
    }

    /**
     * Inicia una nueva partida.
     * @param v
     */
    public void newGame(View v){
        Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Inicia la vista de la lista de los scores.
     * @param v
     */
    public void manageScores(View v){
        Intent intent = new Intent(MainMenuActivity.this, ManageScoresActivity.class);
        startActivity(intent);
    }
}