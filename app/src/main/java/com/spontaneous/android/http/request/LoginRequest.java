package com.spontaneous.android.http.request;

import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.User;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by eidan on 5/23/15.
 */
public interface LoginRequest {

    @POST("/users-api/login")
    void login(@Body FacebookLoginRequestModel loginRequestModel, Callback<BaseResponse<User>> cb);

}
