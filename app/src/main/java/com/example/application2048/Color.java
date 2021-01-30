package com.example.application2048;

import android.widget.TextView;

public class Color {
    private int value;
    private TextView text;

    public Color(int value, TextView text) {
        this.value = value;
        this.text = text;
    }

    public void addColor() {
        if (this.value == 2) {
            text.setBackgroundColor(0xe9e9e9);
        } else if (this.value == 4) {
            text.setBackgroundColor(0xe6daab);

        } else if (this.value == 8) {
            text.setBackgroundColor(0xf79d3d);

        } else if (this.value == 16) {
            text.setBackgroundColor(0xf28007);

        } else if (this.value == 32) {
            text.setBackgroundColor(0xf55e3b);
        } else if (this.value == 64) {
            text.setBackgroundColor(0xff0000);
        } else if (this.value == 128) {
            text.setBackgroundColor(0xe9de84);
        } else if (this.value == 256) {
            text.setBackgroundColor(0xf6e873);
        } else if (this.value == 512) {
            text.setBackgroundColor(0xff5e455);
        } else if (this.value == 1024) {
            text.setBackgroundColor(0xf7e12c);
        } else if (this.value == 2048) {
            text.setBackgroundColor(0xffe400);
        }

    }
}
