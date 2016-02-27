package com.spontaneous.android.model;

/**
 * This class represents a public user profile.
 */
public class UserProfile {

    private final String name;
    private final String email;
    private final String profilePicture;
    private final int age;

    public UserProfile(String name, String email, String profilePicture, int age) {
        this.name = name;
        this.email = email;
        this.profilePicture = profilePicture;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public int getAge() {
        return age;
    }
}
