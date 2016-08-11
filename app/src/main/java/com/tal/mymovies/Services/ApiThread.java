package com.tal.mymovies.Services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ResultReceiver;

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

    public ApiThread(Handler handler, Intent intent) {
        initIntent(intent);
        this.handler = handler;
    }

    private void initIntent(Intent intent) {
        if (intent != null) {
            if (intent.getStringExtra(KEY_API_METHOD).equals(REQUEST_SEARCH_MOVIE)) {
                searchParam = intent.getStringExtra(KEY_SEARCH);
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
