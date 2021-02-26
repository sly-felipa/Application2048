package com.example.application2048.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application2048.R;
import com.example.application2048.db.DBSQLite;
import com.example.application2048.db.IDB;
import com.example.application2048.eventlisteners.OnAdapterLoadedEventListener;
import com.example.application2048.eventlisteners.OnScoreDeletingEventListener;
import com.example.application2048.model.Score;
import com.example.application2048.viewScore.ScoreListAdapter;

import java.util.ArrayList;

public class ManageScoresActivity extends AppCompatActivity {
    private IDB db;
    private RecyclerView mRecyclerView;
    private ScoreListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_scores);

        this.db = new DBSQLite(this);

        Score score = this.db.insertScore(new Score(0, 2000, 20023123, 143));
//        Score score0 = this.db.insertScore(new Score(0, 2000, 20023123, 142));

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

        // Connect the mAdapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}