package com.appdev_soumitri.humbirds.ui.search;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private TrackRequest trackRequest;

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

                // play the song using MediaPlayer

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
        rlNoSearchHistory = view.findViewById(R.id.rlNoSearchHistory);
        rvSearchResult = view.findViewById(R.id.rvSearchResult);
    }

    @Override
    public void onListRequestSuccessful(ArrayList list, int check, boolean status) {
        if (check == RequestCodes.SEARCH_SONGS_WITH_QUERY && status)
        {
            songs.clear();
            rvSearchResult.setAdapter(searchSongAdapter);
            if(list!=null) songs.addAll(list);

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