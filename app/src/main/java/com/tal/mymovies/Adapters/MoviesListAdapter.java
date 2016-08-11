package com.tal.mymovies.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tal.mymovies.Moduls.Movie;
import com.tal.mymovies.R;

import java.util.List;

/**
 * Adapter that takes care
 */
public class MoviesListAdapter extends ArrayAdapter<Movie> {

    private static final String TAG = "MoviesListAdapter";

    private Context context;

    public MoviesListAdapter(Context context, int textViewResourceId,
                             List<Movie> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_line_list, parent, false);

        ImageView icon = (ImageView) view.findViewById(R.id.list_image);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView genre = (TextView) view.findViewById(R.id.genre);
        TextView duration = (TextView) view.findViewById(R.id.duration);

        Movie movie = getItem(position);
        Log.v(TAG, "Position = " + position + " Movie = " + movie.toString());

        Picasso.with(context).load(movie.getImageUrl()).into(icon);
        title.setText(movie.getTitle());
        genre.setText(movie.getGenre());
        duration.setText(movie.getDuration());


        return view;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
