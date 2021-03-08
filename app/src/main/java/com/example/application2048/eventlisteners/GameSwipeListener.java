package com.example.application2048.eventlisteners;

import android.view.MotionEvent;
import android.view.View;

import com.example.application2048.activity.MainActivity;

public class GameSwipeListener implements View.OnTouchListener {

    private float startX;
    private float startY;
    private boolean moving;

    private MainActivity mainActivity;

    private final double minimumDistance = 50;

    public GameSwipeListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                moving = true;
                break;

            case MotionEvent.ACTION_UP:
                moving = false;
                break;

            case MotionEvent.ACTION_MOVE:
                float posX = event.getX();
                float posY = event.getY();

                // d(p1,p2) = sqrt( (x2-x1)^2 + (y2-y1)^2)
                // p2 = (posx,posy)  p1 = (startx,starty)
                double dist = Math.sqrt((posX - startX) * (posX - startX) + (posY - startY) * (posY - startY));

                if (moving && dist >= minimumDistance) {
                    float diffX = posX - startX;
                    float diffY = posY - startY;
                    // 0 up 1 down 2 left 3 right

                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (diffX < 0) {
                            mainActivity.move(2);
                        } else {
                            mainActivity.move(3);
                        }
                    } else {
                        if (diffY < 0) {
                            mainActivity.move(0);
                        } else {
                            mainActivity.move(1);
                        }
                    }
                    moving = false;
                }
                break;
        }
        return true;
    }
}
