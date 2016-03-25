package com.bigfacestusio.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivityFragment extends Fragment {

    private GridViewAdapter mGridViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mGridViewAdapter = new GridViewAdapter(
                getContext(),
                R.layout.layout_grid_item,
                new ArrayList<Movie>());
        GridView g = (GridView) v.findViewById(R.id.movie_gridview);
        g.setAdapter(mGridViewAdapter);


        // Click listener to gridview
        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie m = (Movie) parent.getItemAtPosition(position);
                Intent detailIntent = new Intent(getContext(), DetailActivity.class);
                detailIntent.putExtra(Movie.KEY_POSTER_PATH, m.getPosterImagePath());
                detailIntent.putExtra(Movie.KEY_TITLE, m.getTitle());
                detailIntent.putExtra(Movie.KEY_OVERVIEW, m.getOverview());
                detailIntent.putExtra(Movie.KEY_VOTE_AVERAGE, m.getVoteAverage());
                detailIntent.putExtra(Movie.KEY_RELEASE_DATE, m.getReleaseDate());
                startActivity(detailIntent);
            }
        });
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        updateMovieData();
    }

    /**
     * Run data query and update UI
     */
    private void updateMovieData() {
        if (isConnected()) {
            FetchMovieData fm = new FetchMovieData();
            fm.execute(mGridViewAdapter);
        }
        else
            Toast.makeText(getContext(), getContext().getResources().getText(R.string.msg_no_internet_connection), Toast.LENGTH_SHORT).show();
    }

    /**
     * http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
     * @return
     */
    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}//End of class
