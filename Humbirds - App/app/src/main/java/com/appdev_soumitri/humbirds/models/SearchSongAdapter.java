package com.appdev_soumitri.humbirds.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdev_soumitri.humbirds.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class SearchSongAdapter extends BaseAdapter {

    private Context mContext;
    private List<SongModel> mTracks;

    public SearchSongAdapter(Context context, List<SongModel> tracks) {
        mContext = context;
        mTracks = tracks;
    }

    @Override
    public int getCount() {
        return mTracks.size();
    }

    @Override
    public SongModel getItem(int position) {
        return mTracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SongModel track = getItem(position);

        SearchSongAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.track_list_row, parent, false);
            holder = new SearchSongAdapter.ViewHolder();
            holder.trackImageView = (ImageView) convertView.findViewById(R.id.track_image);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.track_title);
            convertView.setTag(holder);
        } else {
            holder = (SearchSongAdapter.ViewHolder) convertView.getTag();
        }

        holder.titleTextView.setText(track.getTitle());

        // Trigger the download of the URL asynchronously into the image view.

        Glide.with(mContext)
                .load(track.getArtworkURL())
                .into(holder.trackImageView);

        return convertView;
    }

    static class ViewHolder {
        ImageView trackImageView;
        TextView titleTextView;
    }
}
