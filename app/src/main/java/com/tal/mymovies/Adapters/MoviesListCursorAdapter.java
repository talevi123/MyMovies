package com.tal.mymovies.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.tal.mymovies.R;

/**
 * Created by tal on 20/09/16.
 */
public class MoviesListCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;

    public MoviesListCursorAdapter(Context context,Cursor cursor,int flags){
        super(context,cursor,flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.activity_line_list,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
