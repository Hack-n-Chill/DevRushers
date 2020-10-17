package com.appdev_soumitri.humbirds.services;

import android.content.Context;

import com.android.volley.VolleyError;
import com.appdev_soumitri.humbirds.Urls;
import com.appdev_soumitri.humbirds.models.SongModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TrackRequest extends GetRequest {
    RequestResponse requestResponse;
    Context context;
    public TrackRequest(Context context,RequestResponse requestResponse) {
        super(context);
        this.requestResponse=requestResponse;
    }
    public void getTracksWithQuery(String query, int limit) {

        String url = Urls.apiTracksQuery(query.toCharArray(),50);

        callApiForArray(url, new ResponseService() {
            @Override
            public void response(Object response) {
                JSONArray jsonArrayResponse = (JSONArray) response;
                ArrayList<SongModel> songs = new ArrayList<>();
                if (jsonArrayResponse.length() > 0) {
                    for (int i = 0; i < jsonArrayResponse.length(); i++) {
                        try {
                            SongModel song = new SongModel(jsonArrayResponse.getJSONObject(i));
                            songs.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                requestResponse.onListRequestSuccessful(songs, RequestCodes.SEARCH_SONGS_WITH_QUERY, true);
            }

            @Override
            public void stringResponse(String response) {

            }

            @Override
            public void errorResponse(VolleyError error) {
                requestResponse.onListRequestSuccessful(null, RequestCodes.SEARCH_SONGS_WITH_QUERY, false);
            }
        }, RequestCodes.GET, null);

    }

}
