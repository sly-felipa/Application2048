package com.example.application2048.model;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private int column = 4;
    private int row = 4;
    private Box[][] boxArray = new Box[row][column];

    public Table() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                boxArray[i][j] = null;
            }
        }
    }

    private boolean isPositionValidToGenerate(Position pos) {
        return boxArray[pos.getX()][pos.getY()] == null;
    }

    public ArrayList<Position> getPositiontsValidToGenerate() {
        ArrayList<Position> validPositions = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                Position currentPos = new Position(i, j);
                if (isPositionValidToGenerate(currentPos)) {
                    validPositions.add(currentPos);
                }
            }
        }
        return validPositions;
    }

    public void insertBox(Box box) {
        int i = box.getPosition().getX();
        int j = box.getPosition().getY();
        boxArray[i][j] = box;
    }

    public Box[][] getBoxArray() {
        return boxArray;
    }

    public void setBoxArray(Box[][] boxArray) {
        this.boxArray = boxArray;
    }
}
