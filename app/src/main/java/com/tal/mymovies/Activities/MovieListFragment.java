package com.tal.mymovies.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.tal.mymovies.Adapters.MoviesListAdapter;
import com.tal.mymovies.Adapters.MoviesListCursorAdapter;
import com.tal.mymovies.DB.DBManager;
import com.tal.mymovies.Moduls.Movie;
import com.tal.mymovies.Network.ApiManager;
import com.tal.mymovies.R;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment  {

    private BaseAdapter adapter;
    private EditText searchBox;
    ProgressDialog progressDialog;
    private ListView listview;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initSearchButton();
        initMoviesList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
            return view;
    }

    private void updateListViewAdapter(List<Movie> movieList) {
        if (adapter instanceof MoviesListAdapter) {
            MoviesListAdapter moviesListAdapter = (MoviesListAdapter) adapter;
            moviesListAdapter.clear();
            moviesListAdapter.addAll(movieList);
            adapter.notifyDataSetChanged();
        } else {
            MoviesListCursorAdapter moviesListCursorAdapter = (MoviesListCursorAdapter) adapter;
            moviesListCursorAdapter.swapCursor(DBManager.getInstance(getActivity()).getAllMoviesAsCursor());
        }

        progressDialog.dismiss();
    }

    private void initSearchButton() {
        searchBox = (EditText) getView().findViewById(R.id.searchBox);
        ImageButton searchBtn = (ImageButton) getView().findViewById(R.id.searchButton);
        if (searchBtn != null) {
            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
                    if (searchBox != null && searchBox.getText() != null && searchBox.getText().length() > 0) {
                        new SearchMoviesTask().execute(searchBox.getText().toString());
                    }
                }
            });
        }
    }

    public class SearchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... params) {
            return ApiManager.searchMovie(params[0]);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                updateListViewAdapter(movies);
            }
        }
    }

    public void initMoviesList() {

        listview = (ListView) getView().findViewById(R.id.listview);
        List<Movie> movies = new ArrayList<>();

        adapter = new MoviesListAdapter(getActivity(), R.layout.activity_line_list, movies);

        if (listview != null) {
            listview.setAdapter(adapter);

        }
    }


}