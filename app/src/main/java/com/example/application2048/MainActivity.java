package com.example.application2048;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private GridLayout gridLayout;
    private ArrayList<TextView> arrayListTextView = new ArrayList<>();
    private ArrayList<int[]> arrayListPosition = new ArrayList<int[]>();
    private int row = 4;
    private int col = 4;
    int[][] table = new int[row][col];
    int[][] tableCopy = new int[row][col];
    private boolean dead;
    private boolean won;
    private int x;
    private int y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createPosition();// array con c/r
//        display();
        /** muestra la devolución de un textView**/
//        returnChild();
//        arrayListTextView.get(1);
//        Toast.makeText(this, String.valueOf(arrayListTextView.get(1).getText()), Toast.LENGTH_LONG).show();
        //----------------------------------------------------


    }



    /**
     * Agrega qué y dónde se pintarán los números.
     *
     * @param i
     * @param r
     * @param c
     */
    public void showTable(int i, int r, int c) {

        TextView textView = new TextView(this);
        textView.setBackgroundColor(Color.BLACK);
        textView.setTextColor(Color.WHITE);
        textView.setText(String.valueOf(i));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 65);
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
//        param.height = GridLayout.LayoutParams.WRAP_CONTENT;
//        param.width = GridLayout.LayoutParams.WRAP_CONTENT;
//        param.rightMargin = 10;
//        param.bottomMargin = 10;
//        param.leftMargin = 10;
//        param.topMargin = 10;
//        param.setGravity(Gravity.CENTER);
        param.rowSpec = GridLayout.spec(r, GridLayout.CENTER, 1);
        param.columnSpec = GridLayout.spec(c, GridLayout.CENTER, 1);//spec(int start, GridLayout.Alignment alignment, float weight)
        textView.setLayoutParams(param);
        gridLayout.addView(textView);
    }


    /**
     * Retorna cada textView
     */
    public void returnChild() {
        int count = gridLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = gridLayout.getChildAt(i);
            arrayListTextView.add((TextView) child);
        }
    }



    /**
     * crea todas las posiciones de array
     */
    public void createPosition() {
        for (int i = 0, c = 0, r = 0; i < 16; i++, c++) {
            if (c == 4) {
                c = 0;
                r++;
            }

            int[] pos = new int[2];
            pos[0] = c;
            pos[1] = r;
            arrayListPosition.add(pos);
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, shadowBuilder, v, 0);
            v.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }

}


//La combinación del método onDragEvent(DragEvent) y View.OnDragListener es
// análoga a la combinación de onTouchEvent() y View.OnTouchListener que se usa con los eventos táctiles.

//https://developer.android.com/guide/topics/ui/drag-drop?hl=es


//        for (int i = 0, c = 0, r = 0; i < arrayListTextView.size(); i++, c++) {
//
//        Log.i("PRUEBA", String.valueOf(arrayListTextView.get(i).getText()));
//
//            showTextView(i,r,c);//r = 0
//
//
//        }

//        arrayListTextView.get(1).startDragAndDrop(this,); //https://developer.android.com/reference/android/view/View?hl=es#startDragAndDrop(android.content.ClipData,%20android.view.View.DragShadowBuilder,%20java.lang.Object,%20int)
//        arrayListTextView.get(1).startD //https://developer.android.com/reference/android/view/DragEvent?hl=es#ACTION_DROP
