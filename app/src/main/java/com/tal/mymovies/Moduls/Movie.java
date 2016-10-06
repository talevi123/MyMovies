package com.tal.mymovies.Moduls;

import android.database.Cursor;

import com.tal.mymovies.DB.SQLiteHelper;

import org.json.JSONObject;

import static com.google.android.gms.analytics.internal.zzy.c;

/**
 * Created by ronen_abraham on 6/29/16.
 */
public class Movie {
    public int id;
    public String title;
    public String description;
    public String imageUrl;
    public String duration;
    public String year;
    public String director;
    public String imdbId;
    public String genre;
    public String rating;
    public String videoId;


    public static Movie createMovie(Object object) {
        if (object instanceof Movie) {
            return (Movie) object;
        } else if (object instanceof Cursor) {
            return new Movie((Cursor) object);
        } else
            return new Movie((JSONObject) object);
    }

    public Movie(int id, String imdbId, String title, String description, String imageUrl, String duration, String year, String director, String genre, String rating) {
        this.id = id;
        this.imdbId = imdbId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.duration = duration;
        this.year = year;
        this.director = director;
        this.genre = genre;
        this.rating = rating;
    }

    public Movie(JSONObject jsonObject) {
        imdbId = jsonObject.optString("imdbID");
        title = jsonObject.optString("Title");
        year = jsonObject.optString("Year");
        imageUrl = jsonObject.optString("Poster");
        description = jsonObject.optString("Plot");
        director = jsonObject.optString("Director");
        duration = jsonObject.optString("Runtime");
        genre = jsonObject.optString("Genre");
        rating = jsonObject.optString("imdbRating");
    }

    public Movie(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID));
        this.imdbId = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_IMDB_ID));
        this.title = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_TITLE));
        this.year = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_YEAR));
        this.imageUrl = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_IMAGE));
        this.description = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_DESCRIPTION));
        this.director = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_DIRECTOR));
        this.duration = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_DURATION));
        this.genre = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_GENRE));
        this.rating = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_RATING));
    }


    public void SetVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoId() {
        return videoId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDuration() {
        return duration;
    }

    public String getGenre() {
        return genre;
    }

    public String getimdbId() {
        return imdbId;
    }

    public String getDirector() {
        return director;
    }

    public String getYear() {
        return year;
    }

    public String getRating() {
        return rating;
    }


    @Override
    public String toString() {
        return "{" +
                "\"Title\": " + "\"" + title + "\"" + "," +
                "\"Description\": " + "\"" + description + "\"" + "," +
                "\"Poster\": " + "\"" + imageUrl + "\"" + "," +
                "\"imdbID\": " + "\"" + imdbId + "\"" + "," +
                "\"Year\": " + "\"" + year + "\"" +
                "}";
    }
}

