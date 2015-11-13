package com.spontaneous.android.http.request;

import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Ride;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by eidan on 5/23/15.
 */
public interface GetAllRides {

    @GET("/ride/get_all_rides")
    void getAllRides(Callback<BaseResponse<List<Ride>>> cb);
}
