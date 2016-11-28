package com.tal.mymovies.Activities;

import android.content.Intent;
//import android.graphics.Movie;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.tal.mymovies.Moduls.Movie;
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
        Movie movie = (Movie) i.getExtras().getSerializable("Movie");

        title.setText(movie.getTitle());
        description.setText(movie.getDescription());
        director.setText(movie.getDirector() + " (" + movie.getYear() + ")");
        min.setText(movie.getDuration());
        genre.setText(movie.getGenre());
        rating.setText(movie.getRating());
        Picasso.with(this).load(movie.getImageUrl()).into(image);

        final String video_id = movie.getVideoId();
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
