package com.example.application2048.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Game {

    //maneja

    private Table table;

    public void nextTurn(){
        ArrayList<Position> validPositionsToGenerate = table.getPositiontsValidToGenerate();
        if(validPositionsToGenerate.size()>0){
            int randomNum = ThreadLocalRandom.current().nextInt(0, (validPositionsToGenerate.size()-1) + 1);
            Position randomPos = validPositionsToGenerate.get(randomNum);
            Box newBox = new Box(generateRandomContent(),randomPos);
            table.insertBox(newBox);
        }
        else{
            // el juego se acabo
        }
    }

    private int generateRandomContent (){
        return 2;
    }

    public void moveRight() {

    }

    public void moveLeft() {
    }

    public void moveUp() {
    }

    public void moveDown() {
    }

}
