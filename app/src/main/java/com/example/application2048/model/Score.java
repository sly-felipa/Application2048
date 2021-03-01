package com.example.application2048.model;


import com.example.application2048.utils.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Score {
    private long id;
    private int points;
    private long date;
    private double secondsGame;
    private String name = "Anonymous";

    public Score() {
    }

    public Score(long id, int points, long date, double secondsGame, String name) {
        this.id = id;
        this.points = points;
        this.date = date;
        this.secondsGame = secondsGame;
        this.name = name;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getSecondsGame() {
        return secondsGame;
    }

    public void setSecondsGame(double secondsGame) {
        this.secondsGame = secondsGame;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Formatea la fecha en que se juega tipo calendario.
     *
     * @return
     */
    public String getFormattedDate() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.date);
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return formatter.format(calendar.getTime());
        } catch (Exception ex) {
            return "<unparseable>";
        }
    }

    /**
     * Formatea los segundos jugados en minutos y en segundos.
     *
     * @return
     */
    public String getFormattedSecondsGame() {
        int minutes = (int) (this.secondsGame / 60);
        int secondsLeft = (int) (this.secondsGame % 60);
        return StringUtils.padLeft(String.valueOf(minutes), 2, '0') + ":" + StringUtils.padLeft(String.valueOf(secondsLeft), 2, '0');
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return id > 0 && id == score.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
