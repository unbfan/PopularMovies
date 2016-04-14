package com.bigfacestusio.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = MovieDBHelper.class.getSimpleName();

    public static final String DB_NAME = "movies.db";
    public static final int DB_VERSION = 6;

    public MovieDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieDBColumns.TABLE_NAME + "(" +
                MovieContract.MovieDBColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieDBColumns.COL_MID + " TEXT UNIQUE, " +
                MovieContract.MovieDBColumns.COL_TITLE + " TEXT , " +
                MovieContract.MovieDBColumns.COL_POSTER_PATH + " TEXT , " +
                MovieContract.MovieDBColumns.COL_OVERVIEW + " TEXT , " +
                MovieContract.MovieDBColumns.COL_RELEASE_DATE + " TEXT , " +
                MovieContract.MovieDBColumns.COL_POPULARITY + " TEXT , " +
                MovieContract.MovieDBColumns.COL_VOTE_AVERAGE + " TEXT );";
//                MovieContract.MovieDBColumns.COL_TRAILERS + " TEXT , " +
//                MovieContract.MovieDBColumns.COL_IS_FAVORITE + " INTEGER " +


//        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieDBColumns.TABLE_NAME + "(" +
//                MovieContract.MovieDBColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                MovieContract.MovieDBColumns.COL_MID + " TEXT UNIQUE NOT NULL, " +
//                MovieContract.MovieDBColumns.COL_TITLE + " TEXT NOT NULL, " +
//                MovieContract.MovieDBColumns.COL_POSTER_PATH + " TEXT NOT NULL, " +
//                MovieContract.MovieDBColumns.COL_OVERVIEW + " TEXT NOT NULL, " +
//                MovieContract.MovieDBColumns.COL_RELEASE_DATE + " TEXT NOT NULL, " +
//                MovieContract.MovieDBColumns.COL_POPULARITY + " TEXT NOT NULL, " +
//                MovieContract.MovieDBColumns.COL_TRAILERS + " TEXT NOT NULL, " +
//                MovieContract.MovieDBColumns.COL_IS_FAVORITE + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ". OLD DATA WILL BE DESTROYED");

        // Drop the table
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieDBColumns.TABLE_NAME);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + MovieContract.MovieDBColumns.TABLE_NAME + "'");

        // re-create database
        onCreate(db);
    }
}
