package com.spontaneous.android.http.request.service;

import com.spontaneous.android.http.request.model.SaveEventRequest;
import com.spontaneous.android.http.request.model.UpdateGuestRequest;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.model.Guest;

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
     * Update an {@link Guest}.
     *
     * @param id Id of the {@link Guest}.
     * @param cb Callback of the request.
     */
    @PUT("/API/events/updateGuest")
    void updateGuest(@Query("id") long id, @Body UpdateGuestRequest updateRequest, Callback<BaseResponse> cb);

    /**
     * Send a broadcast message to a guest.
     *
     * @param id      Id of the guest.
     * @param message The message to broadcast.
     * @param cb      Callback of the request.
     */
    @POST("/API/events/notify")
    void notifyGuests(@Query("id") long id, @Body String message, Callback<Object> cb);

    /**
     * Assign an item to a guest.
     *
     * @param id    Id of the guest.
     * @param title Title of the item.
     * @param cb    Callback of the request.
     */
    @GET("/API/events/assign")
    void assignItem(@Query("id") long id, @Query("title") String title, Callback<BaseResponse> cb);

    /**
     * Delete an item given its id.
     *
     * @param id Given id of the item.
     * @param cb Callback of the request.
     */
    @DELETE("/API/events/item")
    void deleteItem(@Query("id") long id, Callback<BaseResponse> cb);

    /**
     * Update an item given its id.
     *
     * @param id         Given id of the item.
     * @param isBringing Whether the guest is bringing the item.
     * @param cb         Callback of the request.
     */
    @PUT("/API/events/item")
    void updateItem(@Query("id") long id, @Query("bringing") boolean isBringing, Callback<BaseResponse> cb);

    /**
     * Delete a guest given his id.
     *
     * @param id Id of the guest to delete.
     * @param cb Callback of the request.
     */
    @DELETE("/API/events/guest")
    void deleteGuest(@Query("id") long id, Callback<BaseResponse> cb);
}
