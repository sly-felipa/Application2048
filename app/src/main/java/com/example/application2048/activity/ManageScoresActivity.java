package com.example.application2048.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application2048.R;
import com.example.application2048.db.DBFilters;
import com.example.application2048.db.DBSQLite;
import com.example.application2048.db.IDB;
import com.example.application2048.eventlisteners.OnAdapterLoadedEventListener;
import com.example.application2048.eventlisteners.OnScoreDeletingEventListener;
import com.example.application2048.eventlisteners.OnSharingEventListener;
import com.example.application2048.model.Score;
import com.example.application2048.viewScore.ScoreListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ManageScoresActivity extends AppCompatActivity {
    private IDB db;
    private RecyclerView mRecyclerView;
    private ScoreListAdapter mAdapter;

    // views
    private EditText textSearchView;

    /////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_scores);

        this.db = new DBSQLite(this);

        ArrayList<Score> scores = this.db.findAllScores();

        // Create recycler view.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an mAdapter and supply the data to be displayed.
        mAdapter = new ScoreListAdapter(this, scores);

        mAdapter.setOnScoreDeletingEventListener(new OnScoreDeletingEventListener() {
            @Override
            public void onScoreDelete(Score score) {
                db.deleteScore(score);
            }
        });

        mAdapter.setmOnAdapterLoadedEventListener(new OnAdapterLoadedEventListener() {
            @Override
            public void onLoaded(String message) {
                Toast.makeText(ManageScoresActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });

        mAdapter.setOnSharingEventListener(new OnSharingEventListener() {
            @Override
            public void onSharingRequest(String subject, String body) {
                Intent sharing = new Intent(Intent.ACTION_SEND);
                sharing.setType("text/plain");
                sharing.putExtra(Intent.EXTRA_SUBJECT, "Te comparto mi puntuación!");
                sharing.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(sharing,"Choose app"));
            }
        });

        // Connect the mAdapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        textSearchView = findViewById(R.id.txtSearch);

    }

    public void onClickLessThan(View v) {
        String txt = textSearchView.getText().toString();
        try {
            int value = Integer.parseInt(txt);
            this.filterPointsByValue(value, DBFilters.LessThan);
        } catch (Exception ex) {
            Log.e(ex.getLocalizedMessage(), "Error input");
            textSearchView.setText("");
            return;
        }
    }

    public void onClickGreaterThan(View view) {
        String txt = textSearchView.getText().toString();
        try {
            int value = Integer.parseInt(txt);
            this.filterPointsByValue(value, DBFilters.GreaterThan);
        } catch (Exception ex) {
            Log.e(ex.getLocalizedMessage(), "Error input");
            textSearchView.setText("");
            return;
        }
    }

    public void onClickEquals(View view) {
        String txt = textSearchView.getText().toString();
        try {
            int value = Integer.parseInt(txt);
            this.filterPointsByValue(value, DBFilters.Equals);
        } catch (Exception ex) {
            Log.e(ex.getLocalizedMessage(), "Error input");
            textSearchView.setText("");
            return;
        }
    }

    private void filterPointsByValue(int points, int filter) {
        String sign = DBFilters.filterToString(filter);
        ArrayList<Score> filteredScores = this.db.findByPoints(points, sign);
        mAdapter.setScoreList(filteredScores);

//        Comparator<Score> comparador = new Comparator<Score>() {
//            @Override
//            public int compare(Score o1, Score o2) {
//                if (o1.getPoints() < o2.getPoints()) {
//                    return -1;
//                } else if (o1.getPoints() > o2.getPoints()) {
//                    return 1;
//                } else {
//                    return 0;
//                }
//            }
//        };

    }

    public void filterPointsByOrder(View view) {
        ArrayList<Score> scores = this.db.findAllScores();
        Comparator<Score> comparator = new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                return new Integer((int)o1.getSecondsGame()).compareTo(new Integer((int)o2.getSecondsGame()));
            }
        };
        scores.sort(comparator);
        mAdapter.setScoreList(scores);
    }
}