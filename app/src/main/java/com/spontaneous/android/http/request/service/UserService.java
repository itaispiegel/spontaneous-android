package com.spontaneous.android.http.request.service;

import com.spontaneous.android.http.response.BaseResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * REST API for user operations.
 */
public interface UserService {

    @GET("/API/users/updateGCM")
    void updateGcmToken(@Query("id") long id, @Query("token") String token, Callback<BaseResponse> cb);

}
