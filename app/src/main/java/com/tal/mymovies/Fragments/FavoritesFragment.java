package com.tal.mymovies.Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tal.mymovies.Adapters.FavListCursorAdapter;
import com.tal.mymovies.DB.DBManager;
import com.tal.mymovies.Moduls.Movie;
import com.tal.mymovies.R;

public class FavoritesFragment extends Fragment {

    ListView listView;
    FavListCursorAdapter favListCursorAdapter;
    MovieListFragment.SearchMovieListener searchMovieListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        initFavlist(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateFavorites();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            searchMovieListener = (MovieListFragment.SearchMovieListener) context;
        }
    }

    public void initFavlist(View view) {
                listView = (ListView) view.findViewById(R.id.fav_listview);
                Cursor c = DBManager.getInstance(getActivity()).getAllFavMoviesAsCursor();
                favListCursorAdapter = new FavListCursorAdapter(getActivity(), c);

                listView.setAdapter(favListCursorAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Movie movie = Movie.createMovie(listView.getItemAtPosition(position));
                        searchMovieListener.initMovie(movie.getimdbId());
                    }
                });
    }

    public void updateFavorites() {
        Cursor c = DBManager.getInstance(getActivity()).getAllFavMoviesAsCursor();
        favListCursorAdapter.swapCursor(c);
    }

}
