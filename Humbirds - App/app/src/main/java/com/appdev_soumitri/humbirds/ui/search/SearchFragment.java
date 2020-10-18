package com.appdev_soumitri.humbirds.ui.search;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.appdev_soumitri.humbirds.MainActivity;
import com.appdev_soumitri.humbirds.NoInternetActivity;
import com.appdev_soumitri.humbirds.R;
import com.appdev_soumitri.humbirds.Urls;
import com.appdev_soumitri.humbirds.models.SearchSongAdapter;
import com.appdev_soumitri.humbirds.models.SongModel;
import com.appdev_soumitri.humbirds.services.CustomLLManager;
import com.appdev_soumitri.humbirds.services.GetRequest;
import com.appdev_soumitri.humbirds.services.NetworkConnection;
import com.appdev_soumitri.humbirds.services.RequestCodes;
import com.appdev_soumitri.humbirds.services.RequestResponse;
import com.appdev_soumitri.humbirds.services.ResponseService;
import com.appdev_soumitri.humbirds.services.TrackRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import retrofit2.http.Url;

public class SearchFragment extends Fragment implements RequestResponse {



    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    private SearchViewModel mViewModel;

    private Context context;

    private List<SongModel> songs;

    private EditText etSearch;
    private ImageView ivSearchCross;
    private ListView rvSearchResult;
    private ProgressBar progressBar;
    private RelativeLayout rlNoSearchHistory;
    private CustomLLManager customLinearLayoutManager;
    private SearchSongAdapter searchSongAdapter;

    SharedPreferences sharedPreferences;
    ArrayList<String> history=new ArrayList<>();

    private Toolbar songToolbar;
    private TextView mSelectedTrackTitle;
    private ImageView mSelectedTrackImage;

    private MediaPlayer mMediaPlayer;
    private ImageView mPlayerControl;


    private TrackRequest trackRequest;

    public SearchFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();

        trackRequest = new TrackRequest(context,this);
        songs = new ArrayList<>();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);

        initialiseView(root);
        initListeners();

        searchSongAdapter=new SearchSongAdapter(context,songs);
        rvSearchResult.setAdapter(searchSongAdapter);

        rvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(!(new NetworkConnection(getContext()).isConnected())) {
                    startActivity(new Intent(getContext(), NoInternetActivity.class));
                }

                // play the song using MediaPlayer
                songToolbar.setVisibility(View.VISIBLE);

                SongModel track = songs.get(position);

                mSelectedTrackTitle.setText(track.getTitle());

                history.add(Long.toString(track.getId()));
                HashSet<String> hs=new HashSet<>(history);
                sharedPreferences.edit().putStringSet("song_id",hs).apply();

                Glide.with(requireActivity())
                        .load(track.getArtworkURL())
                        .into(mSelectedTrackImage);

                Log.d("Status","play song");

                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                }

                try {
                    mMediaPlayer.setDataSource(track.getStreamURL() + "?client_id=" + Urls.CLIENT_ID);
                    mMediaPlayer.prepareAsync();
                    mPlayerControl.setVisibility(View.VISIBLE);
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

    private void initListeners() {
        ivSearchCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.getText().clear();
                etSearch.requestFocus();
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if(!(new NetworkConnection(getContext()).isConnected())) {
                        startActivity(new Intent(getContext(), NoInternetActivity.class));
                    }

                    InputMethodManager imgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imgr != null) {
                        imgr.hideSoftInputFromWindow(getView().getRootView().getWindowToken(), 0);
                    }
                    etSearch.clearFocus();
                    performSearch(etSearch.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        mPlayerControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPause(0);
            }
        });

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
            }
        });

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

    private void performSearch(String query) {
        // perform the query
        if(!(new NetworkConnection(context)).isConnected()) {
            Toast.makeText(context, "No internet connection !", Toast.LENGTH_SHORT).show();
            return;
        }

        query=query.trim();
        if(query.length()>0) {
            progressBar.setVisibility(View.VISIBLE);

            final GetRequest request=new GetRequest(context);

            trackRequest.getTracksWithQuery(query,50);

        }
    }

    private void initialiseView(View view) {
        etSearch = view.findViewById(R.id.xetSearch);
        ivSearchCross = view.findViewById(R.id.ivSearchCross);
        rvSearchResult = view.findViewById(R.id.rvSearchResult);
        rvSearchResult.setAdapter(searchSongAdapter);
        progressBar = view.findViewById(R.id.search_progressbar);
        rvSearchResult = view.findViewById(R.id.rvSearchResult);
        songToolbar=view.findViewById(R.id.songToolbar);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mSelectedTrackImage=view.findViewById(R.id.selected_track_image);
        mSelectedTrackTitle=view.findViewById(R.id.selected_track_title);
        mPlayerControl=view.findViewById(R.id.player_control);
        sharedPreferences = view.getContext().getSharedPreferences("com.appdev_soumitri.humbirds",Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("song_id",new HashSet<String>());
        if(!set.isEmpty()) history=new ArrayList<>(set);
    }

    @Override
    public void onListRequestSuccessful(ArrayList list, int check, boolean status) {
        if (check == RequestCodes.SEARCH_SONGS_WITH_QUERY && status)
        {
            songs.clear();
            rvSearchResult.setAdapter(searchSongAdapter);
            if(list!=null) songs.addAll(list);
            else {
                Toast.makeText(context, "No search results found :(", Toast.LENGTH_LONG).show();
            }
            // sort songs based on assumed parameter of being more relevant a result
            Collections.sort(songs, new Comparator<SongModel>() {
                @Override
                public int compare(SongModel s1, SongModel s2) {
                    if (s2.getLikesCount() > s1.getLikesCount()) return 1;
                    else if (s2.getLikesCount() < s1.getLikesCount()) return -1;
                    else return 0;
                }
            });
            searchSongAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onObjectRequestSuccessful(Object object, int check, boolean status) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        // TODO: Use the ViewModel
    }

}