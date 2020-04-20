package com.abdulalim.moviespot.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulalim.moviespot.Activities.MovieDetailActivity;
import com.abdulalim.moviespot.Adapters.FavoritesAdapters;
import com.abdulalim.moviespot.Constants;
import com.abdulalim.moviespot.DBHelper.DB;
import com.abdulalim.moviespot.DBHelper.Movie;
import com.abdulalim.moviespot.R;

import java.util.List;


public class FavFragment extends Fragment {


    private RecyclerView R_list_favorites;
    private ProgressBar P_progress;
    private LinearLayout L_layout_empty_list;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fav, container, false);
        sharedPreferences = getActivity().getSharedPreferences(Constants.SharedPrefs, Context.MODE_PRIVATE);
        initViews(v);

        return v;
    }

    private void initViews(View v) {
        R_list_favorites = v.findViewById(R.id.list_fav_movies);
        P_progress = v.findViewById(R.id.progress);
        L_layout_empty_list = v.findViewById(R.id.layout_empty_list);

        int userID = sharedPreferences.getInt("userID", 0);

        DB db = new DB(getActivity().getApplicationContext());

        final List<Movie> movies = db.fetchFavMovies(userID);

        FavoritesAdapters favoritesAdapters = new FavoritesAdapters(getActivity().getApplicationContext(), movies);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        R_list_favorites.setLayoutManager(layoutManager);
        R_list_favorites.setItemAnimator(new DefaultItemAnimator());
        favoritesAdapters.notifyDataSetChanged();
        R_list_favorites.setAdapter(favoritesAdapters);

        Log.i("FAV FRAG", ""+movies.size());

        //hide progress bar
        P_progress.setVisibility(View.GONE);
        if(movies.size() == 0){
            //DIsplay Empty message
            L_layout_empty_list.setVisibility(View.VISIBLE);
        }else{
            R_list_favorites.setVisibility(View.VISIBLE);
        }


        //Set OnClickListeners on Movies
        //To view MovieDetails of the selected movie
        favoritesAdapters.setOnItemClickListener(new FavoritesAdapters.OnItemClickListener() {
            @Override
            public void viewMovieDetails(int position) {
                Intent movieDetailintent = new Intent(getActivity().getApplicationContext(), MovieDetailActivity.class);
                movieDetailintent.putExtra("movieID", movies.get(position).getMovieID());
                startActivity(movieDetailintent);
            }
        });

    }


}
