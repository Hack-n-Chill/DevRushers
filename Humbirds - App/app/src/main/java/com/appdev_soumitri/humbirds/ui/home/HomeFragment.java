package com.appdev_soumitri.humbirds.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.appdev_soumitri.humbirds.MainActivity;
import com.appdev_soumitri.humbirds.NoInternetActivity;
import com.appdev_soumitri.humbirds.R;
import com.appdev_soumitri.humbirds.models.SCTrackAdapter;
import com.appdev_soumitri.humbirds.services.NetworkConnection;
import com.appdev_soumitri.humbirds.services.RecentTrackService;
import com.appdev_soumitri.humbirds.Urls;
import com.appdev_soumitri.humbirds.models.SongModel;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private List<SongModel> mListItems;
    private SCTrackAdapter mAdapter;

    private Toolbar songToolbar;

    private TextView mSelectedTrackTitle;
    private ImageView mSelectedTrackImage;

    private MediaPlayer mMediaPlayer;
    private ImageView mPlayerControl;

    private DatabaseReference reference;
    private FirebaseUser user;

    private HomeViewModel homeViewModel;

    SharedPreferences sharedPreferences;

    public HomeFragment() {}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initDB();

        songToolbar=root.findViewById(R.id.songToolbar);

        sharedPreferences=root.getContext().getSharedPreferences("com.appdev_soumitri_humbirds", Context.MODE_PRIVATE);

        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("song_id",new HashSet<String>());

        // To do: use SharedPreferences to keep track of play history and show those songs in HomeFragment

        // sample trial of API usage using Retrofit2

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.API_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecentTrackService recentTrackService = retrofit.create(RecentTrackService.class);

        recentTrackService.getBestTracks().enqueue(new Callback<List<SongModel>>() {
            @Override
            public void onResponse(Call<List<SongModel>> call, Response<List<SongModel>> response) {
                if (response.isSuccessful()) {
                    List<SongModel> tracks = response.body();

                    showTrackList(tracks);

                } else {
                    Log.d("Response: ","Unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<List<SongModel>> call, Throwable t) {
                Log.d("Failed: ","json response cannot be fetched");
            }
        });

        // setting up music player with MediaPlayer

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                togglePlayPause(1);
            }
        });


        // setting up adapter and play song functionality

        // Have used MediaPlayer as of now, will be looking into ExoPlayer API

        mSelectedTrackImage=root.findViewById(R.id.selected_track_image);
        mSelectedTrackTitle=root.findViewById(R.id.selected_track_title);

        mPlayerControl = root.findViewById(R.id.player_control);
        mPlayerControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPause(0);
            }
        });

        mListItems = new ArrayList<>();
        ListView listView = (ListView)root.findViewById(R.id.track_list_view);
        mAdapter = new SCTrackAdapter(getContext(), mListItems);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(!(new NetworkConnection(getContext()).isConnected())) {
                    startActivity(new Intent(getContext(), NoInternetActivity.class));
                }


                songToolbar.setVisibility(View.VISIBLE);

                // song clicked, we have to play song
                SongModel track = mListItems.get(position);

                mSelectedTrackTitle.setText(track.getTitle());

                Glide.with(requireActivity())
                        .load(track.getArtworkURL())
                        .into(mSelectedTrackImage);

                Log.d("Status","play song");

                updateDB(track);

                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                }

                try {
                    mMediaPlayer.setDataSource(track.getStreamURL() + "?client_id=" + Urls.CLIENT_ID);
                    mMediaPlayer.prepareAsync();
                    if(mMediaPlayer.isPlaying()) {
                        togglePlayPause(1);
                    }
                    Log.d("Music started:","true");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        return root;
    }

    private void updateDB(SongModel track) {
        // update current song info into firebase
    }

    private void initDB() {
        // initialise firebase RtDB operations
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String uid=user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users");

    }

    private void togglePlayPause(int in) {
        if(in==1) {
            mPlayerControl.setImageResource(R.drawable.ic_pause);
            return;
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPlayerControl.setImageResource(R.drawable.ic_play);
        } else {
            mMediaPlayer.start();
            mPlayerControl.setImageResource(R.drawable.ic_pause);
        }
    }

    private void showTrackList(List<SongModel> tracks) {
        mListItems.clear();
        mListItems.addAll(tracks);
        mAdapter.notifyDataSetChanged();
        Log.d("Status","Songs displayed in adapter");
    }
}