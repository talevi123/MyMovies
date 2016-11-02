package com.tal.mymovies.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.tal.mymovies.Adapters.MoviesListAdapter;
import com.tal.mymovies.Adapters.MoviesListCursorAdapter;
import com.tal.mymovies.DB.DBManager;
import com.tal.mymovies.Moduls.Movie;
import com.tal.mymovies.R;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment {

    private BaseAdapter adapter;
    private EditText searchBox;
    ProgressDialog progressDialog;
    private ListView listview;
    SearchMovieListener searchMovieListener;
    private String moviesListAdapterType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        initSearchButton(view);
        initMoviesList(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            searchMovieListener = (SearchMovieListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initMovieListType();
    }

    private void initMovieListType() {
        moviesListAdapterType = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(getString(R.string.pref_adapter_key), getString(R.string.array_adapter));
    }

    public void updateListViewAdapter(List<Movie> movieList) {
        if (adapter instanceof MoviesListAdapter) {
            MoviesListAdapter moviesListAdapter = (MoviesListAdapter) adapter;
            moviesListAdapter.clear();
            moviesListAdapter.addAll(movieList);
            adapter.notifyDataSetChanged();
        } else {
            MoviesListCursorAdapter moviesListCursorAdapter = (MoviesListCursorAdapter) adapter;
            moviesListCursorAdapter.swapCursor(DBManager.getInstance(getActivity()).getAllMoviesAsCursor());
        }

        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void initSearchButton(View view) {
        searchBox = (EditText) view.findViewById(R.id.searchBox);
        ImageButton searchBtn = (ImageButton) view.findViewById(R.id.searchButton);
        if (searchBtn != null) {
            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
                    if (searchBox != null && searchBox.getText() != null && searchBox.getText().length() > 0) {
                        searchMovieListener.searchMovie((searchBox.getText().toString()));
                    }
                }
            });
        }
    }

    public void initMoviesList(View view) {

        initMovieListType();

        listview = (ListView) view.findViewById(R.id.listview);
        List<Movie> movies = new ArrayList<>();

        if (moviesListAdapterType.equals(getString(R.string.array_adapter))) {
            adapter = new MoviesListAdapter(getActivity(), R.layout.activity_line_list, movies);
        } else {
            Cursor allMoviesAsCursor = DBManager.getInstance(getActivity()).getAllMoviesAsCursor();
            adapter = new MoviesListCursorAdapter(getActivity(), allMoviesAsCursor);
        }

        if (listview != null) {
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
                    Movie movie = Movie.createMovie(listview.getItemAtPosition(position));
                    searchMovieListener.initMovie(movie.getimdbId());
                }
            });
        }
    }

    public Editable getSearchBoxValue() {
        return searchBox.getText();
    }

    public interface SearchMovieListener {
        void searchMovie(String name);

        void initMovie(String imdbId);
    }

}