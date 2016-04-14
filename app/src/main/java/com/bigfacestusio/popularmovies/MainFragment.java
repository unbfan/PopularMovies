package com.bigfacestusio.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.bigfacestusio.popularmovies.data.MovieContract;

import java.util.ArrayList;


public class MainFragment extends Fragment {

    private GridViewAdapter mGridViewAdapter;
    private Cursor mCursor;
    private FavoriteMovieCursorAdapter mFavoriteMovieCursorAdapter;
    private Context mContext;

    private IGridItemSelectListener iGridItemSelectListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mContext = v.getContext();

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
                if (iGridItemSelectListener != null)
                    iGridItemSelectListener.onGridItemSelected(parent, view, position);
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
     * Attach item click listener for GridView
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        iGridItemSelectListener = (IGridItemSelectListener) context;
    }

    /**
     * Run data query and update UI
     */
    private void updateMovieData() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String sortbyStr = pref.getString(mContext.getString(R.string.pref_sortby_key), mContext.getString(R.string.pref_sortby_most_popular_searchStr));

        if (sortbyStr.equals(mContext.getString(R.string.pref_sortby_favorite_searchStr))) {

            //Loading favorite movies
            mCursor = getActivity().getContentResolver().query(MovieContract.MovieDBColumns.CONTENT_URI, null, null, null, null);

            if (mCursor.getCount() < 1) {
                //Do not have any favorite movies
                Toast.makeText(mContext, mContext.getResources().getText(R.string.msg_no_favorite_movie_found), Toast.LENGTH_LONG).show();
            } else {
                mFavoriteMovieCursorAdapter = new FavoriteMovieCursorAdapter(mContext, mCursor);

                GridView g = (GridView) getActivity().findViewById(R.id.movie_gridview);
                g.setAdapter(mFavoriteMovieCursorAdapter);
            }
        } else {
            if (isConnected()) {
                FetchMovieData fm = new FetchMovieData();
                fm.execute(mGridViewAdapter);
            } else
                Toast.makeText(mContext, mContext.getResources().getText(R.string.msg_no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mCursor != null)
            mCursor.close();
    }

    /**
     * http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
     *
     * @return
     */

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected(); //isConnectedOrConnecting();
        return isConnected;
    }

}//End of class
