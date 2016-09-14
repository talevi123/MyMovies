package com.tal.mymovies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.tal.mymovies.R;


public class MovieActivity extends YouTubeBaseActivity {

    private YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Button youbtn = (Button) findViewById(R.id.platbtn);
        final YouTubePlayerView youtube = (YouTubePlayerView) findViewById(R.id.youtube);

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
        String getYear = i.getStringExtra("year");
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

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(getIntent().getStringExtra("VIDEO_ID"));
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }

        };

        youbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                youtube.initialize("AIzaSyBI7gz3jbGZdbS9MjlRjnv4Ur-s1s9HckQ",onInitializedListener);
            }
        });
    }
}
