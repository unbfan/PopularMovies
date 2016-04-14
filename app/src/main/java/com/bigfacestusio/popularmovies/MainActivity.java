package com.bigfacestusio.popularmovies;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ToggleButton;

import com.bigfacestusio.popularmovies.data.MovieContract;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity implements IGridItemSelectListener, IButtonClickListener {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean isTwoPaneMode;
    private Movie currentSelectedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Restore saved movie
        if (savedInstanceState != null) {
            currentSelectedMovie = savedInstanceState.getParcelable(Movie.KEY_MID);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // init Stetho
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

        // check whehther it's in pad mode
        if (findViewById(R.id.movie_detail_container) != null) {
            isTwoPaneMode = true;
        } else {
            isTwoPaneMode = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent prefIntent = new Intent(this, SettingActivity.class);
            startActivity(prefIntent);
            return true;
        }
        if (isTwoPaneMode) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Movie.KEY_MID, this.currentSelectedMovie);
    }

    @Override
    public void onGridItemSelected(AdapterView<?> parent, View view, int position) {
        currentSelectedMovie = (Movie) parent.getItemAtPosition(position);
        if (isTwoPaneMode) {
            DetailFragment df = new DetailFragment();
            df.setMoive(currentSelectedMovie);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.movie_detail_container, df, DETAILFRAGMENT_TAG);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

        } else {
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putExtra(Movie.KEY_MID, currentSelectedMovie);

            startActivity(detailIntent);
        }
    }

    @Override
    public void onFavButtonClicked(View view) {

        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieDBColumns.COL_MID, currentSelectedMovie.getMId());
        values.put(MovieContract.MovieDBColumns.COL_TITLE, currentSelectedMovie.getTitle());
        values.put(MovieContract.MovieDBColumns.COL_POSTER_PATH, currentSelectedMovie.getPosterImagePath());
        values.put(MovieContract.MovieDBColumns.COL_OVERVIEW, currentSelectedMovie.getOverview());
        values.put(MovieContract.MovieDBColumns.COL_RELEASE_DATE, currentSelectedMovie.getReleaseDate());
        values.put(MovieContract.MovieDBColumns.COL_POPULARITY, currentSelectedMovie.getPopularity());

        ToggleButton cb = (ToggleButton) view;
        if (cb.isChecked()) {
            this.getContentResolver().insert(MovieContract.MovieDBColumns.CONTENT_URI, values);
        } else {
            String mSelectionClause = MovieContract.MovieDBColumns.COL_MID + " = ?";
            String[] mSelectionArgs = {currentSelectedMovie.getMId()};

            this.getContentResolver().delete(MovieContract.MovieDBColumns.CONTENT_URI, mSelectionClause, mSelectionArgs);
        }
    }


}//End of class
