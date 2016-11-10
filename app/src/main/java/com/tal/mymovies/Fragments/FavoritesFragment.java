package com.tal.mymovies.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tal.mymovies.Activities.MovieActivity;
import com.tal.mymovies.Adapters.FavListCursorAdapter;
import com.tal.mymovies.DB.DBManager;
import com.tal.mymovies.Moduls.Movie;
import com.tal.mymovies.R;

public class FavoritesFragment extends Fragment {

    ListView listView;
    FavListCursorAdapter favListCursorAdapter;

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

    public void initFavlist(View view) {
        listView = (ListView) view.findViewById(R.id.fav_listview);
        Cursor c = DBManager.getInstance(getActivity()).getAllFavMoviesAsCursor();
        favListCursorAdapter = new FavListCursorAdapter(getActivity(), c);

        listView.setAdapter(favListCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = Movie.createMovie(listView.getItemAtPosition(position));
                Bundle bundle =new Bundle();
                bundle.putSerializable("Movie", movie);
                Intent intent = new Intent(getActivity(),MovieActivity.class );
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void updateFavorites() {
        Cursor c = DBManager.getInstance(getActivity()).getAllFavMoviesAsCursor();
        favListCursorAdapter.swapCursor(c);
    }

}
