package com.example.application2048.model;

import java.util.ArrayList;

public class Table {
    private int column = 4;
    private int row = 4;
    private Box[][] boxArray = new Box[row][column];

    /**
     * Cada posición de la tabla se inicia con el valor null.
     */
    public Table() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                boxArray[i][j] = null;
            }
        }
    }

    /**
     * Verifica si una posición de la tabla tiene un valor null o no para insertar un box.
     * @param pos
     * @return
     */
    private boolean isPositionValidToGenerate(Position pos) {
        return boxArray[pos.getX()][pos.getY()] == null;
    }

    /**
     * Genera una lista con las posiciones válidas para insertar un box.
     * @return
     */
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

    /**
     * Inserta un box a la tabla - juego.
     * @param box
     */
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
