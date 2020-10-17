package com.appdev_soumitri.humbirds.services;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomLLManager extends LinearLayoutManager {

    private Context context;
    private MyErrorListener myErrorListener;

    public CustomLLManager(Context context, MyErrorListener myErrorListener) {
        super(context);
        this.context = context;
        this.myErrorListener = myErrorListener;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            myErrorListener.exceptionCaught();
            Log.e("TAG", "exception occurred");
        }
    }

    public interface MyErrorListener {
        void exceptionCaught();
    }
}
