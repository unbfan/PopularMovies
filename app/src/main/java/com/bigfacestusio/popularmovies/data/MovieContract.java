package com.bigfacestusio.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.bigfacestudio.movies.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class MovieDBColumns implements BaseColumns {

        public static final String TABLE_NAME = "movies";

        public static final String COL_MID = "mid";
        public static final String COL_TITLE = "title";
        public static final String COL_POSTER_PATH = "poster_path";
        public static final String COL_OVERVIEW = "overview";
        public static final String COL_RELEASE_DATE = "release_date";
        public static final String COL_POPULARITY = "popularity";
        public static final String COL_VOTE_AVERAGE = "vote_average";
        public static final String COL_IS_FAVORITE = "is_Favorite";
        public static final String COL_TRAILERS = "trailers";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;


        public static Uri getContentUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
