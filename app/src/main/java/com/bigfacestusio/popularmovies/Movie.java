package com.bigfacestusio.popularmovies;

/**
 * Query: https://api.themoviedb.org/3/configuration?api_key=7270647d5fdd0d62866171c01325d95e
 * {
 * "images": {
 * "base_url": "http:\/\/image.tmdb.org\/t\/p\/",
 * "secure_base_url": "https:\/\/image.tmdb.org\/t\/p\/",
 * "backdrop_sizes": [
 * "w300",
 * "w780",
 * "w1280",
 * "original"
 * ],
 * "logo_sizes": [
 * "w45",
 * "w92",
 * "w154",
 * "w185",
 * "w300",
 * "w500",
 * "original"
 * ],
 * "poster_sizes": [
 * "w92",
 * "w154",
 * "w185",
 * "w342",
 * "w500",
 * "w780",
 * "original"
 * ],
 * "profile_sizes": [
 * "w45",
 * "w185",
 * "h632",
 * "original"
 * ],
 * "still_sizes": [
 * "w92",
 * "w185",
 * "w300",
 * "original"
 * ]
 * },
 * "change_keys": [
 * ...
 * ]
 * }
 */
public class Movie {

    public static final String KEY_POSTER_PATH = "poster_path";
    public static final String KEY_OVERVIEW = "overview";
    public static final String KEY_RELEASE_DATE = "release_date";
    public static final String KEY_ID = "id";
    public static final String KEY_ORIGINAL_TITLE = "original_title";
    public static final String KEY_TITLE = "title";
    public static final String KEY_BACKDROP_PATH = "backdrop_path";
    public static final String KEY_POPULARITY = "popularity";
    public static final String KEY_VOTE_COUNT = "vote_count";
    public static final String KEY_VOTE_AVERAGE = "vote_average";

    //    public static final String BASE_URL_MOVICE_POSTER="http://image.tmdb.org/t/p/w92";
    public static final String BASE_URL_MOVICE_POSTER = "http://image.tmdb.org/t/p/w342";
    public static final String BASE_URL_MOVICE_BACKDROP = "http://image.tmdb.org/t/p/w780";

    private String poster_path;
    private String overview;
    private String release_date;
    private String id;
    private String original_title;
    private String title;
    private String backdrop_path;
    private String popularity;
    private String vote_count;
    private String vote_average;

    public Movie(String poster_path,
                 String overview,
                 String release_date,
                 String id,
                 String original_title,
                 String title,
                 String backdrop_path,
                 String popularity,
                 String vote_count,
                 String vote_average
    ) {
        this.poster_path = poster_path;

        this.overview = overview;
        this.release_date = release_date;
        this.id = id;
        this.original_title = original_title;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
    }


    public String getPosterImagePath() {
        return BASE_URL_MOVICE_POSTER + poster_path;
    }
    public String getBackdropImagePath() {
        return BASE_URL_MOVICE_BACKDROP + backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public String getId() {
        return id;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public String getTitle() {
        return title;
    }



    public String getPopularity() {
        return popularity;
    }

    public String getVoteCount() {
        return vote_count;
    }

    public String getVoteAverage() {
        return vote_average;
    }


}
