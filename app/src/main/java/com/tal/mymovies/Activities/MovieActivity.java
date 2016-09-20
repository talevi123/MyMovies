package com.tal.mymovies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.tal.mymovies.MyMoviesApplication;
import com.tal.mymovies.Network.Youtubeconnector;
import com.tal.mymovies.R;


public class MovieActivity extends YouTubeBaseActivity {

    private YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        final YouTubePlayerView youtube = (YouTubePlayerView) findViewById(R.id.youtube);

        ImageView image = (ImageView) findViewById(R.id.icon);
        TextView title = (TextView) findViewById(R.id.title);
        TextView description = (TextView) findViewById(R.id.plot);
        TextView director = (TextView) findViewById(R.id.director);
        TextView min = (TextView) findViewById(R.id.min);
        TextView genre = (TextView) findViewById(R.id.genre);
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
        director.setText(getDirector + " (" + getYear + ")");
        min.setText(getMin);
        genre.setText(getGenre);
        rating.setText(getRating);
        Picasso.with(this).load(getImageurl).into(image);

        final String video_id = getIntent().getStringExtra("VIDEO_ID");
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (video_id != null) {
                    youTubePlayer.cueVideo(video_id);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }

        };
        if (video_id != null) {
            youtube.initialize(Youtubeconnector.KEY_API, onInitializedListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyMoviesApplication.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        MyMoviesApplication.getInstance().setCurrentActivity(null);
        super.onPause();
    }
}
