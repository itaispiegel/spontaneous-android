package com.spontaneous.android.http.request.service;

import com.spontaneous.android.http.request.model.FacebookLoginRequest;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.User;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * REST API for login requests.
 */
public interface LoginService {

    /**
     * Login a user.
     * @param loginRequestModel A model representing login data - Facebook user id and token.
     */
    @POST("/API/users/login")
    void login(@Body FacebookLoginRequest loginRequestModel, Callback<BaseResponse<User>> cb);

}
