package com.example.application2048.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
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
    private GameSwipeListener gameSwipeListener;

    // views
    private TextView scoreNumberView;
    private TextView bestScoreNumberView;

    ////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Store.MainContext = this;

        this.db = new DBSQLite(this);

        this.scoreNumberView = findViewById(R.id.scoreNumber);
        this.bestScoreNumberView = findViewById(R.id.bestNumber);
        askUsername();
    }

    /**
     * Busca el score máximo.
     */
    private void searchMaxScore() {
        Score maxScore = this.db.findMaxScore();
        if (maxScore != null) {
            this.bestScore = maxScore.getPoints();
            this.firstBestScore = this.bestScore;
        }
    }

    /**
     * Inicia una partida nueva.
     * @param v
     */
    public void onClickSTartNewGame(View v) {
        startNewGame();
    }

    /**
     * Construye un popUp para que el usuario ingrese su nombre antes de iniciar el juego.
     */
    private void askUsername() {
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

    /**
     * Muestra el mensaje de finalización del juego.
     */
    public void showGameOver(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game over! Press ok to play again.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startNewGame();
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
    /**
     * Inicia un score son sus atributos básicos para cada juego nuevo.
     */
    private void initScore() {
        this.score = 0;
        long millisDate = Calendar.getInstance().getTimeInMillis();
        this.currentScore = new Score(0, 0, millisDate, 0, username);
        currentScore = this.db.insertScore(currentScore);
        updateStats();
    }

    /**
     * Inicia una nueva partida.
     */
    private void startNewGame() {
        if (game != null) {
            cleanView();
        }

        game = new Game();
        searchMaxScore();
        insertFirstBoxes();
        setEvents();
        initScore();
    }

    /**
     * Setea un capa transparente encima de la tabla para coger los movimientos.
     */
    private void setEvents() {
        ConstraintLayout transparence = this.findViewById(R.id.transparence);
        transparence.setOnTouchListener(new GameSwipeListener(this));

    }

    /**
     * Identifica a qué dirección se deben mover los boxes.
     *
     * @param direction
     * @return
     */
    private Position directionVector(int direction) {
        Position pos;

        switch (direction) {
            case 0://up
                pos = new Position(-1, 0);
                break;
            case 1://down
                pos = new Position(1, 0);
                break;
            case 2://let
                pos = new Position(0, -1);
                break;
            case 3://rigth
                pos = new Position(0, 1);
                break;
            default:
                pos = new Position(0, 0);
                break;
        }
        return pos;
    }

    /**
     * Calcula la máxima posición a la que se puede mover un box.
     *
     * @param direction
     * @param box
     * @return
     */
    public ArrayList<Position> calculateMaxPosition(Position direction, Box box) {
        Position previousPosition = new Position(0, 0);
        Position nextPosition = new Position(box.getPosition().getX(), box.getPosition().getY());

        do {
            previousPosition = new Position(nextPosition.getX(), nextPosition.getY());
            nextPosition = previousPosition.sum(direction);
        } while ((nextPosition.getX() >= 0 && nextPosition.getX() < 4 && nextPosition.getY() >= 0 && nextPosition.getY() < 4)
                && (game.getTable().getBoxArray()[nextPosition.getX()][nextPosition.getY()] == null));


        ArrayList<Position> result = new ArrayList<>();
        result.add(previousPosition);
        result.add(nextPosition);

        return result;
    }

    /**
     * Calcula el orden de los indices para recorrer la tabla de acuerdo al tipo de vector de dirección.
     * @param direction
     * @param indexI
     * @param indexJ
     */
    private void getLoopIndex(Position direction, ArrayList<Integer> indexI, ArrayList<Integer> indexJ) {

        //0/0 -> 3/3
        for (int i = 0; i < 4; i++) {
            indexI.add(i);
            indexJ.add(i);
        }

        //3/0 -> 0/3
        if (direction.getX() == 1) {
            indexI.clear();
            for (int i = 3; i >= 0; i--) {
                indexI.add(i);
            }
        }

        //0/3 -> 3/0
        if (direction.getY() == 1) {
            indexJ.clear();
            for (int i = 3; i >= 0; i--) {
                indexJ.add(i);
            }
        }
    }

    /**
     * Mueve un box en el modelo y la vista.
     *
     * @param direction
     */
    public void move(int direction) {

        if (isGameOver()) {
            Toast.makeText(this, "GAME OVER :(", Toast.LENGTH_SHORT).show();
            showGameOver();
        }

        // 0 up 1 down 2 left 3 right
        boolean move = false;
        Position directionVector = directionVector(direction);
        ArrayList<Integer> indexI = new ArrayList<>();
        ArrayList<Integer> indexJ = new ArrayList<>();
        getLoopIndex(directionVector, indexI, indexJ);

        for (Integer i : indexI) {
            for (Integer j : indexJ) {
                Box box = game.getTable().getBoxArray()[i][j];
                if (box != null) {
                    ArrayList<Position> possiblePositions = calculateMaxPosition(directionVector, box);

                    Position next = possiblePositions.get(1);
                    Box nextBox = null;
                    if (next.getX() <= 3 && next.getY() <= 3 && next.getX() >= 0 && next.getY() >= 0) {
                        nextBox = game.getTable().getBoxArray()[next.getX()][next.getY()];
                    }
                    if (nextBox != null && nextBox.getContent() == box.getContent() && !nextBox.isMixed()) {
                        // podré mezclarme
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
                        mixedBox.setMoved(true);
                        table.removeView(box.getView());
                        table.removeView(nextBox.getView());

                        applyMovement(mixedBox, next);
                    } else {
                        //movimiento limpio
                        Position previous = possiblePositions.get(0);
                        if(box.getPosition().getX()== previous.getX() && box.getPosition().getY()==previous.getY())   {
                            box.setMoved(false);
                        }else{
                            box.setMoved(true);
                        }
                        game.getTable().getBoxArray()[box.getPosition().getX()][box.getPosition().getY()] = null;
                        box.setPosition(previous);
                        game.getTable().getBoxArray()[box.getPosition().getX()][box.getPosition().getY()] = box;

                        applyMovement(box, previous);
                    }
                }
            }
        }

        for (Integer x : indexI) {
            for (Integer y : indexJ) {
                Box boxMov = game.getTable().getBoxArray()[x][y];
                if (boxMov != null) {
                    if (boxMov.isMoved()) {
                        move = true;
                    }
                }
            }
        }

        if (move) {
            insertRandomBox();
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Box box = game.getTable().getBoxArray()[i][j];
                if (box != null) {
                    box.setMixed(false);
                    box.setMoved(false);
                }
            }
        }

        this.updateStats();
    }

    /**
     * Limpia el view de boxes.
     */
    private void cleanView() {
        ConstraintLayout table = this.findViewById(R.id.table);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Box box = game.getTable().getBoxArray()[i][j];
                if (box != null) {
                    table.removeView(box.getView());
                }
            }
        }


    }

    /**
     *
     */
    private void updateStats() {
        scoreNumberView.setText(this.score + "");
        bestScoreNumberView.setText(this.bestScore + "");

        this.currentScore.setPoints(this.score);
        this.currentScore.setSecondsGame((Calendar.getInstance().getTimeInMillis() / 1000) - (this.currentScore.getDate() / 1000));

        this.currentScore = this.db.updateScore(this.currentScore);
    }

    /**
     * Indica que el juego ha terminado.
     * @return
     */
    private boolean isGameOver() {
        boolean anyBoxCanMerge = false;
        for(int i =0;i<4 && !anyBoxCanMerge;i++){
            for(int j=0;j<4;j++){
                Box box = this.game.getTable().getBoxArray()[i][j];
                if(box!=null){
                    boolean canMerge = false;
                    ArrayList<Box> adjacentBoxes = game.getAdjacentBoxes(box);
                    for(Box adjacent: adjacentBoxes){
                        if(adjacent.getContent() == box.getContent()){
                            canMerge = true;
                            break;
                        }
                    }
                    if(canMerge){
                        anyBoxCanMerge = true;
                        break;
                    }
                }
            }
        }
        return !anyBoxCanMerge && game.getTable().getPositiontsValidToGenerate().size() == 0;
    }

    /**
     * Aplica el movimiento del box actualizando la vista.
     * @param box
     * @param nextPosition
     */
    private void applyMovement(Box box, Position nextPosition) {
        ConstraintLayout table = this.findViewById(R.id.table);

        ConstraintSet layoutView = new ConstraintSet();
        TextView view = box.getView();

        setPositionInView(layoutView, view.getId(), nextPosition);
        layoutView.applyTo(table);
    }

    /**
     * Inserta los primeros 3 boxes al iniciar la partida.
     */
    public void insertFirstBoxes() {
        int numBoxes = 3;
        for (int i = 0; i < numBoxes; i++) {
            this.insertRandomBox();
        }
    }

    /**
     * Inserta una caja aleatoriamente si hay espacio para hacerlo.
     */
    private void insertRandomBox() {
        ArrayList<Position> validPositionsToGenerate = game.getTable().getPositiontsValidToGenerate();

        if (!validPositionsToGenerate.isEmpty()) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, (validPositionsToGenerate.size() - 1) + 1);
            Position randomPos = validPositionsToGenerate.get(randomNum);
            insertBoxModelAndView(randomPos);
        }
    }

    /**
     * Inserta un box aleatorio en el modelo y la vista.
     * @param randomPos
     */
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

    /**
     * Seteaba el textView para que herede las dimensiones del imageView.
     * @param layoutView
     * @param id
     */
    private void setWidthHeightAndRatio(ConstraintSet layoutView, int id) {
        layoutView.constrainHeight(id, 0);
        layoutView.constrainWidth(id, 0);
        layoutView.setDimensionRatio(id, "1:1");
    }

    /**
     * Conecta los lado del textView.
     * @param layoutView
     * @param id
     * @param randomPos
     */
    private void setPositionInView(ConstraintSet layoutView, int id, Position randomPos) {

        String idImageView = "box" + randomPos.getX() + "" + randomPos.getY();
        int idImgView = getResources().getIdentifier(idImageView, "id", getPackageName());
        layoutView.connect(id, ConstraintSet.RIGHT, idImgView, ConstraintSet.RIGHT, 0);
        layoutView.connect(id, ConstraintSet.LEFT, idImgView, ConstraintSet.LEFT, 0);
        layoutView.connect(id, ConstraintSet.TOP, idImgView, ConstraintSet.TOP, 0);
        layoutView.connect(id, ConstraintSet.BOTTOM, idImgView, ConstraintSet.BOTTOM, 0);
    }


    /**
     * Genera un view para un box.
     * @param content
     * @return
     */
    private TextView generateViewForBox(int content) {
        TextView view = (TextView) this.getLayoutInflater().inflate(R.layout.box, null);
        view.setId(View.generateViewId());
        view.setText(content + "");
        ConstraintLayout table = this.findViewById(R.id.table);

        table.addView(view);

        return view;
    }


}

