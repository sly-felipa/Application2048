package com.example.application2048.eventlisteners;

import android.view.View;

public class ButtonOnClickListener implements View.OnClickListener {

    private int scoreId;

    public ButtonOnClickListener(int id) {
        this.scoreId = id;
    }

    public void onClick(View v) {
        // Implemented in WordListAdapter
    }
}
