package com.spontaneous.android.http.request.model;

import com.spontaneous.android.model.Guest;


/**
 * This class represents an HTTP PUT request to update an {@link Guest} entity.
 */
public class UpdateGuestRequest {

    /**
     * What the user has to say about the event.
     */
    private final String status;

    /**
     * Whether the user is attending.
     */
    private final boolean isAttending;

    public UpdateGuestRequest(String status, boolean isAttending) {
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
        return "UpdateGuestRequest{" +
                ", status='" + status + '\'' +
                ", isAttending=" + isAttending +
                '}';
    }
}
