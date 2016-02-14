package com.spontaneous.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.http.request.model.FacebookLoginRequest;
import com.spontaneous.android.http.request.service.LoginService;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.User;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.Logger;

import java.util.Iterator;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Using this activity, the user can log in to the application using an email and password combination, or via Facebook.
 */
public class ActivityLogin extends BaseActivity {

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize email and password EditTexts
        AutoCompleteTextView mEmail = (AutoCompleteTextView) findViewById(R.id.email);

        //Setup email auto complete
        setupEmailAutoComplete(mEmail, this);

        //Just in case the user is accidentally authenticated
        LoginManager.getInstance().logOut();

        //Initialize FacebookSDK
        mCallbackManager = CallbackManager.Factory.create();

        //Initialize login button and set its permissions, callback and click listener
        LoginButton mLoginButton = (LoginButton) findViewById(R.id.login_button);

        mLoginButton.setReadPermissions("public_profile", "user_friends", "email", "user_birthday");
        mLoginButton.registerCallback(mCallbackManager, getFacebookCallback());
        mLoginButton.setOnClickListener(getLoginButtonOnClickListener());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    /**
     * @return OnclickListener that shows the wait dialog.
     */
    private View.OnClickListener getLoginButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.info("Starting authentication process.");
                showWaitDialog();
            }
        };
    }

    /**
     * @return A new Facebook callback method - the method that processes the data retrieved.
     * In case of success, login.
     * In case of failure, show an error.
     */
    private FacebookCallback<LoginResult> getFacebookCallback() {
        return new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                //Add the permissions.
                AccessToken accessToken = loginResult.getAccessToken();

                //Add the first permission.
                String permissions = "";
                Iterator<String> permissionIterator = accessToken.getPermissions().iterator();
                if (permissionIterator.hasNext()) {
                    permissions += permissionIterator.next();
                }

                //Separate the next permissions with a comma.
                while (permissionIterator.hasNext()) {
                    permissions += "," + permissionIterator.next();
                }

                //Send a a log message
                Logger.info("Facebook authentication succeeded, proceeding to authentication with Heroku Server.");
                Logger.info("token = " + accessToken.getToken() + ", userId = " + accessToken.getUserId() +
                        ", app id = " + accessToken.getApplicationId() + ", permissions = " + permissions);

                //Create and send a FacebookLoginRequest to the Heroku Server.
                LoginService loginService = SpontaneousApplication.getInstance().getService(LoginService.class);
                FacebookLoginRequest loginRequestModel = new FacebookLoginRequest(accessToken.getUserId(), accessToken.getToken());

                //Send the login request.
                loginService.login(loginRequestModel, getLoginRequestCallback());
            }

            @Override
            public void onCancel() {
                Logger.info("cancel!");
            }

            @Override
            public void onError(FacebookException error) {

                //If the Facebook authentication did not succeed, show an error.
                Logger.error("Facebook authentication error: " + error.getMessage());

                Toast.makeText(getApplicationContext(), "Facebook Connection Error.", Toast.LENGTH_SHORT)
                        .show();
            }
        };
    }

    /**
     * @return The user request callback.
     * In case of success, continue to the main activity.
     * In case of failure, show an error.
     */
    private Callback<BaseResponse<User>> getLoginRequestCallback() {
        return new Callback<BaseResponse<User>>() {
            @Override
            public void success(BaseResponse<User> baseResponse, Response response) {
                dismissDialog();

                //Proceed to the Main Activity, or show an error if the status code is negative.
                if (baseResponse.getStatusCode() == BaseResponse.SUCCESS) {

                    //Get the user entity.
                    User user = baseResponse.getBody();
                    Logger.info("LoginRequest: success, user = " + user);

                    onAuthenticationFinished(user);
                } else {
                    Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dismissDialog();

                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT)
                        .show();

                Logger.error("LoginActivity onFailure, " + error.getMessage());

                //Sign out of Facebook if the app didn't manage to authenticate with Spontaneous server.
                if (AccountUtils.isAuthenticatedWithFacebook()) {
                    AccountUtils.logout(getApplicationContext(), false);
                    Logger.error("Logged out of Facebook since the app couldn't authenticate with Spontaneous server.");
                }
            }
        };
    }

    /**
     * Save authenticated user data in memory and start main flow.
     */
    private void onAuthenticationFinished(User user) {

        //Dismiss the wait dialog
        if (mWaitDialog != null && mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }

        //Set user data in application memory.
        AccountUtils.setAuthenticatedUser(user);

        //Start the main activity
        AccountUtils.startMainFlow(this);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }
}
