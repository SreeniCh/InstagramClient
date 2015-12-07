package com.material.android.sreeni.instagramclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import android.widget.ListView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {
    public static final String TAG = "PhotosActivity_LOG";
    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        photos = new ArrayList<>();
        // 1. Create an adapter linking to the source
        aPhotos = new InstagramPhotosAdapter(this, photos);
        // 2. Find ListView
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        // 3. bind
        lvPhotos.setAdapter(aPhotos);

        // send n/w request for photos
        fetchPopularPhotos();

        // Initialize SwipeRefreshLayout
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPopularPhotos();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    private void fetchPopularPhotos() {
        /*
        Cient ID: 18083f3bf60340c18b59020515b1a1fd
        - Popular: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
         */

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        // Create the network client
        AsyncHttpClient client = new AsyncHttpClient();
        // Trigger the GET request
        client.get(url, null, new JsonHttpResponseHandler() {
            // onSuccess (worked, 200)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("DEBUG", response.toString());

                JSONArray photosJSON = null;
                aPhotos.clear();

                try {
                    photosJSON = response.getJSONArray("data"); // array of objects
                    Log.i(TAG, "Size = " + photosJSON.length());
                    //iterate array of posts
                    for (int i = 0; i < photosJSON.length(); ++i) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);

                        InstagramPhoto photo = new InstagramPhoto();
                        // Author Name: { "data" => [x] => "user" => "username" }
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        // Caption: { "data" => [x] => "caption" => "text" }
                        photo.caption = photoJSON.getJSONObject("caption").getString("text").toString();
                        // Type: { "data" => [x] => "type" } ("image" or "video")
                        photo.type = photoJSON.getString("type");
                        // URL: { "data" => [x] => "images" => "standard_resoulution" => "url" }
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photo.profilePicUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        photos.add(photo);

                        Log.i(TAG, "Username: " + photoJSON.getJSONObject("user").getString("username"));
                        //Log.i(TAG, "Caption: " + photoJSON.getJSONObject("caption").getString("text"));
                        Log.i(TAG, "Type: " + photoJSON.getString("type"));
                        Log.i(TAG, "imageUrl: " + photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
                        Log.i(TAG, "imageHeight: " + photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height"));
                        Log.i(TAG, "likesCount: " + photoJSON.getJSONObject("likes").getInt("count"));
                        Log.i(TAG, "====================================");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                aPhotos.notifyDataSetChanged();
            }

            //onFailure

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
                //DO SOMETHING
            }
        });

    }
}
