package com.spontaneous.android.http.request;

/**
 * This is a Facebook login request sent to the server.
 */
public class FacebookLoginRequestModel {
    private final String facebookUserId;
    private final String facebookToken;

    public FacebookLoginRequestModel(String facebookUserId, String facebookToken) {
        this.facebookUserId = facebookUserId;
        this.facebookToken = facebookToken;
    }

    public String getFacebookUserId() {
        return facebookUserId;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    @Override
    public String toString() {
        return "FacebookLoginRequestModel{" +
                "facebookUserId='" + facebookUserId + '\'' +
                ", facebookToken='" + facebookToken + '\'' +
                '}';
    }
}
