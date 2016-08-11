package com.tal.mymovies.Services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;

import com.tal.mymovies.Moduls.Movie;
import com.tal.mymovies.Network.ApiManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ApiService extends Service {


    public static final String REQUEST_SEARCH_MOVIE = "search_movie";
    public static final String KEY_MOVIES = "movies_list";

    public static final String KEY_RECEIVER = "receiver";
    public static final String KEY_API_METHOD = "api_method";
    public static final String KEY_SEARCH = "search";

    public ResultReceiver resultReceiver;

    public ApiService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            resultReceiver = intent.getParcelableExtra(KEY_RECEIVER);
            if (intent.getStringExtra(KEY_API_METHOD).equals(REQUEST_SEARCH_MOVIE)) {
                final String searchParam = intent.getStringExtra(KEY_SEARCH);
                new Thread(new Runnable() {
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

                        resultReceiver.send(Activity.RESULT_OK, resultBundle);
                    }
                }).start();
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
