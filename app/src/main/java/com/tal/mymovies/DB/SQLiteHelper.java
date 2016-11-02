package com.tal.mymovies.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ronen_abraham on 8/30/16.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myMovies.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_MOVIES_TABLE);
        db.execSQL(CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * TABLE MOVIES/FAVORITE
     **/
    public static final String TABLE_MOVIES = "movies";
    public static final String TABLE_FAVORITE = "favorite";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IMDB_ID = "imdb_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_DIRECTOR = "director";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_FAV = "fav";

    private static final String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_MOVIES +
            " ( " +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_IMDB_ID + " TEXT," +
            COLUMN_TITLE + " TEXT," +
            COLUMN_DESCRIPTION + " TEXT," +
            COLUMN_IMAGE + " TEXT," +
            COLUMN_DURATION + " TEXT," +
            COLUMN_YEAR + " TEXT," +
            COLUMN_DIRECTOR + " TEXT," +
            COLUMN_GENRE + " TEXT," +
            COLUMN_RATING + " TEXT," +
            COLUMN_FAV + " INTEGER" +
            " )";

    private static final String CREATE_FAVORITE_TABLE = "CREATE TABLE " + TABLE_FAVORITE +
            " ( " +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_IMDB_ID + " TEXT," +
            COLUMN_TITLE + " TEXT," +
            COLUMN_DESCRIPTION + " TEXT," +
            COLUMN_IMAGE + " TEXT," +
            COLUMN_DURATION + " TEXT," +
            COLUMN_YEAR + " TEXT," +
            COLUMN_DIRECTOR + " TEXT," +
            COLUMN_GENRE + " TEXT," +
            COLUMN_RATING + " TEXT," +
            COLUMN_FAV + " INTEGER" +
            " )";


}
