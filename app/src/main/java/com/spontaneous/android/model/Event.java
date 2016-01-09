package com.spontaneous.android.model;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class represents an event persisted in the database.
 */
public class Event extends BaseEntity {

    /**
     * The title of the event.
     */
    private String title;

    /**
     * The description of the event.
     */
    private String description;

    /**
     * Host user of the event.
     * One event has one host.
     */
    private User host;

    /**
     * Users attending to the event.
     * One event has many users attending.
     */
    private Collection<InvitedUser> invitedUsers;

    /**
     * When the event is.
     */
    private DateTime date;

    /**
     * Where the event is.
     */
    private String location;

    public Event() {
        super();
        invitedUsers = new ArrayList<>();
    }

    private void inviteUser(User user) {
        InvitedUser invitedUser = new InvitedUser(user);
        invitedUsers.add(invitedUser);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public Collection<InvitedUser> getInvitedUsers() {
        return invitedUsers;
    }

    public void setInvitedUsers(Collection<InvitedUser> invitedUsers) {
        this.invitedUsers = invitedUsers;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isUserAttending(User user) {
        for (InvitedUser invitedUser : invitedUsers) {
            if(invitedUser.getUser().equals(user)) {
                return true;
            }
        }

        return false;
    }
}
