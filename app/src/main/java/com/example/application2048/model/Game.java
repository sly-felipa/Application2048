package com.example.application2048.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Game {

    private int score;
    private Table table;


    public Game() {
        table = new Table();
    }

    public void nextTurn() {
        ArrayList<Position> validPositionsToGenerate = table.getPositiontsValidToGenerate();
        if (validPositionsToGenerate.size() > 0) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, (validPositionsToGenerate.size() - 1) + 1);
            Position randomPos = validPositionsToGenerate.get(randomNum);
            Box newBox = new Box(generateRandomContent(), randomPos);
            table.insertBox(newBox);
        } else {
            // el juego se acabo
        }
    }

    public int generateRandomContent() {
        Random random = new Random();
        int[] numbers = {2, 2, 2, 2, 2,4, 2, 2, 2, 2};
        int randomIndex  = random.nextInt(numbers.length);
        return numbers[randomIndex];

    }

    public void moveRight() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (table.getBoxArray()[i][j] != null) {
//                    if(table.getBoxArray()[i][j] == ){
//
//                    }
                }
            }
        }

    }

    public void moveLeft() {
    }

    public void moveUp() {
    }

    public void moveDown() {
    }


    public Table getTable() {
        return table;
    }
}
