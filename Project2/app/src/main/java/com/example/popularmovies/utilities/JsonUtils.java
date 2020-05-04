package com.example.popularmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//This is a straight forward class similar to the one used in the Sandwich app.
public class JsonUtils {
    public static String[] parseMoviesJson(String json) {
        try {
            JSONObject rawJson = new JSONObject(json);
            JSONArray moviesJson = rawJson.getJSONArray("results");

            if(moviesJson != null){
                int len = moviesJson.length();
                String[] movies = new String[len];
                for (int i = 0; i < len; i++){
                    movies[i] = moviesJson.getString(i);
                }
                return movies;
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
