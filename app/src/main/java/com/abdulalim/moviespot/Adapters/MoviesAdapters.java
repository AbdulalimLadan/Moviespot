package com.abdulalim.moviespot.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulalim.moviespot.Models.Movie;
import com.abdulalim.moviespot.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapters extends RecyclerView.Adapter<MoviesAdapters.MoviesViewHolder> {

    private List<Movie> movies;
    private OnItemClickListener listener;
    private Context context;

    public MoviesAdapters(Context context, List<Movie> movies){
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_movies, parent, false);
        return new MoviesViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        //Populate the views with the movie details
        holder.T_movie_title.setText(movies.get(position).getTitle());
        holder.T_movie_rating.setText(movies.get(position).getVoteAverage()+"");

        //Picasso the ImageURL into the ImageView
        String imageUrl = movies.get(position).getPosterPath();
        Picasso.get().load(imageUrl).resize(100, 150).into(holder.I_movie_poster);

        //TODO set RecycleyView for Genres
        GenresAdapter genresAdapter = new GenresAdapter(movies.get(position).getGenres());
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        holder.R_movie_genres.setLayoutManager(layoutManager);
        genresAdapter.notifyDataSetChanged();
        holder.R_movie_genres.setAdapter(genresAdapter);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setOnItemClickListener(MoviesAdapters.OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void viewMovieDetails(int position);
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder{

        //Create View instances from list_movies.xml
        CardView C_movie_card;
        TextView T_movie_title, T_movie_rating;
        ImageView I_movie_poster;
        RecyclerView R_movie_genres;

        public MoviesViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            C_movie_card = itemView.findViewById(R.id.movie_card);
            T_movie_title = itemView.findViewById(R.id.movie_title);
            T_movie_rating = itemView.findViewById(R.id.movie_rating);
            I_movie_poster = itemView.findViewById(R.id.movie_poster);
            R_movie_genres = itemView.findViewById(R.id.list_genres);
            C_movie_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){

                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            listener.viewMovieDetails(position);
                        }
                    }
                }
            });

        }
    }
}
