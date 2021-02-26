package com.example.application2048.model;

import android.widget.TextView;

public class Box {
    private int content;
    private Position position;
    private TextView view;
    private boolean moved = false;

    public Box(int content, int x, int y) {
        this.content = content;
        this.position = new Position(x,y);
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
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
}
