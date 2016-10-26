package com.tal.mymovies.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tal.mymovies.Activities.FavoritesFragment;
import com.tal.mymovies.Activities.MovieListFragment;

/**
 * Created by tal on 26/10/16.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MovieListFragment main = new MovieListFragment();
                return main;
            case 1:
                FavoritesFragment fav = new FavoritesFragment();
                return fav;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
