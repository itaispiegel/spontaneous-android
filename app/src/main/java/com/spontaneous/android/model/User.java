package com.spontaneous.android.model;

import java.util.List;

/**
 * Created by Eidan on 4/26/2015.
 */
public class User extends BaseUpdatableEntity {

    private String facebookUserId;
    private String facebookToken;
    private String pictureUrl;
    private String email;
    private String fullName;
    private List<Ride> ownerRides;
    private List<Ride> guestRides;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getFacebookUserId() {
        return facebookUserId;
    }

    public void setFacebookUserId(String facebookUserId) {
        this.facebookUserId = facebookUserId;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Ride> getOwnerRides() {
        return ownerRides;
    }

    public void setOwnerRides(List<Ride> ownerRides) {
        this.ownerRides = ownerRides;
    }

    public List<Ride> getGuestRides() {
        return guestRides;
    }

    public void setGuestRides(List<Ride> guestRides) {
        this.guestRides = guestRides;
    }

    @Override
    public String toString() {
        return "User{" +
            "facebookUserId='" + facebookUserId + '\'' +
            ", facebookToken='" + facebookToken + '\'' +
            ", pictureUrl='" + pictureUrl + '\'' +
            ", email='" + email + '\'' +
            ", ownerRides=" + ownerRides +
            ", guestRides=" + guestRides +
            '}';
    }
}
