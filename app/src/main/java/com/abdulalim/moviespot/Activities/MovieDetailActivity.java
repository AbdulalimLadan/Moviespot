package com.abdulalim.moviespot.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulalim.moviespot.Adapters.GenresAdapter;
import com.abdulalim.moviespot.Api.WootlabMovieDB_Api;
import com.abdulalim.moviespot.Constants;
import com.abdulalim.moviespot.DBHelper.DB;
import com.abdulalim.moviespot.FileHelper.FileHelper;
import com.abdulalim.moviespot.Models.Movie;
import com.abdulalim.moviespot.Models.Videos;
import com.abdulalim.moviespot.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailActivity extends AppCompatActivity {

    Movie movie;

    int movieID, userID;
    boolean isInFav;

    RecyclerView R_genre_list;
    TextView T_title, T_runtime, T_overview, T_release_date, T_rating, T_fav_text;
    ImageView I_poster, I_backdrop, I_fav_toggle_on, I_fav_toggle_off;
    RelativeLayout RL_loading_screen;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_movie_detail);
        Objects.requireNonNull(getSupportActionBar()).hide();
        sharedPreferences = getSharedPreferences(Constants.SharedPrefs, Context.MODE_PRIVATE);

        initBundles();
        initViews();
        fetchData();
    }

    private void initBundles() {
        //This method receives the data passed from the activity calling this activity
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        movieID = bundle.getInt("movieID");
        userID = sharedPreferences.getInt("userID", 0);
    }

    private void fetchData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WootlabMovieDB_Api wootlabMovieDBApi = retrofit.create(WootlabMovieDB_Api.class);
        Call<Movie> call = wootlabMovieDBApi.getMovieDetail(movieID);

        DB db = new DB(this);
        isInFav = db.isInFavorites(userID, movieID);
        if(isInFav){
            //Show Thick fav Icon
            I_fav_toggle_on.setVisibility(View.VISIBLE);
            I_fav_toggle_off.setVisibility(View.GONE);
            T_fav_text.setText("Remove from Favorites");
        }else {
            //show hollow fav Icon
            I_fav_toggle_on.setVisibility(View.GONE);
            I_fav_toggle_off.setVisibility(View.VISIBLE);
            T_fav_text.setText("Add to Favorites");
        }

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(MovieDetailActivity.this, "SCould not get details. Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                    Log.i("sdew", "ER : " + response.message());
                    onBackPressed();
                    return;
                }

                movie = response.body();
                //Display Data
                Picasso.get().load(movie.getBackdropPath()).resize(1024, 768).into(I_backdrop);
                Picasso.get().load(movie.getPosterPath()).resize(400, 450).into(I_poster);
                T_title.setText(movie.getTitle());
                T_runtime.setText(movie.getRuntime()+"");
                T_rating.setText(movie.getVoteAverage()+"");
                T_overview.setText(movie.getPlotSummary());
                int[] date = movie.getReleaseDate();
                T_release_date.setText(date[0]+"-"+date[1]+"-"+date[2]);

                //TODO Load Genres
                GenresAdapter genresAdapter = new GenresAdapter(movie.getGenres());
                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(MovieDetailActivity.this);
                layoutManager.setFlexDirection(FlexDirection.ROW);
                R_genre_list.setLayoutManager(layoutManager);
                genresAdapter.notifyDataSetChanged();
                R_genre_list.setAdapter(genresAdapter);

                //Hide the Loading screen
                RL_loading_screen.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(MovieDetailActivity.this, "WCould not get details. Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                onBackPressed();
                Log.i("sdew", "E : " + t.getMessage());
            }
        });
    }

    private void initViews() {
        R_genre_list = findViewById(R.id.detail_list_genres);


        T_title = findViewById(R.id.detail_title);
        T_runtime = findViewById(R.id.detail_runtime);
        T_release_date = findViewById(R.id.detail_release_date);
        T_overview = findViewById(R.id.detail_overview);
        T_rating = findViewById(R.id.detail_rating);
        T_fav_text = findViewById(R.id.fav_text);


        I_backdrop = findViewById(R.id.detail_backdrop);
        I_poster = findViewById(R.id.detail_poster);
        I_fav_toggle_on = findViewById(R.id.fav_toggle_on);
        I_fav_toggle_off = findViewById(R.id.fav_toggle_off);

        RL_loading_screen = findViewById(R.id.loading_screen);
    }

    public void watchTrailer(View view) {
        //Take user to Youtube
        if(movie.getVideos().size() > 0){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + movie.getVideos().get(0).getKey()));
            startActivity(intent);
        }else{
            Toast.makeText(this, "Movie trailer not available.", Toast.LENGTH_SHORT).show();
        }

    }

    public void goToSite(View view) {
        if(!movie.getHomepage().isEmpty()){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getHomepage()));
            startActivity(intent);
        }else{
            Toast.makeText(this, "Movie homepage not available.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addToFavorites(View view) {
        DB db = new DB(this);
        //Check if in Fav
        //If yes, remove from fav. if no, add to fav
        if(isInFav){
            //Remove from Favorites
            com.abdulalim.moviespot.DBHelper.Movie moviex = new com.abdulalim.moviespot.DBHelper.Movie();
            moviex.setMovieID(movieID);
            moviex.setUserID(userID);

            db.removeFavMovie(moviex);

            Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
            I_fav_toggle_on.setVisibility(View.GONE);
            I_fav_toggle_off.setVisibility(View.VISIBLE);
            T_fav_text.setText("Add to Favorites");

        }else{
            //Add to Favorites
            //Save MovieID, MovieName, moviePoster
            com.abdulalim.moviespot.DBHelper.Movie moviex = new com.abdulalim.moviespot.DBHelper.Movie();
            moviex.setTitle(movie.getTitle());
            moviex.setPosterPath(movie.getPosterPath());
            moviex.setUserID(userID);
            moviex.setMovieID(movie.getId());

            db.addFavMovie(moviex);

            Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
            I_fav_toggle_on.setVisibility(View.VISIBLE);
            I_fav_toggle_off.setVisibility(View.GONE);
            T_fav_text.setText("Remove from Favorites");
        }

        isInFav = !isInFav;

    }
}
