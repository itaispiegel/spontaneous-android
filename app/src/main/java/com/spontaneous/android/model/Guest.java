package com.spontaneous.android.model;

import com.spontaneous.android.http.request.model.UpdateGuestRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a userProfile invited to an event.
 * This object contains data of the userProfile specific to the event, and a pointer to the userProfile itself.
 */
public class Guest extends BaseEntity {

    /**
     * Reference to the userProfile itself.
     */
    private User userProfile;

    /**
     * User status in the event.
     */
    private String status;

    /**
     * Is the userProfile attending?
     */
    private boolean isAttending;

    private List<Item> items;

    /**
     * Create an empty guest instance..
     */
    private Guest() {
        status = "";
        isAttending = false;

        items = new ArrayList<>();
    }

    public User getUserProfile() {
        return userProfile;
    }

    /**
     * @return userProfile status on the event.
     * <br/>e.g: "Looking forward for it!", "It's gonna be LEGENDARY" and etc.
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return whether the userProfile is attending the event.
     */
    public boolean isAttending() {
        return isAttending;
    }

    /**
     * Set the bringer for each item as the guest, and return the list.
     *
     * @return List of items the guest is bringing to the event.
     */
    public List<Item> getItems() {

        //Set the bringer for each item.
        for (Item item : items) {
            item.setBringer(this);
        }

        return items;
    }

    /**
     * Update the Guest according to the given {@link UpdateGuestRequest}.
     *
     * @param updateRequest The request to update the guest.
     */
    public void update(UpdateGuestRequest updateRequest) {
        this.isAttending = updateRequest.isAttending();
        this.status = updateRequest.getStatus();
    }

    @Override
    public String toString() {
        return "Guest{" +
                "userProfile=" + userProfile +
                ", status='" + status + '\'' +
                ", isAttending=" + isAttending +
                '}';
    }
}
