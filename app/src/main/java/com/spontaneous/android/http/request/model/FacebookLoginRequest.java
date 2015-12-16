package com.spontaneous.android.http.request.model;

/**
 * This is a Facebook login request sent to the server.
 */
public class FacebookLoginRequest {
    private final String facebookUserId;
    private final String facebookToken;

    public FacebookLoginRequest(String facebookUserId, String facebookToken) {
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
        return "FacebookLoginRequest{" +
                "facebookUserId='" + facebookUserId + '\'' +
                ", facebookToken='" + facebookToken + '\'' +
                '}';
    }
}
