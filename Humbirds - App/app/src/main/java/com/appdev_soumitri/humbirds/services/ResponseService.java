package com.appdev_soumitri.humbirds.services;

import com.android.volley.VolleyError;

public interface ResponseService {
    void response(Object response);
    void stringResponse(String response);
    void errorResponse(VolleyError response);
}
