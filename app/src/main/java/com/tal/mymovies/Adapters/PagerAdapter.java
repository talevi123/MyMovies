package com.tal.mymovies.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tal.mymovies.Fragments.FavoritesFragment;
import com.tal.mymovies.Fragments.MovieListFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tal on 26/10/16.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;
    private Map<Integer, Fragment> fragments;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        fragments = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MovieListFragment main = new MovieListFragment();
                fragments.put(position, main);
                return main;
            case 1:
                FavoritesFragment fav = new FavoritesFragment();
                fragments.put(position, fav);
                return fav;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }

}
