package com.spontaneous.android.http.request;

import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Ride;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by eidan on 5/24/15.
 */
public interface CreateRideRequest {

    @POST("/ride/create_ride")
    void createRide(@Body CreateRideRequestModel createRideRequestModel,
                    Callback<BaseResponse<Ride>> callback);
}
