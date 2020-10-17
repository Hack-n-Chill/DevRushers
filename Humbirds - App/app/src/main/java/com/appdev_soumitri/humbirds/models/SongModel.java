package com.appdev_soumitri.humbirds.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class SongModel {

    @SerializedName("title")
    private String mTitle;

    @SerializedName("user_id")
    private long mArtistid;

    @SerializedName("duration")
    private long mDuration;

    @SerializedName("streamable")
    private boolean mStreamable;

    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("artwork_url")
    private String mArtworkURL;

    @SerializedName("genre")
    private String mGenre;

    @SerializedName("tag_list")
    private String mTagList;

    @SerializedName("id")
    private long id;

    @SerializedName("playback_count")
    private int playbackCount;

    @SerializedName("likes_count")
    private int likesCount;

    public long getArtistid() {
        return mArtistid;
    }

    public String getTagList() {
        return mTagList;
    }

    public String getGenre() {
        return mGenre;
    }

    public boolean isStreamable() {
        return mStreamable;
    }

    public String getTitle() {
        return mTitle;
    }

    public long getDuration() {
        return mDuration;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public String getArtworkURL() {
        return mArtworkURL;
    }

    public int getPlaybackCount() {
        return playbackCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public SongModel(JSONObject song) {
        try {
            this.id = song.getLong("id");
            this.mStreamable = song.getBoolean("streamable");
            this.mDuration = song.getLong("duration");
            this.mTitle = song.getString("title").trim();
            this.mGenre = song.getString("genre").trim();
            this.mArtworkURL = song.getString("artwork_url");
            this.mStreamURL = song.has("stream_url") ? song.getString("stream_url") : "";
            this.mArtistid = song.getLong("user_id");
            this.playbackCount = song.has("playback_count") ? song.getInt("playback_count") : 0;
            this.likesCount = song.has("likes_count") ? song.getInt("likes_count") : 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
