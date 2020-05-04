package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.popularmovies.utilities.JsonUtils;
import com.example.popularmovies.utilities.NetworkUtils;
import com.example.popularmovies.utilities.SpacesItemDecoration;
import com.example.popularmovies.utilities.recyclerAdapter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    recyclerAdapter adapter;
    String[] json;
    Context context;
    Boolean firstTime = true;

    /*Ok, a brief explanation about the onCreate: I have an Internet connection checker. If there is
    connection, then I load the data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        new InternetCheck(internet -> { loadMovies("/3/movie/top_rated");});

        final Switch toggleButton =  findViewById(R.id.toggleButton);
        toggleButton.setText(R.string.top_rated);
        toggleButton.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new InternetCheck(internet -> { loadMovies("/3/movie/popular");});
                    toggleButton.setText(R.string.popular);
                }
                else{
                    new InternetCheck(internet -> { loadMovies("/3/movie/top_rated");});
                    toggleButton.setText(R.string.top_rated);
                }
            }
        });
    }

    private void loadMovies(String path){
        new FetchPopularMovies().execute(path);
    }

    /*This is the Internet checker. It runs on another thread. Found how to do this in
    https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out*/
    public static class InternetCheck extends AsyncTask<Void,Void,Boolean> {

        private Consumer mConsumer;
        public  interface Consumer { void accept(Boolean internet); }

        public  InternetCheck(Consumer consumer) { mConsumer = consumer; execute(); }

        @Override protected Boolean doInBackground(Void... voids) { try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            return true;
        } catch (IOException e) { return false; } }

        @Override protected void onPostExecute(Boolean internet) { mConsumer.accept(internet); }
    }

    //The api call to imdb. An asynchronous task to fetch the JSON. I take the short order as a parameter.
    public class FetchPopularMovies extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... moviePath) {
            //Your api key to IMDB goes here.
            String apiKey ="";
            String imdb = "api.themoviedb.org";
            String path = moviePath[0];
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https").authority(imdb).path(path).appendQueryParameter("api_key",apiKey).build();
            URL imdbUri = null;
            try {
                imdbUri = new URL(builder.toString());
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                String moviesJson = NetworkUtils
                        .getResponseFromHttpUrl(imdbUri);
                return moviesJson;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String imdbJson){
            if (imdbJson != null){
                json = JsonUtils.parseMoviesJson(imdbJson);
                if(json != null) {
                    RecyclerView recyclerView = findViewById(R.id.recycler);
                    int numberOfColumns = 2;
                    recyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
                    adapter = new recyclerAdapter(context, json);
                    int spacingPixels = getResources().getDimensionPixelSize(R.dimen.grid_gap);
                    /*Founded something interesting while checking the switcher. Every time I changed
                    the shorting order, the item decoration method made the space between items grow
                    and the pictures itself shrunk, so I ended adding that firstTime thing in order to
                    avoid it. Although, I think I can do it in a simpler way.
                     */
                    recyclerView.addItemDecoration(new SpacesItemDecoration(spacingPixels, firstTime));
                    recyclerView.setAdapter(adapter);
                    firstTime = false;
                }
            }
        }
    }
}