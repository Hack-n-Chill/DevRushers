package com.appdev_soumitri.humbirds.services;

import com.appdev_soumitri.humbirds.Urls;
import com.appdev_soumitri.humbirds.models.SongModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// this is interface for various queries to be made
// sample interface for hands on

public interface SCService {
    @GET("/tracks?&limit=100&filter=public&client_id=" + Urls.CLIENT_ID)
    Call<List<SongModel>> getBestTracks(@Query("most%20liked%20songs") String date);
}
