package com.bigfacestusio.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
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


public class DetailFragment extends Fragment {


    public Uri mUri;
    private Cursor mDetailCursor;
    public Movie mMovie;
    public IButtonClickListener iButtonClickListener; // for favorite buttion
    public Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    /**
     * Check whether a movie (based on movieId) is in favorite database
     *
     * @param mid
     * @return
     */
    public boolean isFavorite(String mid) {
        String mSelectionClause = MovieContract.MovieDBColumns.COL_MID + " = ?";
        String[] mSelectionArgs = {mid};

        Cursor c = getActivity().getContentResolver().query(MovieContract.MovieDBColumns.CONTENT_URI, null, mSelectionClause, mSelectionArgs, null, null);

        if (c != null && c.getCount() == 1)
            return true;
        return false;
    }


    public void populateImageToImageView(ImageView iv, String imageURL) {
        Picasso.with(getActivity().getApplicationContext()).load(imageURL).into(iv); //set movie poster
    }

    @Override
    public void onStart() {
        super.onStart();

        //populate views with infomation
        View v = getView();
        TextView movieTitleView = (TextView) v.findViewById(R.id.textview_movie_title);
        ImageView moviePosterView = (ImageView) v.findViewById(R.id.imageview_movie_poster);
        TextView movieOverviewView = (TextView) v.findViewById(R.id.textview_overview);
        TextView movieRatingView = (TextView) v.findViewById(R.id.textview_user_rating);
        TextView movieReleaseDateView = (TextView) v.findViewById(R.id.textview_release_date);

        getActivity().setTitle(mMovie.getTitle());
        movieTitleView.setText(mMovie.getTitle());
        movieReleaseDateView.setText(mMovie.getReleaseDate());
        movieRatingView.setText(mMovie.getVoteAverage() + "/10");
        movieOverviewView.setText(mMovie.getOverview());
        populateImageToImageView(moviePosterView, mMovie.getPosterImagePath());

        // Set favorite toggle button state
        ToggleButton tb = (ToggleButton) getActivity().findViewById(R.id.togglebutton_favorite);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (iButtonClickListener != null)
                ((IButtonClickListener) getActivity()).onFavButtonClicked(v);
            }
        });
        if (isFavorite(mMovie.getMId())) {
            tb.setChecked(true);
        } else {
            tb.setChecked(false);
        }

        FetchMovieTrailerAndReview f = new FetchMovieTrailerAndReview(this, mMovie);
        f.execute(mMovie.getMId());
    }

    public void setMoive(Movie movie) {
        this.mMovie = movie;
    }

    /**
     * Will be called by Async Task to update trailer textviews
     *
     * @param trailers
     */
    public void updateTrailerListView(Vector<String> trailers) {
        if (trailers == null) return;

        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.placeholder_layout_trailers);
        if (linearLayout != null) {
            //Clear all views before adding new ones
            linearLayout.removeAllViews();

            if (trailers.isEmpty()) {
                TextView divider = new TextView(mContext);
                divider.setHeight(2);
                divider.setWidth(-1);
                divider.setBackgroundColor(Color.BLACK);
                linearLayout.addView(divider);

                final TextView rowTextView = new TextView(mContext);
                rowTextView.setText(mContext.getString(R.string.msg_there_is_no_trailers));
                rowTextView.setTextColor(Color.BLUE);
                rowTextView.setPadding(5, 30, 5, 30);
                linearLayout.addView(rowTextView);
            }


            for (int i = 0; i < trailers.size(); i++) {

                TextView divider = new TextView(mContext);
                divider.setHeight(2);
                divider.setWidth(-1);
                divider.setBackgroundColor(Color.BLACK);
                linearLayout.addView(divider);

                final TextView rowTextView = new TextView(mContext);
                rowTextView.setText("\uD83C\uDFA5    " + mContext.getString(R.string.trailer_prefix) + " " + (i + 1));
                rowTextView.setPadding(5, 30, 5, 30);
                rowTextView.setTextColor(Color.BLUE);
                final int index = i;
                rowTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Open trailer vidwo in youtube
                        String uriString = "http://www.youtube.com/watch?v=" + mMovie.getTrailers().get(index);
                        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
                        startActivity(youtubeIntent);
                    }
                });
                linearLayout.addView(rowTextView);
            }

            TextView divider = new TextView(mContext);
            divider.setHeight(2);
            divider.setWidth(-1);
            divider.setBackgroundColor(Color.BLACK);
            linearLayout.addView(divider);

        }
    }

    /**
     * Will be called by Async Task to update review textviews
     *
     * @param reviews
     */
    public void updateReviewListView(Vector<String> reviews) {

        if (reviews == null) return;

        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.placeholder_layout_reviews);
        if (linearLayout != null) {
            //Clear all views before adding new ones
            linearLayout.removeAllViews();

            if (reviews.isEmpty()) {

                TextView divider = new TextView(mContext);
                divider.setHeight(2);
                divider.setWidth(-1);
                divider.setBackgroundColor(Color.BLACK);
                linearLayout.addView(divider);


                final TextView rowTextView = new TextView(mContext);
                rowTextView.setTextColor(Color.BLUE);
                rowTextView.setText(mContext.getString(R.string.msn_there_is_no_reviews));
                rowTextView.setPadding(5, 30, 5, 30);
                linearLayout.addView(rowTextView);
            }

            for (String r : reviews) {
                TextView divider = new TextView(mContext);
                divider.setHeight(2);
                divider.setWidth(-1);
                divider.setBackgroundColor(Color.BLACK);
                linearLayout.addView(divider);


                final TextView rowTextView = new TextView(mContext);
                rowTextView.setText(r);
                rowTextView.setTextColor(Color.BLUE);
                rowTextView.setPadding(5, 30, 5, 30);
                linearLayout.addView(rowTextView);
            }

            TextView divider = new TextView(mContext);
            divider.setHeight(2);
            divider.setWidth(-1);
            divider.setBackgroundColor(Color.BLACK);
            divider.setPadding(0, 30, 0, 0);
            linearLayout.addView(divider);

        }
    }



}//End of class
