package com.bigfacestusio.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailActivityFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView movieTitleView = (TextView) v.findViewById(R.id.textview_movie_title);
        ImageView moviePosterView = (ImageView) v.findViewById(R.id.imageview_movie_poster);

        TextView movieOverviewView = (TextView) v.findViewById(R.id.textview_overview);
        TextView movieRatingView = (TextView) v.findViewById(R.id.textview_user_rating);
        TextView movieReleaseDateView = (TextView) v.findViewById(R.id.textview_release_date);

        Intent mIntent = getActivity().getIntent();
        String title = mIntent.getStringExtra(Movie.KEY_TITLE);
        String imagePosterURL = mIntent.getStringExtra(Movie.KEY_POSTER_PATH);
        String overview = mIntent.getStringExtra(Movie.KEY_OVERVIEW);
        String rating = mIntent.getStringExtra(Movie.KEY_VOTE_AVERAGE);
        String releaseDate = mIntent.getStringExtra(Movie.KEY_RELEASE_DATE);

        getActivity().setTitle(title);
        movieOverviewView.setText(overview);
        movieRatingView.setText(rating);
        movieReleaseDateView.setText(releaseDate);
        movieTitleView.setText(title);

        populateImageToImageView(moviePosterView, imagePosterURL);
        return v;
    }


    public void populateImageToImageView(ImageView iv, String imageURL) {
        Picasso.with(getContext()).load(imageURL).into(iv); //set movie poster
    }
}
