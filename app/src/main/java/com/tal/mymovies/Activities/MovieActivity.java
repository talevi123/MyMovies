package com.tal.mymovies.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
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
        TextView description = (TextView) findViewById(R.id.plot);
        TextView director = (TextView) findViewById(R.id.director);
        TextView min = (TextView) findViewById(R.id.min);
        TextView genre  = (TextView) findViewById(R.id.genre);
        TextView rating = (TextView) findViewById(R.id.r);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        Intent i = getIntent();
        String getTitle = i.getStringExtra("title");
        String getDescription = i.getStringExtra("description");
        String getImageurl = i.getStringExtra("imageUrl");
        String getDirector = i.getStringExtra("director");
        String getYear = i.getStringExtra("Year");
        String getMin = i.getStringExtra("min");
        String getGenre = i.getStringExtra("genre");
        String getRating = i.getStringExtra("rating");

        title.setText(getTitle);
        description.setText(getDescription);
        director.setText(getDirector+" ("+getYear+")");
        min.setText(getMin);
        genre.setText(getGenre);
        rating.setText(getRating);
        Picasso.with(this).load(getImageurl).into(image);
    }
}
