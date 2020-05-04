package com.example.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetail extends AppCompatActivity {

    public static JSONObject json;
    String imageUrl = "https://image.tmdb.org/t/p/w185";
    String movie_poster;
    String title;
    String overview;
    String date;
    String voteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        //Get the different views.
        ImageView posterView = findViewById(R.id.poster_view);
        TextView synopsisView = findViewById(R.id.overview_tv);
        TextView releaseDate = findViewById(R.id.release_date_tv);
        TextView originalTitle = findViewById(R.id.original_title_tv);
        TextView userRating = findViewById(R.id.rating_bar);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        try {
            json = new JSONObject(intent.getStringExtra("json"));

            if (json != null){
                //Now that we have the data from the intent, lets fill the details.
                movie_poster = json.getString("poster_path");
                Picasso.get().load(imageUrl+movie_poster).into(posterView);
                title = json.getString("original_title");
                originalTitle.setText(title);
                overview = json.getString("overview");
                synopsisView.setText(overview);
                date = json.getString("release_date");
                releaseDate.setText(date);
                voteAverage = json.getString("vote_average");
                userRating.setText(voteAverage);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {

    }
}
