package com.example.flixstermoviesapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    String imageBaseUrl;
    String posterSize;
    String backdropSize;

    public Config(JSONObject response) throws JSONException {
        JSONObject images = response.getJSONObject("images");
        this.imageBaseUrl = images.getString("secure_base_url");
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        this.posterSize = posterSizeOptions.optString(3, "w342");
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        this.backdropSize = backdropSizeOptions.optString(1, "w780");
    }

    public String getImageBaseUrl(String size, String path) {
        return String.format("%s%s%s", imageBaseUrl, size, path);
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }
}
