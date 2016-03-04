package com.spontaneous.android.model;

import org.joda.time.DateTime;
import org.joda.time.Partial;
import org.joda.time.Years;

import java.util.List;

/**
 * This class represents a user persisted in the database.
 */
public class User extends BaseEntity {

    /**
     * Facebook id of the user.
     */
    private String facebookUserId;

    /**
     * Facebook token of the user.
     */
    private String facebookToken;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The email of the user.
     */
    private String email;

    /**
     * Profile picture URL of the user.
     */
    private String profilePicture;

    /**
     * Birthday of the user.
     */
    private DateTime birthday;

    /**
     * Phone number of the user.
     */
    private String phoneNumber;

    /**
     * Is the user a male or a female.
     */
    private Gender gender;

    private String gcmToken;

    private List<User> friends;

    /**
     * Create an empty user object.
     */
    public User() {
    }

    /**
     * @return facebook user id.
     */
    public String getFacebookUserId() {
        return facebookUserId;
    }

    /**
     * @return facebook token of the user.
     */
    public String getFacebookToken() {
        return facebookToken;
    }

    /**
     * @return the name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return profile picture URL of the user.
     */
    public String getProfilePicture() {
        return profilePicture;
    }

    /**
     * @return birthday of the user.
     */
    public DateTime getBirthday() {
        return birthday;
    }

    /**
     * @return The age of the user.
     */
    public int getAge() {
        return Math.abs(Years.yearsBetween(DateTime.now(), birthday)
                .getYears());
    }

    /**
     * @return the phone number of the user.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return whether the user is a male or female.
     */
    public Gender getGender() {
        return gender;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public List<User> getFriends() {
        return friends;
    }

    /**
     * The two {@link User} entities are equal, if the have equal id's, names, and emails.
     *
     * @param o Object to compare to.
     * @return Whether this equals the given object.
     */
    @Override
    public boolean equals(Object o) {
        User user = (User) o;

        return super.equals(o) && name.equals(user.name) && email.equals(user.email);

    }

    /**
     * Return a string representation of the user.
     */
    @Override
    public String toString() {
        return "User{" +
                "facebookUserId='" + facebookUserId + '\'' +
                ", facebookToken='" + facebookToken + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", birthday=" + birthday +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender=" + gender +
                "} " + super.toString();
    }

    public enum Gender {
        Male, Female, Unspecified
    }
}
