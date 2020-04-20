package com.abdulalim.moviespot.Api;

import com.abdulalim.moviespot.Models.Movie;
import com.abdulalim.moviespot.Models.MoviesList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WootlabMovieDB_Api {

    @GET("movie/list")
    Call<MoviesList> getMovies();

    @GET("movie/detail/{id}")
    Call<Movie> getMovieDetail(@Path("id") int movieID);

}
