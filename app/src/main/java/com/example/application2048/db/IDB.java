package com.example.application2048.db;

import com.example.application2048.model.Score;

import java.util.ArrayList;

public interface IDB {

    Score findScoreById(int id);
    ArrayList<Score> findByPoints(int points, DBFilters filter);
    ArrayList<Score> findAllScores();

    Score insertScore(Score score);
    void deleteScore(Score score);
    Score updateScore (Score score);

}
