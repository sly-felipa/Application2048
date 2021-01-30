package com.example.application2048.model;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private Box[][] boxArray = new Box[4][4];

    public Table() {
        for (int i = 0;i<4;i++){
            for(int j=0;j<4;j++){
                boxArray[i][j] = null;
            }
        }
    }

    private boolean isPositionValidToGenerate(Position pos){
        return boxArray[pos.getX()][pos.getY()] == null;
    }

    public ArrayList<Position> getPositiontsValidToGenerate(){
        ArrayList<Position> validPositions = new ArrayList<>();
        for (int i = 0;i<4;i++){
            for(int j=0;j<4;j++){
                Position currentPos  = new Position(i,j);
                if(isPositionValidToGenerate(currentPos)){
                    validPositions.add(currentPos);
                }
            }
        }
        return validPositions;
    }

    public void insertBox(Box box){
        int i = box.getPosition().getX();
        int j = box.getPosition().getY();
        boxArray[i][j] = box;
    }


}
