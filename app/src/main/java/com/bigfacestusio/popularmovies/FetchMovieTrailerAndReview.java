package com.bigfacestusio.popularmovies;


import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;


public class FetchMovieTrailerAndReview extends AsyncTask<String, Void, Void> {
    private static final String LOG_TAG = FetchMovieData.class.getSimpleName();

    private DetailActivityFragment detailActivityFragment;
    private Movie movie;
    private String movieId;

    private Vector<String> trailers;
    private Vector<String> reviews;


    public FetchMovieTrailerAndReview(DetailActivityFragment daf, Movie movie) {
        this.detailActivityFragment = daf;
        this.movie = movie;

    }

    public static final String API_KEY_THEMOVIEDB = FetchMovieData.API_KEY_THEMOVIEDB;



    @Override
    protected Void doInBackground(String... params) {

        movieId = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            URL[] urls = this.buildQueryUrl();
            String[] jsonStr = new String[urls.length];

            for (int i = 0; i < urls.length; i++) {

                // Create the request to themoviedb.org, and open the connection
                urlConnection = (HttpURLConnection) urls[i].openConnection();
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

                jsonStr[i] = buffer.toString();
            }

            //Process Json str 
            processJsonStrArr(jsonStr);


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
     * Prepare Json query URLs
     * @return
     * @throws MalformedURLException
     */
    private URL[] buildQueryUrl() throws MalformedURLException {
         /*
            https://api.themoviedb.org/3/movie/209112/videos?api_key=7270647d5fdd0d62866171c01325d95e
            https://api.themoviedb.org/3/movie/209112/reviews?api_key=7270647d5fdd0d62866171c01325d95e
            */
        URL[] urls = new URL[2];
        String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/" + this.movieId + "/videos?";
        String APPID_PARAM = "api_key";

        Uri buildUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(APPID_PARAM, API_KEY_THEMOVIEDB)
                .build();

        urls[0] = new URL(buildUri.toString());


        // URL for reviews
        MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/" + this.movieId + "/reviews?";
        APPID_PARAM = "api_key";

        buildUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(APPID_PARAM, API_KEY_THEMOVIEDB)
                .build();

        urls[1] = new URL(buildUri.toString());

        return urls;
    }


    private void processJsonStrArr(String[] jsonStr) throws JSONException {
        this.getTrailerDataFromJson(jsonStr[0]);
        this.getReviewDataFromJson(jsonStr[1]);
    }

    /**
     * Process Trailer Json string
     * @param jsonStr
     * @throws JSONException
     */
    private void getTrailerDataFromJson(String jsonStr) throws JSONException {
        JSONObject movieJson = new JSONObject(jsonStr);
        JSONArray resultArray = movieJson.getJSONArray("results"); // get the result node

        trailers = new Vector<>();
        for (int i = 0; i < resultArray.length(); i++) {
            JSONObject movie = resultArray.getJSONObject(i);
            String trailer_key = movie.getString("key");
            trailers.add(trailer_key);
        }
    }

    /**
     * Process Review Json string
     * @param jsonStr
     * @throws JSONException
     */
    private void getReviewDataFromJson(String jsonStr) throws JSONException {
        JSONObject movieJson = new JSONObject(jsonStr);
        JSONArray resultArray = movieJson.getJSONArray("results"); // get the result node

        reviews = new Vector<>();
        for (int i = 0; i < resultArray.length(); i++) {
            JSONObject movie = resultArray.getJSONObject(i);
            String author = movie.getString("author");
            String content = movie.getString("content");
            String url = movie.getString("url");

            reviews.add(author + "\n" + content + "\n=======================");
        }


    }


    @Override
    protected void onPostExecute(Void aVoid) {

        movie.setTrailers(trailers);
//        movie.setReviews((String[]) reviews.toArray());

        detailActivityFragment.updateTrailerListView(trailers);
        detailActivityFragment.updateReviewListView(reviews);

    }

}//End of class

