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

    /**
     * Create a new event in the server.
     * @param event To create.
     */
    @POST("/API/events")
    void createEvent(@Body Event event, Callback<BaseResponse<Event>> cb);

    /**
     * Get all the events related to the given user.
     * @param id Of the user.
     */
    @GET("/API/events/getUserEvents")
    void getUserEvents(@Query("user_id") long id, Callback<BaseResponse<List<Event>>> cb);

}
