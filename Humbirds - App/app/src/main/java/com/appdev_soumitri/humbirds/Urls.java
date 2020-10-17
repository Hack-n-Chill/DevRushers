package com.appdev_soumitri.humbirds;

import android.util.Log;

import java.util.Arrays;

public class Urls {

    public static final String API_BASE = "https://api.soundcloud.com";
    public static final String API_URL = "https://api.soundcloud.com/resolve?url=";
    public static final String CLIENT_ID = "L769F50feEmlOI4zzWlSaA4DWuQfPmc8";
    public static final String TRACKS = "http://api.soundcloud.com/tracks";
    public static final String PLAYLISTS = "http://api.soundcloud.com/playlists";
    public static final String ARTISTS = "http://api.soundcloud.com/users";

    public static String apiTracksQuery(char[] query, int limit) {
        StringBuffer encodedQuery=new StringBuffer("");
        for(int i=0;i<query.length;i++)
        {
            if(query[i]==' ') encodedQuery.append("%20");
            else encodedQuery.append(query[i]);
        }
        Log.d("Initial:", Arrays.toString(query));
        Log.d("Final:",encodedQuery.toString());
        return Urls.TRACKS + "?&q=" + encodedQuery.toString().trim() + "&limit=" + limit + "&filter=public&client_id=" + Urls.CLIENT_ID;
    }

}
