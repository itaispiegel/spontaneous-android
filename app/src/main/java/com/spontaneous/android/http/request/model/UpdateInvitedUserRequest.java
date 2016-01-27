package com.spontaneous.android.http.request.model;

import com.google.gson.annotations.Expose;
import com.spontaneous.android.model.InvitedUser;


/**
 * This class represents an HTTP PUT request to update an {@link InvitedUser} entity.
 */
public class UpdateInvitedUserRequest {

    /**
     * What the user has to say about the event.
     */
    @Expose
    private final String status;

    /**
     * Whether the user is attending.
     */
    @Expose
    private final boolean isAttending;

    public UpdateInvitedUserRequest(String status, boolean isAttending) {
        this.status = status;
        this.isAttending = isAttending;
    }

    public String getStatus() {
        return status;
    }

    public boolean isAttending() {
        return isAttending;
    }

    @Override
    public String toString() {
        return "UpdateInvitedUserRequest{" +
                ", status='" + status + '\'' +
                ", isAttending=" + isAttending +
                '}';
    }
}
