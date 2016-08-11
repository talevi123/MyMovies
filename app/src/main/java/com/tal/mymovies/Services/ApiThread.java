package com.tal.mymovies.Services;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tal.mymovies.Moduls.Movie;
import com.tal.mymovies.Network.ApiManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ApiThread extends Thread {


    public static final String REQUEST_SEARCH_MOVIE = "search_movie";
    public static final String KEY_MOVIES = "movies_list";

    public static final String KEY_RECEIVER = "receiver";
    public static final String KEY_API_METHOD = "api_method";
    public static final String KEY_SEARCH = "search";

    Handler handler;
    private String searchParam;

    public ApiThread(Handler handler, Bundle bundle) {
        initBundle(bundle);
        this.handler = handler;
    }

    private void initBundle(Bundle bundle) {
        if (bundle != null) {
            if (bundle.getString(KEY_API_METHOD).equals(REQUEST_SEARCH_MOVIE)) {
                searchParam = bundle.getString(KEY_SEARCH);
            }
        }
    }

    @Override
    public void run() {
        List<Movie> movieieList = ApiManager.searchMovie(searchParam);

        Bundle resultBundle = new Bundle();
        resultBundle.putString(KEY_API_METHOD, REQUEST_SEARCH_MOVIE);
        JSONArray jsonArray = new JSONArray();
        for (Movie movie : movieieList) {
            try {
                jsonArray.put(new JSONObject(movie.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        resultBundle.putString(KEY_MOVIES, jsonArray.toString());

        Message msg = new Message();
        msg.setData(resultBundle);
        handler.sendMessage(msg);
    }
}
