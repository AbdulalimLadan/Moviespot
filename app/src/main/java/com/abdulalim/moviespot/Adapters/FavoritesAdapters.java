package com.abdulalim.moviespot.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulalim.moviespot.DBHelper.Movie;
import com.abdulalim.moviespot.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoritesAdapters extends RecyclerView.Adapter<FavoritesAdapters.FavoritesViewHolder> {

    private List<Movie> movies;
    private OnItemClickListener listener;
    private Context context;

    public FavoritesAdapters(Context context, List<Movie> movies){
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_favorites, parent, false);
        return new FavoritesViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        //Populate the views with the movie details
        holder.T_movie_title.setText(movies.get(position).getTitle());

        //Picasso the ImageURL into the ImageView
        String imageUrl = movies.get(position).getPosterPath();
        Picasso.get().load(imageUrl).resize(100, 150).into(holder.I_movie_poster);


    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setOnItemClickListener(FavoritesAdapters.OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void viewMovieDetails(int position);
    }

    class FavoritesViewHolder extends RecyclerView.ViewHolder{

        //Create View instances from list_movies.xml
        CardView C_movie_card;
        TextView T_movie_title;
        ImageView I_movie_poster;

        FavoritesViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            C_movie_card = itemView.findViewById(R.id.movie_card);
            T_movie_title = itemView.findViewById(R.id.fav_movie_title);
            I_movie_poster = itemView.findViewById(R.id.fav_movie_poster);
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
