package com.example.application2048.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
//    private int score;
    private Table table;

    public Game() {
        table = new Table();
    }

//    public void nextTurn() {
//        ArrayList<Position> validPositionsToGenerate = table.getPositiontsValidToGenerate();
//        if (validPositionsToGenerate.size() > 0) {
//            int randomNum = ThreadLocalRandom.current().nextInt(0, (validPositionsToGenerate.size() - 1) + 1);
//            Position randomPos = validPositionsToGenerate.get(randomNum);
//            Box newBox = new Box(generateRandomContent(), randomPos);
//            table.insertBox(newBox);
//        } else {
//            // el juego se acabo
//        }
//    }

    /**
     * Genera aleatoriamente el número que contendra un box nuevo.
     * @return
     */
    public int generateRandomContent() {
        Random random = new Random();
        int[] numbers = {2, 2, 2, 2, 2,4, 2, 2, 2, 2};
        int randomIndex  = random.nextInt(numbers.length);
        return numbers[randomIndex];
    }

    public Table getTable() {
        return table;
    }
}
