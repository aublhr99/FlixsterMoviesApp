package com.example.flixstermoviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixstermoviesapp.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView ivBackdropImage;
    String backdropUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        ivBackdropImage = (ImageView) findViewById(R.id.ivBackdropImage);

        backdropUrl = getIntent().getStringExtra("backdropUrl") + getIntent().getStringExtra("backdropSize")
                + movie.getBackdropPath();

        Glide.with(getApplicationContext())
                .load(backdropUrl)
                .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), 25, 0))
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .error(R.drawable.flicks_backdrop_placeholder)
                .into(ivBackdropImage);

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        Log.d("Backdrop Path", backdropUrl);

        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
    }
}
