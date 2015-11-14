package com.spontaneous.android.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.spontaneous.android.Application;
import com.spontaneous.android.activity.ActivityLogin;
import com.spontaneous.android.activity.ActivityMain;
import com.spontaneous.android.model.User;

/**
 * An assortment of authentication and login helper utilities.
 */
public class AccountUtils {

    private static final String PREF_AUTH_TOKEN = "auth_token";
    private static final String PREF_ID = "id";
    private static final String PREF_USER = "user";

    private static User sUser = null;
    private static Long sId = null;
    private static String sFacebookToken = null;

    /**
     * @return whether a current user is authenticated.
     */
    public static boolean isAuthenticated() {
        return !TextUtils.isEmpty(getFacebookToken()) && null != getAuthenticatedUser();
    }

    /**
     * @return whether the user is signed in with Facebook.
     */
    public static boolean isAuthenticatedWithFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    /**
     * @return the Facebook token of the authenticated user.
     */
    public static String getFacebookToken() {
        if (sFacebookToken == null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Application.getInstance());
            sFacebookToken = sp.getString(PREF_AUTH_TOKEN, null);
        }

        return sFacebookToken;
    }

    /**
     * Sets a Facebook token.
     *
     * @param token to set.
     */
    public static void setFacebookToken(final String token) {
        AccountUtils.sFacebookToken = token;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Application.getInstance());
        sp.edit().putString(PREF_AUTH_TOKEN, token).apply();
    }

    /**
     * Logout the current user.
     * Clears the current Facebook token,
     * sets current user to null and starts the authentication flow.
     */
    public static void logout(Context ctx) {
        AccountUtils.clearAuthenticationToken();
        AccountUtils.setAuthenticatedUser(null);
        AccountUtils.startAuthenticationFlow(ctx);

        LoginManager.getInstance().logOut();
    }

    /**
     * Clears the current Facebook token.
     */
    public static void clearAuthenticationToken() {
        AccountUtils.sFacebookToken = null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Application.getInstance());
        sp.edit().putString(PREF_AUTH_TOKEN, null).apply();
    }

    /**
     * Sets the current authenticated user.
     */
    public static void setAuthenticatedUser(@Nullable User user) {
        AccountUtils.sUser = user;

        String userJson = GsonFactory.getGson().toJson(user);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Application.getInstance());
        sp.edit().putString(PREF_USER, userJson).apply();

        if (null != user) {
            setId(user.getId());
            setFacebookToken(user.getFacebookToken());
        }
    }

    /**
     * Gets the current authenticated user from device memory.
     * If no user details found, get them from the server.
     *
     * @return the authenticated user
     */
    public static User getAuthenticatedUser() {
        if (sUser != null) {
            return sUser;
        } else {
            //Check if SharedPreferences holds current user, if it does, return it.
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Application.getInstance());
            String userJson = sp.getString(PREF_USER, null);

            if (userJson != null) {
                sUser = GsonFactory.getGson().fromJson(userJson, User.class);
                return sUser;
            }

            //If we do not have the current user in the memory. Retrieve it from the server.
            //TODO: Implement
/*            Application.getInstance().getUserService().getAuthenticatedUser(new Callback<Result<User>>() {
                @Override
                public void success(Result<User> user, Response response) {
                    setAuthenticatedUser(user.getResult());
                }

                @Override
                public void failure(RetrofitError error) {
                }
            });*/
        }
        return sUser;
    }

    /**
     * Gets the authenticated user's id
     *
     * @return authenticated user's id
     */
    public static Long getId() {
        if (sId == null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Application.getInstance());
            sId = Long.parseLong(sp.getString(PREF_ID, "-1"));
        }

        return sId;
    }

    /**
     * sets the authenticated user's id
     */
    public static void setId(final Long id) {
        AccountUtils.sId = id;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Application.getInstance());
        sp.edit().putString(PREF_ID, String.valueOf(id)).apply();
    }

    /**
     * Start a flow by the specified class and context
     *
     * @param flowClass of the requested flow
     * @param context   current context
     */
    private static void startFlow(final Context context, final Class flowClass) {
        Intent intent = new Intent(context, flowClass);
        if (Build.VERSION.SDK_INT >= 11) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    /**
     * Starts the authentication activity
     */
    public static void startAuthenticationFlow(final Context context) {
        startFlow(context, ActivityLogin.class);
    }

    /**
     * Start the main activity
     */
    public static void startMainFlow(final Context ctx) {
        startFlow(ctx, ActivityMain.class);
    }
}
