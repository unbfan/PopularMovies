package com.bigfacestusio.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

public class MovieContentProvider extends ContentProvider {

    private static final String LOG_TAG = MovieContentProvider.class.getSimpleName();
    private MovieDBHelper movieDBHelper;
    private UriMatcher mMatcher;

    @Override
    public boolean onCreate() {

        Log.v("", "in ContentProvider() onCreate");

        movieDBHelper = new MovieDBHelper(this.getContext());
        mMatcher = mMatcher();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor c;
        switch (mMatcher.match(uri)) {
            // All Movie selected
            case MOVIE: {
                c = movieDBHelper.getReadableDatabase()
                        .query(
                                MovieContract.MovieDBColumns.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder
                        );
                return c;
            }
            // Individual movie based on Id selected
            case MOVIE_WITH_ID: {
                c = movieDBHelper.getReadableDatabase()
                        .query(
                                MovieContract.MovieDBColumns.TABLE_NAME,
                                projection,
                                MovieContract.MovieDBColumns._ID + " = ?",
                                new String[]{String.valueOf(ContentUris.parseId(uri))},
                                null,
                                null,
                                sortOrder
                        );
                return c;
            }
            default: {
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        Log.v(LOG_TAG, "in getType()");
        return MovieContract.MovieDBColumns.CONTENT_DIR_TYPE;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri retUri;
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        switch (mMatcher.match(uri)) {
            case MOVIE: {
                long _id = db.insert(MovieContract.MovieDBColumns.TABLE_NAME, null, values);

                // insert unless it is already contained in the database
                if (_id > 0) {
                    retUri = MovieContract.MovieDBColumns.getContentUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        int numDeleted;

        switch (mMatcher.match(uri)) {
            case MOVIE:
                numDeleted = db.delete(
                        MovieContract.MovieDBColumns.TABLE_NAME,
                        MovieContract.MovieDBColumns.COL_MID + " = ?",
                        selectionArgs
                );

                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + MovieContract.MovieDBColumns.TABLE_NAME + "'");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }


    /**
     * UriMatcher
     */
    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 200;

    private static UriMatcher mMatcher() {
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, MovieContract.MovieDBColumns.TABLE_NAME, MOVIE);
        matcher.addURI(authority, MovieContract.MovieDBColumns.TABLE_NAME + "/#", MOVIE_WITH_ID);

        return matcher;
    }
}
