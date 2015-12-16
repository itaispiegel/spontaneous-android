package com.spontaneous.android.http.request;

import com.spontaneous.android.http.request.model.FacebookLoginRequest;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.User;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Login request.
 */
public interface LoginService {

    @POST("/API/users/login")
    void login(@Body FacebookLoginRequest loginRequestModel, Callback<BaseResponse<User>> cb);

}
