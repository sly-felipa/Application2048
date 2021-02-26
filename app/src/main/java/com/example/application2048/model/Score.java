package com.example.application2048.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Score {
    private long id;
    private int points;
    private int date;
    private double secondsGame;

//    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//    long secondsSinceEpoch = calendar.getTimeInMillis() / 1000L;


    public Score() {
    }

    public Score(long id, int points, int date, double secondsGame) {
        this.id = id;
        this.points = points;
        this.date = date;
        this.secondsGame = secondsGame;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public double getSecondsGame() {
        return secondsGame;
    }

    public void setSecondsGame(double secondsGame) {
        this.secondsGame = secondsGame;
    }

    public Date getCalendarDate(){
        Date date = new Date((long)(this.date*1000));
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return id>0 && id == score.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
