package com.spontaneous.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.activity.ActivityLogin;
import com.spontaneous.android.activity.ActivityMain;
import com.spontaneous.android.activity.BaseActivity;
import com.spontaneous.android.model.User;

/**
 * An assortment of authentication and login helper utilities.
 */
public class AccountUtils {

    private static final String PREF_FACEBOOK_TOKEN = "auth_token";
    private static final String PREF_USER = "user";

    private static User sUser = null;
    private static String sFacebookToken = null;

    /**
     * @return Whether a current user is authenticated.
     */
    public static boolean isAuthenticated() {

        //Return true if the user is authenticated both on the server and on Facebook.
        if (!TextUtils.isEmpty(getFacebookToken()) && getAuthenticatedUser() != null) {
            return true;
        }

        //In case the user has authenticated with Facebook, but not with the server.
        if (getFacebookToken() != null) {
            setFacebookToken(null);
        }

        return false;
    }

    /**
     * @return Whether the user is signed in with Facebook.
     */
    public static boolean isAuthenticatedWithFacebook() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    /**
     * @return The Facebook token of the authenticated user.
     */
    private static String getFacebookToken() {
        if (sFacebookToken == null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SpontaneousApplication.getInstance());
            sFacebookToken = sp.getString(PREF_FACEBOOK_TOKEN, null);
        }

        return sFacebookToken;
    }

    /**
     * Sets a Facebook token.
     *
     * @param token to set.
     */
    private static void setFacebookToken(final String token) {
        AccountUtils.sFacebookToken = token;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SpontaneousApplication.getInstance());
        sp.edit().putString(PREF_FACEBOOK_TOKEN, token).apply();
    }

    /**
     * Clears the current Facebook token.
     */
    private static void clearAuthenticationToken() {
        setFacebookToken(null);
    }

    /**
     * Logout the current user.
     * Clears the current Facebook token, sets current user to null.
     *
     * @param context                 Current context.
     * @param startAuthenticationFlow Whether the app should start the authentication flow.
     */
    public static void logout(Context context, boolean startAuthenticationFlow) {
        AccountUtils.clearAuthenticationToken();
        AccountUtils.setAuthenticatedUser(null);

        LoginManager.getInstance().logOut();

        if (startAuthenticationFlow) {
            startAuthenticationFlow((BaseActivity) context);
        }
    }

    /**
     * Sets the current authenticated user.
     */
    public static void setAuthenticatedUser(@Nullable User user) {
        AccountUtils.sUser = user;

        //Save user in application preferences.
        String userJson = GsonFactory.getGson().toJson(user);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SpontaneousApplication.getInstance());
        sp.edit().putString(PREF_USER, userJson).apply();

        if (user != null) {
            setFacebookToken(user.getFacebookToken());
        }
    }

    /**
     * Gets the current authenticated user from device memory.
     * If no user details found, get them from the server.
     *
     * @return the authenticated user.
     */
    public static User getAuthenticatedUser() {
        if (sUser != null) {
            return sUser;
        } else {
            //Check if SharedPreferences holds current user, if it does, return it.
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SpontaneousApplication.getInstance());
            String userJson = sp.getString(PREF_USER, null);

            if (userJson != null) {
                sUser = GsonFactory.getGson().fromJson(userJson, User.class);
                return sUser;
            }
        }

        throw new NullPointerException("Tried to get the authenticated user, but there is no user in the application memory.");
    }

    /**
     * Start the authentication activity.
     */
    public static void startAuthenticationFlow(final BaseActivity baseActivity) {
        baseActivity.startActivity(ActivityLogin.class);
    }

    /**
     * Start the main activity.
     */
    public static void startMainFlow(final BaseActivity baseActivity) {
        baseActivity.startActivity(ActivityMain.class);
    }
}
