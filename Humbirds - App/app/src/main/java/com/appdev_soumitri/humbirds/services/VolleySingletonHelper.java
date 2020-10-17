package com.appdev_soumitri.humbirds.services;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingletonHelper {

    @SuppressLint("StaticFieldLeak")
    private static VolleySingletonHelper mInstance;

    private Context context;
    private RequestQueue mRequestQueue;

    private VolleySingletonHelper(Context context) {
        this.context = context;

    }

    public static synchronized VolleySingletonHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingletonHelper(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}
