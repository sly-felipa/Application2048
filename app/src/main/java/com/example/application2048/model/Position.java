package com.example.application2048.model;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Suma a la posición actual del box la posición de dirección(rigth, left, down, up).
     * @param vector
     * @return
     */
    public Position sum(Position vector){
        return new Position(this.x+vector.getX(), this.y + vector.getY());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
