package com.tal.mymovies.Moduls;

import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by ronen_abraham on 6/29/16.
 */
public class Movie  {
    public int id;
    public String title;
    public String description;
    public String imageUrl;
    public String duration;
    public String year;
    public String imdbId;
    public String genre;


    public Movie(int id, String title, String description, String imageUrl, long duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Movie(JSONObject jsonObject) {
        title = jsonObject.optString("Title");
        year = jsonObject.optString("Year");
        imdbId = jsonObject.optString("imdbID");
        imageUrl = jsonObject.optString("Poster");
        description = jsonObject.optString("Plot");
        duration = jsonObject.optString("Runtime");
        genre = jsonObject.optString("Genre");
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

    @Override
    public String toString() {
        return "{" +
                "\"Title\": " + "\"" +title + "\"" + "," +
                "\"Description\": " + "\"" + description + "\"" + "," +
                "\"Poster\": " + "\"" + imageUrl + "\"" + "," +
                "\"imdbID\": " + "\"" + imdbId + "\"" + "," +
                "\"Year\": " + "\"" + year + "\""  +
                "}";
    }
}
