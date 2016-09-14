package com.tal.mymovies.Network;

import com.tal.mymovies.Moduls.Movie;

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


    public static List<Movie> searchMovie(String title) {
        String searchResultJson = ConnectionManager.sendGetRequest(BASE_URL + PARAM_S + title + URL_SUFFIX);
        if (searchResultJson != null) {
            List<Movie> movieList = new ArrayList<>();
            try {
                JSONObject searchJsonObject = new JSONObject(searchResultJson);
                JSONArray searchJsonArray = searchJsonObject.optJSONArray("Search");
                if (searchJsonArray != null) {
                    for (int i = 0; i < searchJsonArray.length(); i++) {
                        Movie movie = new Movie(searchJsonArray.optJSONObject(i));
                        movieList.add(movie);
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

    private static String getId(String title) {
        String ans = Youtubeconnector.getInstance().search(title);
        return ans;
    }


}
