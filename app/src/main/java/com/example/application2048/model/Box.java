package com.example.application2048.model;

import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.application2048.R;
import com.example.application2048.activity.Store;

public class Box {
    private int content;
    private Position position;
    private TextView view;
    private boolean mixed = false;
    private boolean moved = false;

    public Box(int content, int x, int y) {
        this.content = content;
        this.position = new Position(x, y);
    }

    public Box(int content, Position position) {
        this.content = content;
        this.position = position;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public TextView getView() {
        return view;
    }

    public void setView(TextView view) {
        this.view = view;
        if (this.content == 2) {
            this.view.setBackgroundColor(ContextCompat.getColor(Store.MainContext, R.color.color2));
        } else if (this.content == 4) {
            this.view.setBackgroundColor(ContextCompat.getColor(Store.MainContext, R.color.color4));
        } else if (this.content == 8) {
            this.view.setBackgroundColor(ContextCompat.getColor(Store.MainContext, R.color.color8));
        } else if (this.content == 16) {
            this.view.setBackgroundColor(ContextCompat.getColor(Store.MainContext, R.color.color16));
        } else if (this.content == 32) {
            this.view.setBackgroundColor(ContextCompat.getColor(Store.MainContext, R.color.color32));
        } else if (this.content == 64) {
            this.view.setBackgroundColor(ContextCompat.getColor(Store.MainContext, R.color.color64));
        } else if (this.content == 128) {
            this.view.setBackgroundColor(ContextCompat.getColor(Store.MainContext, R.color.color128));
        } else if (this.content == 256) {
            this.view.setBackgroundColor(ContextCompat.getColor(Store.MainContext, R.color.color256));
        } else if (this.content == 512) {
            this.view.setBackgroundColor(ContextCompat.getColor(Store.MainContext, R.color.color512));
        } else if (this.content == 1024) {
            this.view.setBackgroundColor(ContextCompat.getColor(Store.MainContext, R.color.color1024));
        } else if (this.content == 2048) {
            this.view.setBackgroundColor(ContextCompat.getColor(Store.MainContext, R.color.color2048));
        }
    }

    public boolean isMixed() {
        return mixed;
    }

    public void setMixed(boolean mixed) {
        this.mixed = mixed;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
}
