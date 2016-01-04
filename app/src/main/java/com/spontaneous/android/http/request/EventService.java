package com.spontaneous.android.http.request;

import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Event;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * REST API for events operations.
 */
public interface EventService {

    @POST("/API/events")
    void createEvent(@Body Event event, Callback<BaseResponse<Event>> cb);

    @GET("/events/search/findByInvitedUser")
    void findUserEvents(@Query("user_id") long id, Callback<List<Event>> cb);

}
