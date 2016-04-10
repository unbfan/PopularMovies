package com.bigfacestusio.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bigfacestusio.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.Vector;


public class DetailActivityFragment extends Fragment {


    public Uri mUri;
    private Cursor mDetailCursor;

    public Movie selectedMovie;


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
        selectedMovie = (Movie) mIntent.getParcelableExtra(Movie.KEY_MID);

        getActivity().setTitle(selectedMovie.getTitle());
        movieTitleView.setText(selectedMovie.getTitle());
        movieReleaseDateView.setText(selectedMovie.getReleaseDate());
        movieRatingView.setText(selectedMovie.getVoteAverage() + "/10");
        movieOverviewView.setText(selectedMovie.getOverview());

        populateImageToImageView(moviePosterView, selectedMovie.getPosterImagePath());

        return v;
    }


    public boolean isFavorite(String mid) {

        String mSelectionClause = MovieContract.MovieDBColumns.COL_MID + " = ?";
        String[] mSelectionArgs = {mid};

        Cursor c = getActivity().getContentResolver().query(MovieContract.MovieDBColumns.CONTENT_URI, null, mSelectionClause, mSelectionArgs, null, null);

        if (c != null && c.getCount() == 1)
            return true;
        return false;
    }


    public void populateImageToImageView(ImageView iv, String imageURL) {
        Picasso.with(getContext()).load(imageURL).into(iv); //set movie poster
    }

    @Override
    public void onStart() {
        super.onStart();

        ToggleButton tb = (ToggleButton) getActivity().findViewById(R.id.togglebutton_favorite);
        if (isFavorite(selectedMovie.getMId())) {
            tb.setChecked(true);
        } else {
            tb.setChecked(false);
        }


        FetchMovieTrailerAndReview f = new FetchMovieTrailerAndReview(this, selectedMovie);
        f.execute(selectedMovie.getMId());
    }

    /**
     * Will be called by Async Task to update trailer textviews
     * @param trailers
     */
    public void updateTrailerListView(Vector<String> trailers) {
        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.placeholder_layout_trailers);
        //Clear all views before adding new ones
        linearLayout.removeAllViews();

        for (int i = 0; i < trailers.size(); i++) {
            final TextView rowTextView = new TextView(getContext());
            rowTextView.setText(getContext().getString(R.string.trailer_prefix) + " " + (i + 1));
            final int index = i;
            rowTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Open trailer vidwo in youtube
                    String uriString = "http://www.youtube.com/watch?v=" + selectedMovie.getTrailers().get(index);
                    Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
                    startActivity(youtubeIntent);
                }
            });
            linearLayout.addView(rowTextView);
        }
    }

    /**
     * Will be called by Async Task to update review textviews
     * @param reviews
     */
    public void updateReviewListView(Vector<String> reviews) {

        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.placeholder_layout_reviews);
        //Clear all views before adding new ones
        linearLayout.removeAllViews();

        for (String r : reviews) {
            final TextView rowTextView = new TextView(getContext());
            rowTextView.setText(r);
            linearLayout.addView(rowTextView);
        }
    }


}//End of class
