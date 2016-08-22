package com.tal.mymovies.Services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;

import com.tal.mymovies.Activities.MoviesListActivity;
import com.tal.mymovies.Moduls.Movie;
import com.tal.mymovies.Network.ApiManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ApiBroadcastThread extends Thread {


    public static final String REQUEST_SEARCH_MOVIE = "search_movie";
    public static final String KEY_MOVIES = "movies_list";

    public static final String KEY_RECEIVER = "receiver";
    public static final String KEY_API_METHOD = "api_method";
    public static final String KEY_SEARCH = "search";
    private final Context context;

    private String searchParam;

    public ApiBroadcastThread(Context context, Bundle bundle) {
        this.context = context;
        initBundle(bundle);
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

        // send broadcast
        Intent intent = new Intent(MoviesListActivity.EVENT_NETWORK_DATA_READY);

        // add data
        intent.putExtras(resultBundle);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
