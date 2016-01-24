package com.spontaneous.android.http.request.service;

import com.spontaneous.android.http.request.model.CreateEventRequest;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Event;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * REST API for events operations.
 */
public interface EventService {

    /**
     * Create a new event in the server.
     * @param createEventRequest Request entity to create event.
     */
    @POST("/API/events")
    void createEvent(@Body CreateEventRequest createEventRequest, Callback<BaseResponse<Event>> cb);

    /**
     * Get all the events related to the given user.
     * @param id Of the user.
     */
    @GET("/API/events/getUserEvents")
    void getUserEvents(@Query("id") long id, Callback<BaseResponse<List<Event>>> cb);

    /**
     * Delete an event by it's id.
     * @param id Of the event.
     */
    @DELETE("/API/events/delete")
    void deleteEvent(@Query("id") long id, Callback<BaseResponse> cb);

}
