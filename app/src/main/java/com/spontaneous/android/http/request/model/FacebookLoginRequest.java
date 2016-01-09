package com.spontaneous.android.http.request.model;

/**
 * This is a Facebook login request sent to the server.
 */
public class FacebookLoginRequest {

    /**
     * Facebook user id.
     */
    private final String facebookUserId;

    /**
     * Facebook user token.
     */
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
