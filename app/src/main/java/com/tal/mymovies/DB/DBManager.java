package com.tal.mymovies.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.tal.mymovies.Moduls.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.analytics.internal.zzy.n;

/**
 * Created by ronen_abraham on 8/30/16.
 */
public class DBManager {

    private static DBManager instance;

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = {SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_IMDB_ID, SQLiteHelper.COLUMN_TITLE,
            SQLiteHelper.COLUMN_DESCRIPTION, SQLiteHelper.COLUMN_IMAGE, SQLiteHelper.COLUMN_DURATION,
            SQLiteHelper.COLUMN_YEAR, SQLiteHelper.COLUMN_DIRECTOR, SQLiteHelper.COLUMN_GENRE,
            SQLiteHelper.COLUMN_RATING, SQLiteHelper.COLUMN_FAV};

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
        cv.put(SQLiteHelper.COLUMN_FAV, movie.isFavorite() ? 1 : 0);
        database.insertWithOnConflict(SQLiteHelper.TABLE_MOVIES, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void addToFav(Movie movie) {
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
        cv.put(SQLiteHelper.COLUMN_FAV, movie.isFavorite() ? 1 : 0);
        database.insertWithOnConflict(SQLiteHelper.TABLE_FAVORITE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void addCursorToFav(Cursor cursor) {
        ContentValues cv = new ContentValues();
        cv.put(SQLiteHelper.COLUMN_IMDB_ID, cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_IMDB_ID)));
        cv.put(SQLiteHelper.COLUMN_TITLE, cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_TITLE)));
        cv.put(SQLiteHelper.COLUMN_DESCRIPTION, cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_DESCRIPTION)));
        cv.put(SQLiteHelper.COLUMN_IMAGE, cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_IMAGE)));
        cv.put(SQLiteHelper.COLUMN_DURATION, cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_DURATION)));
        cv.put(SQLiteHelper.COLUMN_YEAR, cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_YEAR)));
        cv.put(SQLiteHelper.COLUMN_DIRECTOR, cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_DIRECTOR)));
        cv.put(SQLiteHelper.COLUMN_GENRE, cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_GENRE)));
        cv.put(SQLiteHelper.COLUMN_RATING, cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_RATING)));
        cv.put(SQLiteHelper.COLUMN_FAV, 1);
        database.insertWithOnConflict(SQLiteHelper.TABLE_FAVORITE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public boolean checkIfExsists(String imdbId) {
//        String query = "SELECT * FROM " + SQLiteHelper.TABLE_FAVORITE
//                + " WHERE " + SQLiteHelper.COLUMN_IMDB_ID + " = " + "'" + imdbId + "'";
//        Cursor c = database.rawQuery(query, null);
        Cursor c = database.query(SQLiteHelper.TABLE_FAVORITE, null, SQLiteHelper.COLUMN_IMDB_ID + " =? ",
                new String[]{imdbId}, null, null, null);
        return c.getCount() > 0;
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_MOVIES, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Movie movie = new Movie(cursor);
            movies.add(movie);
            cursor.moveToNext();
        }
        cursor.close();
        return movies;
    }

    public void setFav(int movieId, int isLiked) {
        ContentValues cv = new ContentValues();
        cv.put(SQLiteHelper.COLUMN_FAV,isLiked);
        database.update(SQLiteHelper.TABLE_MOVIES, cv, null, null);
    }

    public void deleteMovie(Movie movie) {
        String id = movie.getimdbId();
        database.delete(SQLiteHelper.TABLE_FAVORITE, SQLiteHelper.COLUMN_IMDB_ID + " =? ", new String[]{id});
    }

    public void deleteMovieCursor(Cursor cursor) {
        String id =  cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_IMDB_ID));
        database.delete(SQLiteHelper.TABLE_FAVORITE, SQLiteHelper.COLUMN_IMDB_ID + " =? ", new String[]{id});
    }


    public void deleteAllMovies() {
        database.delete(SQLiteHelper.TABLE_MOVIES, null, null);
    }

    public void deleteAllFavs() {
        database.delete(SQLiteHelper.TABLE_FAVORITE, null, null);
    }

    public Cursor getAllMoviesAsCursor() {
        return database.query(SQLiteHelper.TABLE_MOVIES, allColumns, null, null, null, null, null);
    }

    public Cursor getAllFavMoviesAsCursor() {
        return database.query(SQLiteHelper.TABLE_FAVORITE, allColumns, null, null, null, null, null);
    }

}
