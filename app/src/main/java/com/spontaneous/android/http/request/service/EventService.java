package com.spontaneous.android.http.request.service;

import com.spontaneous.android.http.request.model.SaveEventRequest;
import com.spontaneous.android.http.request.model.UpdateInvitedUserRequest;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.model.InvitedUser;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Query;

/**
 * REST API for events operations.
 */
public interface EventService {

    /**
     * Create a new event in the server.
     *
     * @param saveEventRequest Request entity to create event.
     * @param cb               Callback of the request.
     */
    @POST("/API/events")
    void createEvent(@Body SaveEventRequest saveEventRequest, Callback<BaseResponse<Event>> cb);

    /**
     * Edit an existing event.
     *
     * @param saveEventRequest Request entity to edit event.
     * @param cb               Callback of the request.
     */
    @PUT("/API/events")
    void editEvent(@Query("id") long id, @Body SaveEventRequest saveEventRequest, Callback<BaseResponse<Event>> cb);

    /**
     * Get all the events related to the given user.
     *
     * @param id Of the user.
     * @param cb Callback of the request.
     */
    @GET("/API/events/getUserEvents")
    void getUserEvents(@Query("id") long id, Callback<BaseResponse<List<Event>>> cb);

    /**
     * Delete an event by it's id.
     *
     * @param id Of the event.
     * @param cb Callback of the request.
     */
    @DELETE("/API/events/delete")
    void deleteEvent(@Query("id") long id, Callback<BaseResponse> cb);

    /**
     * Update an {@link InvitedUser}.
     *
     * @param id Id of the {@link InvitedUser}.
     * @param cb Callback of the request.
     */
    @PUT("/API/events/updateInvitedUser")
    void updateInvitedUser(@Query("id") long id, @Body UpdateInvitedUserRequest updateRequest, Callback<BaseResponse> cb);

}
