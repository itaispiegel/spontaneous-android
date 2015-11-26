package com.spontaneous.android.model;

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
    private Boolean isAttending;

    /**
     * Create an empty invited user.
     */
    public InvitedUser() {
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
    public Boolean isAttending() {
        return isAttending;
    }

    /**
     * Sets whether the user is attending the event.
     */
    public void setIsAttending(Boolean isAttending) {
        this.isAttending = isAttending;
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
