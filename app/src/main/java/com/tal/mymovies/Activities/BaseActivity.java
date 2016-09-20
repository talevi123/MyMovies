package com.tal.mymovies.Activities;

import android.support.v7.app.AppCompatActivity;

import com.tal.mymovies.MyMoviesApplication;

/**
 * Created by ronen_abraham on 9/20/16.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        MyMoviesApplication.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        MyMoviesApplication.getInstance().setCurrentActivity(null);
        super.onPause();
    }
}
