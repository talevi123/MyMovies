package com.tal.mymovies.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tal.mymovies.R;


public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ImageView image =(ImageView)findViewById(R.id.icon);
        TextView title = (TextView) findViewById(R.id.title);
        TextView description = (TextView) findViewById(R.id.description);

        Intent i = getIntent();
        String getTitle = i.getStringExtra("title");
        String getDescription = i.getStringExtra("description");
        String getImageurl = i.getStringExtra("imageUrl");

        title.setText(getTitle);
        description.setText(getDescription);
        Picasso.with(this).load(getImageurl).into(image);
    }
}
