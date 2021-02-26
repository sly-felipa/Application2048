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
                "SECONDS_GAME REAL" +
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

    @Override
    public Score findScoreById(int id) {
        return null;
    }

    @Override
    public ArrayList<Score> findByPoints(int points, DBFilters filter) {
        return null;
    }

    @Override
    public ArrayList<Score> findAllScores() {
        ArrayList<Score> scores = new ArrayList<>();
        String query = "SELECT * FROM SCORES";
        Cursor cursor = null;

        try{
            if(mReadableDB == null){
                mReadableDB = getReadableDatabase();
            }

            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();

            do{
                Score score = new Score(
                        cursor.getLong(cursor.getColumnIndex("ID")),
                        cursor.getInt(cursor.getColumnIndex("POINTS")),
                        cursor.getInt(cursor.getColumnIndex("DATE")),
                        cursor.getDouble(cursor.getColumnIndex("SECONDS_GAME"))
                );
                scores.add(score);
                cursor.moveToNext();
            }while(!cursor.isAfterLast());

        }catch (Exception ex){
            Log.d(TAG, "QUERY ALL EXCEPTION! " + ex.getMessage());
        }
        finally{
            if(cursor!= null){
                cursor.close();
            }
        }

        return scores;
    }

    @Override
    public Score insertScore(Score score) {
        ContentValues values = new ContentValues();
        //values.put("ID",);
        long generatedId = 0;
        values.put("POINTS", score.getPoints());
        values.put("DATE", score.getDate());
        values.put("SECONDS_GAME",score.getSecondsGame());
        try{
            if(mWritableDB == null){
                mWritableDB = getWritableDatabase();
            }
            generatedId = mWritableDB.insert("SCORES",null,values);
        }catch (Exception ex){
            Log.d(TAG, "INSERT EXCEPTION! " + ex.getMessage());
        }

        score.setId(generatedId);
        return score;
    }

    @Override
    public void deleteScore(Score score) {
        int affectedRows = 0;

        try {
            if(mWritableDB==null){
                mWritableDB = getWritableDatabase();
            }
            affectedRows = mWritableDB.delete("SCORES","ID = ?",new String[]{String.valueOf(score.getId())});
        }catch(Exception ex){
            Log.d(TAG, "DELETE EXCEPTION! " + ex.getMessage());
        }
    }

    @Override
    public Score updateScore(Score score) {
        return null;
    }
}
