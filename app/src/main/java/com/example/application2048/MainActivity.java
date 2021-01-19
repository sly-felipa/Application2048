package com.example.application2048;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
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

        for (int i = 0, c = 0, r = 0; i < 16-4; i++, c++) {
            if (c == 4) {
                c = 0;
                r++;
            }
            TextView textView = new TextView(this);
            textView.setBackgroundColor(Color.BLACK);
            textView.setTextColor(Color.WHITE);
            textView.setText(String.valueOf(i));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.MATCH_PARENT;
            param.width = GridLayout.LayoutParams.MATCH_PARENT;
            param.rightMargin = 5;
            param.topMargin = 5;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(c);
            param.rowSpec = GridLayout.spec(r);
            textView.setLayoutParams(param);
            gridLayout.addView(textView);
        }
    }

    //
//    <TextView
//    android:layout_width="match_parent"
//
//    android:layout_height="wrap_content"
//    android:layout_weight="0.5"
//    android:background="@color/colorAccent"
//    android:text="Ocupado" />
//
//    <GridLayout
//    android:id="@+id/gridLayout"
//    android:layout_width="wrap_content"
//    android:layout_height="wrap_content"
//    android:layout_gravity="center_vertical|center_horizontal"
//    android:layout_margin="10dp"
//    android:columnCount="4"
//    android:gravity="center"
//    android:orientation="vertical"
//    android:rowCount="4"
//    android:background="@color/colorYellow">
//    </GridLayout>
//
//    <TextView
//    android:layout_width="match_parent"
//    android:layout_height="wrap_content"
//    android:layout_weight="0.5"
//    android:background="@color/colorPrimaryDark"
//    android:text="Ocupado" />
}