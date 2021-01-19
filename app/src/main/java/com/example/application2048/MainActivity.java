package com.example.application2048;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    GridLayout gridLayout;

    String[][] touchArray = new String[4][4];
    int startMove;
    int endMove;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
/**
 textView = new TextView(this);
 textView.setText(String.valueOf(i));
 **/

        for (int i = 0, c = 0, r = 0; i < 16 ; i++, c++) {
            if (c == 4) {
                c = 0;
                r++;
            }
            //
            createTextView(i,r,c);

        }
    }

    public void createTextView(int i, int r, int c) {
        TextView textView = new TextView(this);
        textView.setBackgroundColor(Color.BLACK);
        textView.setTextColor(Color.WHITE);
        textView.setText(String.valueOf(i));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80);
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        param.height = GridLayout.LayoutParams.WRAP_CONTENT;
        param.width = GridLayout.LayoutParams.WRAP_CONTENT;
        param.rightMargin = 5;
        param.topMargin = 5;
        param.setGravity(Gravity.CENTER);
        param.columnSpec = GridLayout.spec(c, GridLayout.CENTER,1);//spec(int start, GridLayout.Alignment alignment, float weight)
        param.rowSpec = GridLayout.spec(r,GridLayout.CENTER,1);
        textView.setLayoutParams(param);
        gridLayout.addView(textView);
    }
}