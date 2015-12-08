package com.spontaneous.android.model;

import org.joda.time.DateTime;

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
     * When is the event.
     */
    private DateTime when;

    /**
     * Where is the event.
     */
    private String where;

    public Event() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public void setInvitedUsers(Collection<InvitedUser> invitedUsers) {
        this.invitedUsers = invitedUsers;
    }

    public void setWhen(DateTime when) {
        this.when = when;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getHost() {
        return host;
    }

    public Collection<InvitedUser> getInvitedUsers() {
        return invitedUsers;
    }

    public DateTime getWhen() {
        return when;
    }

    public String getWhere() {
        return where;
    }
}
