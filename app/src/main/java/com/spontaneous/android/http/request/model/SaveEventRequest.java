package com.spontaneous.android.http.request.model;

import org.joda.time.DateTime;

import java.util.List;

/**
 * This class represents an HTTP POST request to create a new event.
 */
public class SaveEventRequest {

    /**
     * The title of the event.
     */
    private final String title;

    /**
     * A short description about the event.
     */
    private final String description;

    /**
     * Id number of the host user.
     */
    private final long hostUserId;

    /**
     * Emails of the users to invite.
     */
    private final List<String> guestsEmails;

    /**
     * When the event is.
     */
    private final DateTime date;

    /**
     * Where the event is.
     */
    private final String location;

    public SaveEventRequest(String title, String description, long hostUserId, List<String> guestsEmails, DateTime date, String location) {
        this.title = title;
        this.description = description;
        this.hostUserId = hostUserId;
        this.guestsEmails = guestsEmails;
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

    public List<String> getGuestsEmails() {
        return guestsEmails;
    }

    public DateTime getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "SaveEventRequest{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", hostUserId=" + hostUserId +
                ", guestsEmails=" + guestsEmails +
                ", date=" + date +
                ", location='" + location + '\'' +
                '}';
    }
}
