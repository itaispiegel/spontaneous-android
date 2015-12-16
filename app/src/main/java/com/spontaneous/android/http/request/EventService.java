package com.spontaneous.android.http.request;

import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Event;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * REST API for events operations.
 */
public interface EventService {

    @POST("/API/events")
    void createEvent(@Body Event event, Callback<BaseResponse<Event>> cb);

}
