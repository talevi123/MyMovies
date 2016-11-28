package com.tal.mymovies.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tal.mymovies.DB.SQLiteHelper;
import com.tal.mymovies.Network.Utility;
import com.tal.mymovies.R;

/**
 * Created by tal on 31/10/16.
 */
public class FavListCursorAdapter extends CursorAdapter {

    public FavListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(R.layout.card_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView icon = (ImageView) view.findViewById(R.id.coverImageView);
        if(cursor.getBlob(cursor.getColumnIndex(SQLiteHelper.COLUMN_BITMAP)) != null) {
           byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(SQLiteHelper.COLUMN_BITMAP));
           Bitmap image = Utility.getImage(byteArray);
           icon.setImageBitmap(image);
        } else {
            Picasso.with(context).load(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_IMAGE))).fit().into(icon);
        }
        TextView title = (TextView) view.findViewById(R.id.card_title);
        title.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_TITLE)));
    }



}
