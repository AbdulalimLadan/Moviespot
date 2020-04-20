package com.abdulalim.moviespot.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulalim.moviespot.Activities.MovieDetailActivity;
import com.abdulalim.moviespot.Adapters.MoviesAdapters;
import com.abdulalim.moviespot.Api.WootlabMovieDB_Api;
import com.abdulalim.moviespot.Constants;
import com.abdulalim.moviespot.Models.Movie;
import com.abdulalim.moviespot.Models.MoviesList;
import com.abdulalim.moviespot.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {

    private RecyclerView R_list_movies;
    private ProgressBar P_progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(v);

        return v;
    }

    private void initViews(View v) {

        R_list_movies = v.findViewById(R.id.list_movies);
        P_progress = v.findViewById(R.id.progress);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WootlabMovieDB_Api wootlabMovieDBApi = retrofit.create(WootlabMovieDB_Api.class);

        Call<MoviesList> call = wootlabMovieDBApi.getMovies();
        call.enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(@NonNull Call<MoviesList> call,@NonNull Response<MoviesList> response) {
                if(!response.isSuccessful()){
                    Log.i("HOME", "ER : "+response.code());
                    return;
                }

                MoviesList moviesList = response.body();
                //
                final List<Movie> movies = moviesList.getMovies();
                MoviesAdapters moviesAdapters = new MoviesAdapters(getActivity().getApplicationContext(), movies);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                R_list_movies.setLayoutManager(layoutManager);
                R_list_movies.setItemAnimator(new DefaultItemAnimator());
                moviesAdapters.notifyDataSetChanged();
                R_list_movies.setAdapter(moviesAdapters);

                //hide progress bar
                P_progress.setVisibility(View.GONE);

                //Set OnClickListeners on Movies
                //To view MovieDetails of the selected movie
                moviesAdapters.setOnItemClickListener(new MoviesAdapters.OnItemClickListener() {
                    @Override
                    public void viewMovieDetails(int position) {
                        //Show selected movie details
                        Intent movieDetailintent = new Intent(getActivity().getApplicationContext(), MovieDetailActivity.class);
                        movieDetailintent.putExtra("movieID", movies.get(position).getId());
                        startActivity(movieDetailintent);
                    }
                });

            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                //If there is a server error or Error during conversion of Data to Gson
                Log.i("HOME", "E: "+t.getMessage());
            }
        });

    }


}
