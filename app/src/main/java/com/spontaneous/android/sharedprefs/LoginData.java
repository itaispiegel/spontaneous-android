package com.spontaneous.android.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.spontaneous.android.model.User;
import com.spontaneous.android.util.GsonFactory;

/**
 * Created by Eidan on 4/25/2015.
 */
public final class LoginData {
    private static final String LOGIN_DATA_PREF = "ridefind.login_data_pref";
    private static final String USER = "ridefind.login.user";
    private static final String USER_ID_PREF = "ridefind.login.user_id";
    private static final String FACEBOOK_USER_ID_PREF = "ridefind.login.facebook.user_id";
    private static final String FACEBOOK_TOKEN_PREF = "ridefind.login.facebook.token";
    private static final String FACEBOOK_PICTURE = "ridefind.login.facebook.picture";

    private LoginData() {
    }

    public static boolean isLoggedIn(Context ctx) {
        return ctx.getSharedPreferences(LOGIN_DATA_PREF, Context.MODE_PRIVATE).getLong(USER_ID_PREF, -1) > 0;
    }

    public static long getUserId(Context ctx) {
        return ctx.getSharedPreferences(LOGIN_DATA_PREF, Context.MODE_PRIVATE).getLong(USER_ID_PREF, -1);
    }

    public static String getFacebookUserId(Context ctx) {
        return ctx.getSharedPreferences(LOGIN_DATA_PREF, Context.MODE_PRIVATE).getString(FACEBOOK_USER_ID_PREF, null);
    }

    public static String getFacebookToken(Context ctx) {
        return ctx.getSharedPreferences(LOGIN_DATA_PREF, Context.MODE_PRIVATE).getString(FACEBOOK_TOKEN_PREF, null);
    }

    public static String getFacebookPicture(Context ctx) {
        return ctx.getSharedPreferences(LOGIN_DATA_PREF, Context.MODE_PRIVATE).getString(FACEBOOK_PICTURE, null);
    }

    public static User getUser(Context ctx) {
        String json = ctx.getSharedPreferences(LOGIN_DATA_PREF, Context.MODE_PRIVATE).getString(USER, null);
        if(json != null) {
            return GsonFactory.gson.fromJson(json, User.class);
        } else {
            return null;
        }
    }

    public static void logout(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(LOGIN_DATA_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = prefs.edit();
        e.remove(USER_ID_PREF);
        e.remove(FACEBOOK_USER_ID_PREF);
        e.remove(FACEBOOK_TOKEN_PREF);
        e.remove(FACEBOOK_PICTURE);
        e.remove(USER);
        e.apply();
    }

    public static void login(Context context, User user) {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_DATA_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = prefs.edit();
        e.putLong(USER_ID_PREF, user.getId());
        e.putString(FACEBOOK_USER_ID_PREF, user.getFacebookUserId());
        e.putString(FACEBOOK_TOKEN_PREF, user.getFacebookToken());
        e.putString(FACEBOOK_PICTURE, user.getPictureUrl());
        e.putString(USER, GsonFactory.gson.toJson(user));
        e.apply();
    }
}
