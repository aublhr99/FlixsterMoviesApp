package com.example.flixstermoviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.flixstermoviesapp.models.Config;
import com.example.flixstermoviesapp.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    public final static String API_KEY_PARAM = "api_key";
    public final static String TAG = "Flixster";

    AsyncHttpClient client;
    ArrayList<Movie> movies;
    RecyclerView rvMovies;
    MovieAdapter adapter;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new AsyncHttpClient();
        movies = new ArrayList<>();
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        adapter = new MovieAdapter(movies);

        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        getConfiguration();
    }

    private void getNowPlaying() {
        String url = API_BASE_URL + "/movie/now_playing";
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to properly parse now playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
                logError("Failed to get data from now_playing endpoint", throwable, true);
            }
        });
    }

    private void getConfiguration() {
        String url = API_BASE_URL + "/configuration";
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
                try {
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posdterSize %s",
                            config.getImageBaseUrl(),
                            config.getPosterSize()));
                    adapter.setConfig(config);
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
                logError("Failed getting configuration", throwable, true);
            }
        });
        getNowPlaying();
    }

    private void logError(String message, Throwable error, boolean alertUser) {
        Log.e(TAG, message);
        if (alertUser) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
