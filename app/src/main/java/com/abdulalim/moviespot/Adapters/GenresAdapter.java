package com.abdulalim.moviespot.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulalim.moviespot.Models.Genre;
import com.abdulalim.moviespot.R;

import java.util.List;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.GenresViewHolder> {

    private List<Genre> genreList;

    public GenresAdapter(List<Genre> genreList){
        this.genreList = genreList;
    }

    @NonNull
    @Override
    public GenresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_genres, parent, false);
        return new GenresViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GenresViewHolder holder, int position) {
        holder.T_genre_name.setText(genreList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    class GenresViewHolder extends RecyclerView.ViewHolder{
        TextView T_genre_name;

        public GenresViewHolder(@NonNull View itemView) {
            super(itemView);
            T_genre_name = itemView.findViewById(R.id.movie_genre_name);
        }
    }
}
