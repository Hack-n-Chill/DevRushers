package com.appdev_soumitri.humbirds.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkConnection {

    private Context context;
    public NetworkConnection(Context context) {
        this.context =context;
    }

    public boolean isConnected() {
        boolean isAvailable=false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo network=manager.getActiveNetworkInfo();
        if(network!=null && network.isConnected()) isAvailable=true;
        Log.d("Network ",Boolean.toString(isAvailable));
        return isAvailable;
    }
}
