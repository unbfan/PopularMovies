package com.bigfacestusio.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;


public class FetchMovieData extends AsyncTask<ArrayAdapter, Void, Vector<Movie>> {
    private static final String LOG_TAG = FetchMovieData.class.getSimpleName();


    //Add the movie db API key here...
    public static final String API_KEY_THEMOVIEDB = "";


    private ArrayAdapter mArrayAdapter;

    @Override
    protected Vector<Movie> doInBackground(ArrayAdapter... params) {
        this.mArrayAdapter = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;
        Context ct = mArrayAdapter.getContext();

        // get sort-by setting from preference
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ct);
        String sortbyStr = pref.getString(ct.getString(R.string.pref_sortby_key), ct.getString(R.string.pref_sortby_most_popular_searchStr));

        try {
            //https://api.themoviedb.org/3/discover/movie?api_key=7270647d5fdd0d62866171c01325d95e&sort_by=popularity.desc
            final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
            final String APPID_PARAM = "api_key";
            final String SORTBY_PARAM = "sort_by";

            Uri buildUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(APPID_PARAM, API_KEY_THEMOVIEDB)
                    .appendQueryParameter(SORTBY_PARAM, sortbyStr)
                    .build();

            URL url = new URL(buildUri.toString());

            // Create the request to themoviedb.org, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            movieJsonStr = buffer.toString();
//            Log.v(LOG_TAG, "Forecast JSON String: " + movieJsonStr);

            //return a vector of Movie Objects
            return getMovieDataFromJson(movieJsonStr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * Paring JSON String
     *
     * @param jsonMovieStr
     * @return a Vector that holds all Movie objects
     * @throws JSONException
     */
    private Vector getMovieDataFromJson(String jsonMovieStr) throws JSONException {
        JSONObject movieJson = new JSONObject(jsonMovieStr);
        JSONArray resultArray = movieJson.getJSONArray("results"); // get the result node

        Vector<Movie> movieInfo = new Vector(); //store info for each movie

        for (int i = 0; i < resultArray.length(); i++) {
            JSONObject movie = resultArray.getJSONObject(i);

            String poster_path = movie.getString(Movie.KEY_POSTER_PATH); // "poster_path": "\/w93GAiq860UjmgR6tU9h2T24vaV.jpg",
            String overview = movie.getString(Movie.KEY_OVERVIEW); //"overview": "With the nation of Panem in a full scale war, Katniss confronts President Snow in the final showdown. Teamed with a group of her closest friends \u2013 including Gale, Finnick, and Peeta \u2013 Katniss goes off on a mission with the unit from District 13 as they risk their lives to stage an assassination attempt on President Snow who has become increasingly obsessed with destroying her. The mortal traps, enemies, and moral choices that await Katniss will challenge her more than any arena she faced in The Hunger Games.",
            String release_date = movie.getString(Movie.KEY_RELEASE_DATE);//"release_date": "2015-11-18",
            String id = movie.getString(Movie.KEY_MID);//"id": 131634,
            String original_title = movie.getString(Movie.KEY_ORIGINAL_TITLE);//"original_title": "The Hunger Games: Mockingjay - Part 2",
            String title = movie.getString(Movie.KEY_TITLE);//"title": "The Hunger Games: Mockingjay - Part 2",
            String backdrop_path = movie.getString(Movie.KEY_BACKDROP_PATH);//"backdrop_path": "\/qjn3fzCAHGfl0CzeUlFbjrsmu4c.jpg",
            String popularity = movie.getString(Movie.KEY_POPULARITY);//"popularity": 37.001894,
            String vote_count = movie.getString(Movie.KEY_VOTE_COUNT);//"vote_count": 1309,
            String vote_average = movie.getString(Movie.KEY_VOTE_AVERAGE);//"vote_average": 6.8

            Movie m = new Movie(poster_path, overview, release_date, id, original_title, title, backdrop_path, popularity, vote_count, vote_average);
            movieInfo.add(m);
        }
        return movieInfo;
    }


    /**
     * Update UI once doInBackground() is done
     *
     * @param allMovies
     */
    @Override
    protected void onPostExecute(Vector<Movie> allMovies) {
        if (allMovies != null) {
            if (allMovies.size() > 0) {
                mArrayAdapter.clear();
                mArrayAdapter.addAll(allMovies);
            }
        }
    }

}//End of class
