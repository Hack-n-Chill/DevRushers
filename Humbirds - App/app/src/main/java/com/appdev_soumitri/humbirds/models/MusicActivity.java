package com.appdev_soumitri.humbirds.models;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.appdev_soumitri.humbirds.R;
import com.appdev_soumitri.humbirds.Urls;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

public class MusicActivity extends AppCompatActivity {

    private PlayerView playerView;
    private SimpleExoPlayer exoPlayer;

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        ActionBar actionBar=getSupportActionBar();
        assert actionBar!=null;
        actionBar.hide();

        playerView=findViewById(R.id.exo_view);


    }
//    private void initializePlayer() {
//        exoPlayer=new SimpleExoPlayer.Builder(this).build();
//        playerView.setPlayer(exoPlayer);
//
//        MediaItem item=MediaItem.fromUri("https://api.soundcloud.com/tracks/137898342/stream?client_id=L769F50feEmlOI4zzWlSaA4DWuQfPmc8");
//        exoPlayer.setMediaItem(item);
//
//        exoPlayer.setPlayWhenReady(playWhenReady);
//        exoPlayer.seekTo(currentWindow, playbackPosition);
//        exoPlayer.prepare();
//
//    }
//
//    @SuppressLint("InlinedApi")
//    private void hideSystemUi() {
//        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//    }
//
//    private void releasePlayer() {
//        if (exoPlayer != null) {
//            playWhenReady = exoPlayer.getPlayWhenReady();
//            playbackPosition = exoPlayer.getCurrentPosition();
//            currentWindow = exoPlayer.getCurrentWindowIndex();
//            exoPlayer.release();
//            exoPlayer = null;
//        }
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (Util.SDK_INT >= 24) {
//            initializePlayer();
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        // hideSystemUi();
//        if ((Util.SDK_INT < 24 || exoPlayer == null)) {
//            initializePlayer();
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (Util.SDK_INT < 24) {
//            releasePlayer();
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (Util.SDK_INT >= 24) {
//            releasePlayer();
//        }
//    }

}