package com.example.application2048.model;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private Table table;

    public Game() {
        table = new Table();
    }

    /**
     * Genera aleatoriamente el número que contendra un box nuevo.
     * @return
     */
    public int generateRandomContent() {
        Random random = new Random();
        int[] numbers = {2, 2, 2, 2, 2, 4, 2, 2, 2, 2};
        int randomIndex = random.nextInt(numbers.length);
        return numbers[randomIndex];
    }

    /**
     * Encuentra las cajas adyacentes de una caja.
     * @param box
     * @return
     */
    public ArrayList<Box> getAdjacentBoxes(Box box){
        ArrayList<Box> adjacentBoxes = new ArrayList<>();

        Position top = new Position(box.getPosition().getX()-1,box.getPosition().getY());
        Position bot = new Position(box.getPosition().getX()+1,box.getPosition().getY());
        Position left = new Position(box.getPosition().getX(),box.getPosition().getY()-1);
        Position right = new Position(box.getPosition().getX(),box.getPosition().getY()+1);

        if(top.getX()>=0){
            Box topBox = table.getBoxArray()[top.getX()][top.getY()];
            if(topBox!=null){
                adjacentBoxes.add(topBox);
            }
        }

        if(bot.getX()<=3){
            Box botBox = table.getBoxArray()[bot.getX()][bot.getY()];
            if(botBox!=null){
                adjacentBoxes.add(botBox);
            }
        }

        if(left.getY()>=0){
            Box leftBox = table.getBoxArray()[left.getX()][left.getY()];
            if(leftBox!=null){
                adjacentBoxes.add(leftBox);
            }
        }

        if(right.getY()<=3){
            Box rightBox = table.getBoxArray()[right.getX()][right.getY()];
            if(rightBox!=null){
                adjacentBoxes.add(rightBox);
            }
        }

        return adjacentBoxes;
    }

    public Table getTable() {
        return table;
    }
}
