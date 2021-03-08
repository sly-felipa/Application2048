package com.example.application2048.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.application2048.model.Score;

import java.util.ArrayList;

public class DBSQLite extends SQLiteOpenHelper implements IDB {
    private static final String TAG = DBSQLite.class.getSimpleName();

    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    public DBSQLite(@Nullable Context context) {
        super(context, DBConfig.DATABASE_NAME, null, DBConfig.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE SCORES (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "POINTS INT," +
                "DATE INTEGER," +
                "SECONDS_GAME REAL," +
                "NAME VARCHAR(50)" +
                ")"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBSQLite.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS SCORES");
        onCreate(db);
    }

    /**
     * Encuentra puntuaciones de acuerdo al filtro que se le ingrese.
     * @param points
     * @param sign
     * @return
     */
    @Override
    public ArrayList<Score> findByPoints(int points, String sign) {
        ArrayList<Score> scores = new ArrayList<>();
        String query = "SELECT * FROM SCORES WHERE POINTS " + sign + " " + points;
        Cursor cursor = null;

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();

            do {
                Score score = new Score(
                        cursor.getLong(cursor.getColumnIndex("ID")),
                        cursor.getInt(cursor.getColumnIndex("POINTS")),
                        cursor.getLong(cursor.getColumnIndex("DATE")),
                        cursor.getDouble(cursor.getColumnIndex("SECONDS_GAME")),
                        cursor.getString(cursor.getColumnIndex("NAME"))
                );
                scores.add(score);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());

        } catch (Exception ex) {
            Log.d(TAG, "QUERY ALL EXCEPTION! " + ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return scores;
    }

    /**
     * Encuentra todos los scores.
     * @return
     */
    @Override
    public ArrayList<Score> findAllScores() {
        ArrayList<Score> scores = new ArrayList<>();
        String query = "SELECT * FROM SCORES";
        Cursor cursor = null;

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();

            do {
                Score score = new Score(
                        cursor.getLong(cursor.getColumnIndex("ID")),
                        cursor.getInt(cursor.getColumnIndex("POINTS")),
                        cursor.getLong(cursor.getColumnIndex("DATE")),
                        cursor.getDouble(cursor.getColumnIndex("SECONDS_GAME")),
                        cursor.getString(cursor.getColumnIndex("NAME"))
                );
                scores.add(score);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());

        } catch (Exception ex) {
            Log.d(TAG, "QUERY ALL EXCEPTION! " + ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return scores;
    }

    /**
     * Encuentra el score con la puntuación más alta.
     * @return
     */
    @Override
    public Score findMaxScore() {
        String query = "SELECT ID, MAX(POINTS), DATE, SECONDS_GAME, NAME  FROM SCORES";

        Cursor cursor = null;

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();

            Score score = new Score(
                    cursor.getLong(cursor.getColumnIndex("ID")),
                    cursor.getInt(cursor.getColumnIndex("MAX(POINTS)")),
                    cursor.getLong(cursor.getColumnIndex("DATE")),
                    cursor.getDouble(cursor.getColumnIndex("SECONDS_GAME")),
                    cursor.getString(cursor.getColumnIndex("NAME"))
            );
            return score;

        } catch (Exception ex) {
            Log.d(TAG, "QUERY ALL EXCEPTION! " + ex.getMessage());
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Inserta un score a la base de datos.
     * @param score
     * @return
     */
    @Override
    public Score insertScore(Score score) {
        ContentValues values = new ContentValues();
        long generatedId = 0;
        values.put("POINTS", score.getPoints());
        values.put("DATE", score.getDate());
        values.put("SECONDS_GAME", score.getSecondsGame());
        values.put("NAME", score.getName());
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            generatedId = mWritableDB.insert("SCORES", null, values);
        } catch (Exception ex) {
            Log.d(TAG, "INSERT EXCEPTION! " + ex.getMessage());
        }

        score.setId(generatedId);
        return score;
    }

    /**
     * Borra un score de la base de datos.
     * @param score
     */
    @Override
    public void deleteScore(Score score) {
        int affectedRows = 0;

        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            affectedRows = mWritableDB.delete("SCORES", "ID = ?", new String[]{String.valueOf(score.getId())});
        } catch (Exception ex) {
            Log.d(TAG, "DELETE EXCEPTION! " + ex.getMessage());
        }
    }

    /**
     * Actualiza la información de un score.
     * @param score
     * @return
     */
    @Override
    public Score updateScore(Score score) {
        long generatedId = 0;
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            ContentValues values = new ContentValues();
            values.put("POINTS", score.getPoints());
            values.put("NAME", score.getName());
            values.put("SECONDS_GAME", score.getSecondsGame());

            generatedId = mWritableDB.update("SCORES", values, "ID" + " = ?",
                    new String[]{String.valueOf(score.getId())});

        } catch (Exception e) {
            Log.d(TAG, "UPDATE EXCEPTION! " + e.getMessage());
        }
        return score;
    }
}
