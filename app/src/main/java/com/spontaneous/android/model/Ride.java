package com.spontaneous.android.model;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eidan on 5/23/15.
 */
public class Ride extends BaseUpdatableEntity {

    private User owner;
    private List<User> guests;
    private List<Long> paidGuests;
    private String origin;
    private String destination;
    private DateTime startTime;
    private BigDecimal cost;
    private RideStatus status;
    private String description;
    private int passengers;

    public Ride() {
        status = RideStatus.PENDING;
        guests = new ArrayList<>();
        paidGuests = new ArrayList<>();
    }

    public enum RideStatus {
        PENDING,
        DONE
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public List<Long> getPaidGuests() {
        return paidGuests;
    }

    public void setPaidGuests(List<Long> paidGuests) {
        this.paidGuests = paidGuests;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getGuests() {
        return guests;
    }

    public void setGuests(List<User> guests) {
        this.guests = guests;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
