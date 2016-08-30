package com.tal.mymovies.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.tal.mymovies.Moduls.Movie;

/**
 * Created by ronen_abraham on 8/30/16.
 */
public class DBManager {

    private static DBManager instance;

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public static DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }

        return instance;
    }

    private DBManager(Context context) {
        dbHelper = new SQLiteHelper(context);
        open();
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addMovie(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(SQLiteHelper.COLUMN_IMDB_ID, movie.getimdbId());
        cv.put(SQLiteHelper.COLUMN_TITLE, movie.getTitle());
        cv.put(SQLiteHelper.COLUMN_DESCRIPTION, movie.getDescription());
        cv.put(SQLiteHelper.COLUMN_IMAGE, movie.getImageUrl());
        cv.put(SQLiteHelper.COLUMN_DURATION, movie.getDuration());
        cv.put(SQLiteHelper.COLUMN_YEAR, movie.getYear());
        cv.put(SQLiteHelper.COLUMN_DIRECTOR, movie.getDirector());
        cv.put(SQLiteHelper.COLUMN_GENRE, movie.getGenre());
        cv.put(SQLiteHelper.COLUMN_RATING, movie.getRating());

        database.insert(SQLiteHelper.TABLE_MOVIES, null, cv);
    }

}
