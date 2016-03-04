package com.spontaneous.android.model;

import com.spontaneous.android.http.request.model.UpdateGuestRequest;

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

    /**
     * Create an empty guest instance..
     */
    private Guest() {
        status = "";
        isAttending = false;
    }

    /**
     * Create a guest that references to the given userProfile.
     */
    public Guest(User userProfile) {
        this();
        this.userProfile = userProfile;
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
     * Update the Guest according to the given {@link UpdateGuestRequest}.
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
