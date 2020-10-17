package com.appdev_soumitri.humbirds.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetRequest {
    private Context context;

    public GetRequest(Context context) {
        this.context=context;
    }

    public void callApiForArray(String url, final ResponseService responseService, int code, JSONObject jsonObject) {
        if (code == RequestCodes.GET) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                    url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    responseService.response(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    responseService.errorResponse(error);
                }
            });
            VolleySingletonHelper.getInstance(context).addToRequestQueue(jsonArrayRequest);
        }
    }

    public void callApiForObject(String url, final ResponseService responseService, int code, JSONObject jsonObject) {
        if (code == RequestCodes.GET) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    responseService.response(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    responseService.errorResponse(error);
                }
            });
            VolleySingletonHelper.getInstance(context).addToRequestQueue(jsonObjectRequest);
        }
    }


}
