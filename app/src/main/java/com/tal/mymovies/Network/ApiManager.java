package com.tal.mymovies.Network;

import com.tal.mymovies.DB.DBManager;
import com.tal.mymovies.Moduls.Cinema;
import com.tal.mymovies.Moduls.Movie;
import com.tal.mymovies.MyMoviesApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronen_abraham on 7/7/16.
 */
public class ApiManager {

    private static final String BASE_URL = "http://www.omdbapi.com/?";
    private static final String PARAM_S = "s=";
    private static final String PARAM_I = "i=";
    private static final String URL_SUFFIX = "&r=json&type=movie";

    ///maps url
    private static final String GOOGLEAPIS_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String PARAM_LOCATION = "location=";
    private static final String GOOGLEAPIS_URL_SUFFIX = "&radius=10000&type=movie_theater&key=AIzaSyD5anlL6tztkNRrsZK9PBiEhkuzVRsz5Ow";


    public static List<Movie> searchMovie(String title) {
        DBManager.getInstance(MyMoviesApplication.getInstance()).deleteAllMovies();
        String searchResultJson = ConnectionManager.sendGetRequest(BASE_URL + PARAM_S + title + URL_SUFFIX);
        if (searchResultJson != null) {
            List<Movie> movieList = new ArrayList<>();
            try {
                JSONObject searchJsonObject = new JSONObject(searchResultJson);
                JSONArray searchJsonArray = searchJsonObject.optJSONArray("Search");
                if (searchJsonArray != null) {
                    for (int i = 0; i < searchJsonArray.length(); i++) {
                        Movie movie = new Movie(searchJsonArray.optJSONObject(i));
                        boolean isFavorite = DBManager.getInstance(MyMoviesApplication.getInstance()).checkIfExsists(movie.getimdbId());
                        movie.setFavorie(isFavorite);
                        movieList.add(movie);
                        DBManager.getInstance(MyMoviesApplication.getInstance()).addMovie(movie);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movieList;
        }
        return null;
    }

    public static Movie findMovie(String title) {
        String videoId = getId(title);
        String searchResultJson = ConnectionManager.sendGetRequest(BASE_URL + PARAM_I + title + URL_SUFFIX);
        if (searchResultJson != null) {
            try {
                JSONObject searchJsonObject = new JSONObject(searchResultJson);
                Movie movie = new Movie(searchJsonObject);
                movie.SetVideoId(videoId);
                return movie;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<Cinema> getPlaces(String url) {
        String result = ConnectionManager.sendGetRequest(url);
        if (result != null) {
            List<Cinema> cinemas = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.optJSONArray("results");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Cinema cinema = new Cinema(jsonArray.optJSONObject(i));
                        cinemas.add(cinema);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return cinemas;
        }
        return null;
    }

    private static String getId(String title) {
        String ans = Youtubeconnector.getInstance().search(title);
        return ans;
    }

    public static String getUrl(double latitude, double longitude) {
        String location = PARAM_LOCATION + latitude + "," + longitude;
        return (GOOGLEAPIS_URL + location + GOOGLEAPIS_URL_SUFFIX).toString();
    }

}


