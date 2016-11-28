package com.tal.mymovies.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tal.mymovies.DB.DBManager;
import com.tal.mymovies.DB.SQLiteHelper;
import com.tal.mymovies.R;

/**
 * Created by tal on 20/09/16.
 */
public class MoviesListCursorAdapter extends CursorAdapter {

    private Context context;

    public MoviesListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, true);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.activity_line_list, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView icon = (ImageView) view.findViewById(R.id.list_image);
        Picasso.with(context).load(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_IMAGE))).into(icon);

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_TITLE)));

        TextView genre = (TextView) view.findViewById(R.id.genre);
        genre.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_GENRE)));

        ImageView likeImg = (ImageView) view.findViewById(R.id.likeImageView);

        likeImg.setImageResource(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_FAV)) == 1 ? R.drawable.ic_liked : R.drawable.ic_unliked);
       // likeImg.setOnClickListener(new OnMovieFavClickListener(cursor));

    }

    private class OnMovieFavClickListener implements View.OnClickListener {

        private Cursor cursor;

        public OnMovieFavClickListener(Cursor cursor) {
            this.cursor = cursor;
        }

        @Override
        public void onClick(View view) {

            int movieId = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID));
            if (cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_FAV)) == 1) {
                DBManager.getInstance(context).setFav(movieId, 0);
                DBManager.getInstance(context).deleteMovieCursor(cursor);
            } else {
                DBManager.getInstance(context).addCursorToFav(cursor);
                DBManager.getInstance(context).setFav(movieId, 1);
            }

            swapCursor(DBManager.getInstance(context).getAllMoviesAsCursor());
        }
    }
}
