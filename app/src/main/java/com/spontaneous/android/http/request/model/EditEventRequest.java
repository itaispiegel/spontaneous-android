package com.spontaneous.android.http.request.model;

import com.google.gson.annotations.Expose;

import org.joda.time.DateTime;

import java.util.List;

/**
 * This class represents an HTTP POST request to create a new event.
 */
public class EditEventRequest {

    /**
     * The title of the event.
     */
    @Expose
    private final String title;

    /**
     * A short description about the event.
     */
    @Expose
    private final String description;

    /**
     * Id number of the host user.
     */
    @Expose
    private final long hostUserId;

    /**
     * Emails of the users to invite.
     */
    @Expose
    private final List<String> invitedUsersEmails;

    /**
     * When the event is.
     */
    @Expose
    private final DateTime date;

    /**
     * Where the event is.
     */
    @Expose
    private final String location;

    public EditEventRequest(String title, String description, long hostUserId, List<String> invitedUsersEmails, DateTime date, String location) {
        this.title = title;
        this.description = description;
        this.hostUserId = hostUserId;
        this.invitedUsersEmails = invitedUsersEmails;
        this.date = date;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getHostUserId() {
        return hostUserId;
    }

    public List<String> getInvitedUsersEmails() {
        return invitedUsersEmails;
    }

    public DateTime getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "EditEventRequest{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", hostUserId=" + hostUserId +
                ", invitedUsersEmails=" + invitedUsersEmails +
                ", date=" + date +
                ", location='" + location + '\'' +
                '}';
    }
}
