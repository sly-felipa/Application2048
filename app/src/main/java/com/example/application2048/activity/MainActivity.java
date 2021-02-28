package com.example.application2048.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.application2048.R;
import com.example.application2048.db.DBSQLite;
import com.example.application2048.db.IDB;
import com.example.application2048.eventlisteners.GameSwipeListener;
import com.example.application2048.model.Box;
import com.example.application2048.model.Game;
import com.example.application2048.model.Position;
import com.example.application2048.model.Score;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {
    private IDB db;

    private Game game;
    private int score = 0;
    private int bestScore = 0;
    private int firstBestScore = 0;
    private String username = "Anonymous";
    private Score currentScore;

    // views
    private TextView scoreNumberView;
    private TextView bestScoreNumberView;

    //Button
    private Button btnNewGame;
    ////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.db = new DBSQLite(this);

        this.scoreNumberView = findViewById(R.id.scoreNumber);
        this.bestScoreNumberView = findViewById(R.id.bestNumber);
        this.btnNewGame = findViewById(R.id.btnNewGame);

        setListeners();

        askUsername();

    }

    private void searchMaxScore(){
        Score maxScore = this.db.findMaxScore();
        if (maxScore != null) {
            this.bestScore = maxScore.getPoints();
            this.firstBestScore = this.bestScore;
        }
    }

    private void setListeners(){
        this.btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });
    }

    private void askUsername(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please, enter your username:");
        builder.setCancelable(false);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                startNewGame();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                username = input.getText().toString();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void initScore() {
        this.score = 0;
        long millis =Calendar.getInstance().getTimeInMillis();
        this.currentScore = new Score(0,0,millis,0,username);
        currentScore = this.db.insertScore(currentScore);
        updateStats();
    }


    private void startNewGame() {
        if(game!=null){
            cleanView();
        }

        game = new Game();
        searchMaxScore();
        insertFirstBoxes();
        setEvents();
        initScore();
    }

    private void setEvents() {
        ConstraintLayout transparence = this.findViewById(R.id.transparence);
        transparence.setOnTouchListener(new GameSwipeListener(this));

    }

    private Position directionVector(int direction) {
        Position pos;

        switch (direction) {
            case 0://arriba
//                pos = new Position(0, -1);
                pos = new Position(-1, 0);
                break;
            case 1://abajo
//                pos = new Position(0, 1);
                pos = new Position(1, 0);
                break;
            case 2://izq
//                pos = new Position(-1, 0);
                pos = new Position(0, -1);
                break;
            case 3://dere
//                pos = new Position(1, 0);
                pos = new Position(0, 1);
                break;
            default:
//                pos = new Position(0, 0);
                pos = new Position(0, 0);
                break;
        }

        return pos;
    }

    public ArrayList<Position> calculateMaxPosition(Position direction, Box box) {
        Position anterior = new Position(0, 0);
        Position next = new Position(box.getPosition().getX(), box.getPosition().getY());
        //next = box.getPosition();

        do {
            anterior = new Position(next.getX(), next.getY());
            next = anterior.sum(direction);
        } while ((next.getX() >= 0 && next.getX() < 4 && next.getY() >= 0 && next.getY() < 4)
                && (game.getTable().getBoxArray()[next.getX()][next.getY()] == null));


        ArrayList<Position> resultado = new ArrayList<>();
        resultado.add(anterior);
        resultado.add(next);

        return resultado;
    }

    private void getLoopIndex(Position direction, ArrayList<Integer> indexI, ArrayList<Integer> indexJ) {
        for (int i = 0; i < 4; i++) {
            indexI.add(i);
            indexJ.add(i);
        }

        //arriba para abajo
        if (direction.getX() == 1) {
            indexI.clear();
            for (int i = 3; i >= 0; i--) {
                indexI.add(i);
            }
        }

        //izquierda a derecha
        if (direction.getY() == 1) {
            indexJ.clear();
            for (int i = 3; i >= 0; i--) {
                indexJ.add(i);
            }
        }
    }

    public void move(int direction) {
        // 0 up 1 down 2 left 3 right
        Position directionVector = directionVector(direction);
        ArrayList<Integer> indexI = new ArrayList<>();
        ArrayList<Integer> indexJ = new ArrayList<>();
        getLoopIndex(directionVector, indexI, indexJ);

        for (Integer i : indexI) {
            for (Integer j : indexJ) {
                Box box = game.getTable().getBoxArray()[i][j];
                if (box != null) {
                    ArrayList<Position> posiblePositions = calculateMaxPosition(directionVector, box);

                    Position next = posiblePositions.get(1);
                    Box nextBox = null;
                    if (next.getX() <= 3 && next.getY() <= 3 && next.getX() >= 0 && next.getY() >= 0) {
                        nextBox = game.getTable().getBoxArray()[next.getX()][next.getY()];
                    }
                    if (nextBox != null && nextBox.getContent() == box.getContent() && !nextBox.isMixed()) {
                        // podre mezclarme
                        ConstraintLayout table = this.findViewById(R.id.table);

                        Box mixedBox = new Box(box.getContent() * 2, next);
                        this.score += mixedBox.getContent();
                        if (this.score > this.bestScore) {
                            this.bestScore = this.score;
                        }

                        mixedBox.setMixed(true);
                        mixedBox.setView(generateViewForBox(mixedBox.getContent()));

                        game.getTable().getBoxArray()[box.getPosition().getX()][box.getPosition().getY()] = null;
                        game.getTable().getBoxArray()[nextBox.getPosition().getX()][nextBox.getPosition().getY()] = null;

                        game.getTable().getBoxArray()[mixedBox.getPosition().getX()][mixedBox.getPosition().getY()] = mixedBox;


                        table.removeView(box.getView());
                        table.removeView(nextBox.getView());


                        applyMovement(mixedBox, next);
                    } else {
                        Position anterior = posiblePositions.get(0);
                        game.getTable().getBoxArray()[box.getPosition().getX()][box.getPosition().getY()] = null;
                        box.setPosition(anterior);
                        game.getTable().getBoxArray()[box.getPosition().getX()][box.getPosition().getY()] = box;
                        applyMovement(box, anterior);
                    }

                    //realizar comprobaciones
                    //box.setMoved(true);
                }


            }
        }

        insertRandomBox();

        if (isGameOver()) {
            Toast.makeText(this, "GAME OVER :(", Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Box box = game.getTable().getBoxArray()[i][j];
                if (box != null) {
                    box.setMixed(false);
                }

            }
        }

        this.updateStats();
    }

    private void cleanView(){
        ConstraintLayout table = this.findViewById(R.id.table);

        for(int i = 0;i<4;i++){
            for(int j = 0;j<4;j++){
                Box box = game.getTable().getBoxArray()[i][j];
                if(box!=null){
                    table.removeView(box.getView());
                }
            }
        }


    }

    private void updateStats() {
        scoreNumberView.setText(this.score + "");
        bestScoreNumberView.setText(this.bestScore + "");

        this.currentScore.setPoints(this.score);
        this.currentScore.setSecondsGame((Calendar.getInstance().getTimeInMillis() / 1000) - (this.currentScore.getDate()/1000));

        this.currentScore = this.db.updateScore(this.currentScore);

    }

    private boolean isGameOver() {
        return game.getTable().getPositiontsValidToGenerate().size() == 0;
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


}

