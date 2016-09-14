package com.tal.mymovies.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.tal.mymovies.Moduls.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronen_abraham on 8/30/16.
 */
public class DBManager {

    private static DBManager instance;

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = {SQLiteHelper.COLUMN_ID,SQLiteHelper.COLUMN_IMDB_ID,SQLiteHelper.COLUMN_TITLE,
                                    SQLiteHelper.COLUMN_DESCRIPTION,SQLiteHelper.COLUMN_IMAGE,SQLiteHelper.COLUMN_DURATION,
                                    SQLiteHelper.COLUMN_YEAR,SQLiteHelper.COLUMN_DIRECTOR,SQLiteHelper.COLUMN_GENRE,
                                    SQLiteHelper.COLUMN_RATING};

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

    public List<Movie> getAllMovies(){
        List<Movie> movies = new ArrayList<>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_MOVIES,allColumns,null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Movie movie = cursorToMovie(cursor);
            movies.add(movie);
            cursor.moveToNext();
        }
        cursor.close();
        return movies;
    }

    private Movie cursorToMovie(Cursor cursor){

        JSONObject movie = new JSONObject();
        for(int i=0; i<allColumns.length ;i++) {
            try {
                if (i == 0)
                    movie.put(cursor.getColumnName(i), cursor.getInt(i));
                else
                    movie.put(cursor.getColumnName(i), cursor.getString(i));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new Movie(movie);
    }
}
