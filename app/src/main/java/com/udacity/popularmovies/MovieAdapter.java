package com.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.model.Genre;
import com.udacity.popularmovies.model.GenreContainer;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.MovieContainer;
import com.udacity.popularmovies.utilities.NetworkUtil;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ouz on 24/02/18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    Context context;
    MovieContainer movieContainer;
    List<Movie> movieList;
    Boolean isLandscapeMode;

    /*
    public MovieAdapter(Context mContext, List<Movie> movies) {
        this.context = mContext;
        this.movieList = movies;
    }
    */

    public MovieAdapter(Context mContext, List<Movie> movies,Boolean orientationMode) {
        this.context = mContext;
        this.movieList = movies;
        this.isLandscapeMode = orientationMode;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view;

        if(isLandscapeMode){
            view = inflater.inflate(R.layout.movie_card_land, parent, false);
        }else {
            view = inflater.inflate(R.layout.movie_card, parent, false);
        }

        MovieAdapterViewHolder viewHolder = new MovieAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {

        final Movie movie = movieList.get(position);



        if(isLandscapeMode){
            holder.movie_land_title.setText(movie.getTitle());
            holder.movie_land_tagline.setText(movie.getTagline());
            holder.movie_land_genres.setText(genresToString(movie.getGenre_ids()));

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(movie.getRelease_date());
            holder.movie_land_release_date.setText(String.valueOf(calendar.get(Calendar.DATE))+" / "
                    + String.valueOf(calendar.get(Calendar.MONTH))+" / "
                    + String.valueOf(calendar.get(Calendar.YEAR)));

            Picasso.with(context)
                    .load(NetworkUtil.buildImageUrl(movie.getPoster_path()).toString())
                    .into(holder.movie_land_poster);
        }else{
            holder.movie_title.setText(movie.getTitle());

            Picasso.with(context)
                    .load(NetworkUtil.buildImageUrl(movie.getPoster_path()).toString())
                    .into(holder.movie_poster);
        }

    }



    @Override
    public int getItemCount() {

        if (movieList != null && movieList.size() > 0)
            return movieList.size();
        else
            return 0;
    }

    @Override
    public long getItemId(int position) {

        if (movieList != null && movieList.size() > 0)
            return new Long(movieList.get(position).getId());
        else
            return 0;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView movie_poster,movie_land_poster;
        TextView movie_title,movie_land_release_date,movie_land_genres,movie_land_tagline,movie_land_title;

        public MovieAdapterViewHolder(View view) {
            super(view);

            if(isLandscapeMode){
                movie_land_poster = view.findViewById(R.id.movie_land_thumbnail);
                movie_land_title = view.findViewById(R.id.tv_main_land_title);
                movie_land_tagline = view.findViewById(R.id.tv_main_land_tagline);
                movie_land_genres = view.findViewById(R.id.tv_main_land_genre);
                movie_land_release_date = view.findViewById(R.id.tv_main_land_release_date);

            }else {
                movie_poster = view.findViewById(R.id.movie_thumbnail);
                movie_title = view.findViewById(R.id.movie_title);
            }

            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {

            Movie selectedMovie = movieList.get(getAdapterPosition());
            Log.d("CLICK_POSTER", " THUMBNAIL POSTER has CLICKED !\nHere is the title : " + selectedMovie.getTitle());

            Intent intent = new Intent(context,DetailActivity.class);
            intent.putExtra("movie_id",selectedMovie.getId());
            intent.putExtra("genres",genresToString(selectedMovie.getGenre_ids()));
            context.startActivity(intent);
        }
    }

    public void setData(MovieContainer data) {
        movieContainer = data;

        if (movieContainer != null) {
            movieList = movieContainer.getResults();
        }

        notifyDataSetChanged();
    }

    @NonNull
    private String genresToString(List<String> genreList){

        StringBuilder builder = new StringBuilder();

        for(String element : genreList){
            Genre genre = GenreContainer.findGenre(element);
            if(genre != null ){
                builder.append(genre.getName()+" " );
            }
        }

        return builder.toString();
    }
}
