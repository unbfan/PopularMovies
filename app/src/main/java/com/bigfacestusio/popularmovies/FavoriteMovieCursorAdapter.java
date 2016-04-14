package com.bigfacestusio.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigfacestusio.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteMovieCursorAdapter extends CursorAdapter {
    private static final String LOG_TAG = FavoriteMovieCursorAdapter.class.getSimpleName();

    private Context mContext;
    private static int sLoaderID;
    private ArrayList<Movie> favoriteMovies = new ArrayList<>();

    public FavoriteMovieCursorAdapter(Context context, Cursor c, int flags, int loaderID) {
        super(context, c, flags);

        mContext = context;
        sLoaderID = loaderID;
    }
    public FavoriteMovieCursorAdapter(Context context, Cursor c) {
        super(context, c);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = R.layout.layout_grid_item;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String id = cursor.getString(MovieContract.MovieDBColumns.COL_I_MID);
        String title = cursor.getString(MovieContract.MovieDBColumns.COL_I_TITLE);
        String poster_path = cursor.getString(MovieContract.MovieDBColumns.COL_I_POSTER_PATH);
        String overview = cursor.getString(MovieContract.MovieDBColumns.COL_I_OVERVIEW);
        String release_date = cursor.getString(MovieContract.MovieDBColumns.COL_I_RELEASE_DATE);
        String popularity = cursor.getString(MovieContract.MovieDBColumns.COL_I_POPULARITY);
        String vote_average = cursor.getString(MovieContract.MovieDBColumns.COL_I_VOTE_AVERAGE);

        Movie m = new Movie(id, title, poster_path, overview, release_date, popularity, vote_average);
        favoriteMovies.add(m);

        viewHolder.titleTextView.setText(m.getTitle()); // set movice title
        Picasso.with(mContext).load(m.getPosterImagePath()).into(viewHolder.imageView); //set movie poster
    }

    @Override
    public Movie getItem(int position) {
        return favoriteMovies.get(position);
    }

    public static class ViewHolder {
        public final ImageView imageView;
        public final TextView titleTextView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.grid_item_image);
            titleTextView = (TextView) view.findViewById(R.id.grid_item_title);
        }
    }

}
