package com.spontaneous.android.model;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
    private List<Guest> guests;

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
        guests = new ArrayList<>();
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

    public List<Guest> getGuests() {
        return guests;
    }

    public DateTime getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public HashSet<String> getGuestsEmails() {
        HashSet<String> emails = new HashSet<>(guests.size());

        for (Guest guest : guests) {
            emails.add(guest.getUserProfile()
                    .getEmail());
        }

        return emails;
    }

    public Guest getGuestByUser(User user) {
        for(Guest guest : guests) {
            if(guest.getUserProfile().equals(user)) {
                return guest;
            }
        }

        return null;
    }

    /**
     * @return The list of required items for the event.
     */
    public List<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();

        //Iterate over all of the guests, and return all of the items of the event.
        for (Guest guest : guests) {
            items.addAll(guest.getItems());
        }

        return items;
    }

    /**
     * @param user The given user.
     * @return Whether the given user is attending the event.
     */
    public boolean isUserAttending(User user) {
        for (Guest guest : guests) {
            if (user.equals(guest.getUserProfile())) {
                return guest.isAttending();
            }
        }

        return false;
    }
}
