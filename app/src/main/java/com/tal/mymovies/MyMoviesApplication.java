package com.tal.mymovies;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ronen_abraham on 9/20/16.
 */
public class MyMoviesApplication extends Application {

    private static MyMoviesApplication instance;
    private Activity currentActivity;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        instance = this;
    }

    public static MyMoviesApplication getInstance() {
        return instance;
    }

    public void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void closeKeyboard() {
        View view = getCurrentActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
