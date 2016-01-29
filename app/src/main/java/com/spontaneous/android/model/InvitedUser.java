package com.spontaneous.android.model;

import com.spontaneous.android.http.request.model.UpdateInvitedUserRequest;

/**
 * This class represents a user invited to an event.
 * This object contains data of the user specific to the event, and a pointer to the user itself.
 */
public class InvitedUser extends BaseEntity {

    /**
     * Reference to the user itself.
     */
    private User user;

    /**
     * User status in the event.
     */
    private String status;

    /**
     * Is the user attending?
     */
    private boolean isAttending;

    /**
     * Create an empty invited user.
     */
    private InvitedUser() {
        status = "";
        isAttending = false;
    }

    /**
     * Create an invited user that relies on the given user
     */
    public InvitedUser(User user) {
        this();
        this.user = user;
    }

    /**
     * @return user data.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user pointer.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return user status on the event.
     * <br/>e.g: "Looking forward for it!", "It's gonna be LEGENDARY" and etc.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets user status on the event.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return whether the user is attending the event.
     */
    public boolean isAttending() {
        return isAttending;
    }

    /**
     * Sets whether the user is attending the event.
     */
    public void setIsAttending(boolean isAttending) {
        this.isAttending = isAttending;
    }

    /**
     * Update the InvitedUser according to the given {@link UpdateInvitedUserRequest}.
     * @param updateRequest The request to update the invited user.
     */
    public void update(UpdateInvitedUserRequest updateRequest) {
        this.isAttending = updateRequest.isAttending();
        this.status = updateRequest.getStatus();
    }

    @Override
    public String toString() {
        return "InvitedUser{" +
                "user=" + user +
                ", status='" + status + '\'' +
                ", isAttending=" + isAttending +
                '}';
    }
}
