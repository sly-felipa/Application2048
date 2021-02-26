package com.example.application2048.activity;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.application2048.R;
import com.example.application2048.eventlisteners.GameSwipeListener;
import com.example.application2048.model.Box;
import com.example.application2048.model.Game;
import com.example.application2048.model.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game = new Game();
        startNewGame();

    }

    private void startNewGame() {
        insertFirstBoxes();
        setEvents();
    }

    private void setEvents() {
        ConstraintLayout transparence = this.findViewById(R.id.transparence);
        transparence.setOnTouchListener(new GameSwipeListener(this));

    }


    private Position directionVector(int direction) {
        Position pos;

        switch (direction) {
            case 3:
                pos = new Position(0, 1);
                break;
            case 0:
                pos = new Position(-1, 0);
                break;
            case 1:
                pos = new Position(1, 0);
                break;
            case 2:
                pos = new Position(0, -1);
                break;

            default:
                pos = new Position(0, 0);
                break;
        }

        return pos;
    }

    public ArrayList<Position> calculateMaxPosition(Position direction, Box box) {
        Position anterior = new Position(0, 0);
        Position next = new Position(box.getPosition().getX(),box.getPosition().getY());
        next = box.getPosition();

        do{
            anterior = new Position(next.getX(), next.getY());
            next = next.sum(direction);
        }while(next.getX() >= 0 && next.getX() < 4 && next.getY() >= 0 && next.getY() < 4
                && game.getTable().getBoxArray()[next.getX()][next.getY()] == null);


        ArrayList<Position> resultado = new ArrayList<>();
        resultado.add(anterior);
        resultado.add(next);

        return resultado;
    }

    public void move(int direction) {
        // 0 up 1 down 2 left 3 right
        Position directionVector = directionVector(direction);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Box box = game.getTable().getBoxArray()[i][j];
                if(box!=null){
                    ArrayList<Position> posiblePositions = calculateMaxPosition(directionVector, box);

                    Position next = posiblePositions.get(1);
                    Box nextBox = game.getTable().getBoxArray()[next.getX()][next.getY()];
                    if(nextBox != null && nextBox.getContent()==box.getContent()){
                        // podre mezclarme
                        ConstraintLayout table = this.findViewById(R.id.table);

                        box.setContent(box.getContent() * 2);
                        box.getView().setText(box.getContent() + "");
                        game.getTable().getBoxArray()[box.getPosition().getX()][box.getPosition().getY()] = box;
                        table.removeView(nextBox.getView());
                        applyMovement(box,next);
                    }
                    else{
                        Position anterior = posiblePositions.get(0);
                        game.getTable().getBoxArray()[box.getPosition().getX()][box.getPosition().getY()] = null;
                        box.setPosition(anterior);
                        game.getTable().getBoxArray()[box.getPosition().getX()][box.getPosition().getY()] = box;
                        applyMovement(box,anterior);
                    }

                    //realizar comprobaciones
                    //box.setMoved(true);
                }



            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Box box = game.getTable().getBoxArray()[i][j];
                if (box != null) {
                    box.setMoved(false);
                }

            }
        }
    }

    private void applyMovement(Box box, Position nextPosition) {
        // esta instruccion está machacando cualquier caja que haya ahi puesta previamente
        // estaría bien antes de setear la nueva caja, ver si se pueden mezclar,
        ConstraintLayout table = this.findViewById(R.id.table);

        ConstraintSet layoutView = new ConstraintSet();
        TextView view = box.getView();

        setPositionInView(layoutView, view.getId(), nextPosition);
        layoutView.applyTo(table);
    }

    public void insertFirstBoxes() {
        int numBoxes = 3;
        for (int i = 0; i < numBoxes; i++) {
            this.insertRandomBox();
        }
    }

    private void insertRandomBox() {
        ArrayList<Position> validPositionsToGenerate = game.getTable().getPositiontsValidToGenerate();

        if (validPositionsToGenerate.isEmpty()) {
            // no tengo sitio para generar nuevas cajas
        } else {
            int randomNum = ThreadLocalRandom.current().nextInt(0, (validPositionsToGenerate.size() - 1) + 1);
            Position randomPos = validPositionsToGenerate.get(randomNum);
            insertBoxModelAndView(randomPos);
        }
    }

    private void insertBoxModelAndView(Position randomPos) {
        int randomContent = game.generateRandomContent();
        Box newBox = new Box(randomContent, randomPos);
        newBox.setView(generateViewForBox(randomContent));

        game.getTable().insertBox(newBox);

        ConstraintSet layoutView = new ConstraintSet();
        setWidthHeightAndRatio(layoutView, newBox.getView().getId());
        setPositionInView(layoutView, newBox.getView().getId(), randomPos);

        ConstraintLayout table = this.findViewById(R.id.table);
        layoutView.applyTo(table);

    }

    private void setPositionInView(ConstraintSet layoutView, int id, Position randomPos) {
        String idImageView = "box" + randomPos.getX() + "" + randomPos.getY();
        int idImgView = getResources().getIdentifier(idImageView, "id", getPackageName());
        layoutView.connect(id, ConstraintSet.RIGHT, idImgView, ConstraintSet.RIGHT, 0);
        layoutView.connect(id, ConstraintSet.LEFT, idImgView, ConstraintSet.LEFT, 0);
        layoutView.connect(id, ConstraintSet.TOP, idImgView, ConstraintSet.TOP, 0);
        layoutView.connect(id, ConstraintSet.BOTTOM, idImgView, ConstraintSet.BOTTOM, 0);

    }

    private void setWidthHeightAndRatio(ConstraintSet layoutView, int id) {
        layoutView.constrainHeight(id, 0);
        layoutView.constrainWidth(id, 0);
        layoutView.setDimensionRatio(id, "1:1");
    }

    private TextView generateViewForBox(int content) {
        TextView view = (TextView) this.getLayoutInflater().inflate(R.layout.box, null);
        view.setId(View.generateViewId());
        view.setText(content + "");
        ConstraintLayout table = this.findViewById(R.id.table);

        table.addView(view);

        return view;
    }


   /* @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, shadowBuilder, v, 0);
            v.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }*/

}

