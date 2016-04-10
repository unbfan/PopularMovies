package com.bigfacestusio.popularmovies;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ToggleButton;

import com.bigfacestusio.popularmovies.data.MovieContract;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    /**
     * Handle toggle button click event
     * @param view
     */
    public void handleFavButtonClick(View view) {
        Movie mm = (Movie) getIntent().getParcelableExtra(Movie.KEY_MID);

        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieDBColumns.COL_MID, mm.getMId());
        values.put(MovieContract.MovieDBColumns.COL_TITLE, mm.getTitle());
        values.put(MovieContract.MovieDBColumns.COL_POSTER_PATH, mm.getPosterImagePath());
        values.put(MovieContract.MovieDBColumns.COL_OVERVIEW, mm.getOverview());
        values.put(MovieContract.MovieDBColumns.COL_RELEASE_DATE, mm.getReleaseDate());
        values.put(MovieContract.MovieDBColumns.COL_POPULARITY, mm.getPopularity());
        values.put(MovieContract.MovieDBColumns.COL_TRAILERS, "--");
        values.put(MovieContract.MovieDBColumns.COL_IS_FAVORITE, 0);


        ToggleButton cb = (ToggleButton) view.findViewById(R.id.togglebutton_favorite);


        if (cb.isChecked()) {
            this.getContentResolver().insert(MovieContract.MovieDBColumns.CONTENT_URI, values);
        } else {
            String mSelectionClause = MovieContract.MovieDBColumns.COL_MID + " = ?";
            String[] mSelectionArgs = {mm.getMId()};

            this.getContentResolver().delete(MovieContract.MovieDBColumns.CONTENT_URI, mSelectionClause, mSelectionArgs);
        }
    }
}
