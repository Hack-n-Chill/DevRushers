package com.appdev_soumitri.humbirds.services;

import java.util.ArrayList;

public interface RequestResponse {
    void onListRequestSuccessful(ArrayList list, int check, boolean status);

    void onObjectRequestSuccessful(Object object, int check, boolean status);
}
