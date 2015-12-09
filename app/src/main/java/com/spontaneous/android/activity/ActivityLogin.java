package com.spontaneous.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.spontaneous.android.R;
import com.spontaneous.android.http.ApiRestClient;
import com.spontaneous.android.http.request.FacebookLoginRequestModel;
import com.spontaneous.android.http.request.LoginRequest;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.User;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.Logger;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Using this activity, the user can log in to the application using an email and password combination, or via Facebook.
 */
public class ActivityLogin extends BaseActivity {

    private Dialog mWaitDialog;

    private AutoCompleteTextView mEmail;
    private EditText mPassword;
    private LoginButton mLoginButton;

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Animations, and content view
        super.onCreate(savedInstanceState);

        //Initialize email and password edittexts
        mEmail = (AutoCompleteTextView) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);

        //Setup email auto complete
        setupEmailAutoComplete(mEmail, this);

        //Initialize FacebookSDK
        mCallbackManager = CallbackManager.Factory.create();

        //Set login button image, permissions, callback and click listener
        mLoginButton = (LoginButton) findViewById(R.id.login_button);

        mLoginButton.setReadPermissions("public_profile", "user_friends", "email");
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

    private View.OnClickListener getLoginButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWaitDialog = showWaitDialog();
            }
        };
    }

    private FacebookCallback<LoginResult> getFacebookCallback() {
        return new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken token = loginResult.getAccessToken();
                String permissions = "";

                for (String s : token.getPermissions()) {
                    permissions += s + ",";
                }

                if (!TextUtils.isEmpty(permissions)) {
                    permissions = permissions.substring(0, permissions.length() - 1);
                }

                Logger.info("Success! token = " + token.getToken() + ", userId = " + token.getUserId() +
                        ", app id = " + token.getApplicationId() + ", permissions = " + permissions);

                LoginRequest loginRequest = ApiRestClient.getRequest(getApplicationContext(), LoginRequest.class);
                FacebookLoginRequestModel loginRequestModel = new FacebookLoginRequestModel(token.getUserId(), token.getToken());

                loginRequest.login(loginRequestModel, getLoginRequestCallback());
            }

            @Override
            public void onCancel() {
                Logger.info("cancel!");
            }

            @Override
            public void onError(FacebookException error) {
                Logger.info("error! " + error.getMessage());
            }
        };
    }

    private Callback<BaseResponse<User>> getLoginRequestCallback() {
        return new Callback<BaseResponse<User>>() {
            @Override
            public void success(BaseResponse<User> baseResponse, Response response) {
                if (mWaitDialog != null && mWaitDialog.isShowing()) {
                    mWaitDialog.dismiss();
                }

                User user = baseResponse.getBody();
                Logger.info("LoginRequest: success, user = " + user);

                if (baseResponse.getStatusCode() == BaseResponse.SUCCESS) {
                    onAuthenticationFinished(user);
                } else {
                    Toast.makeText(getApplicationContext(), baseResponse.getDescription(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (mWaitDialog != null && mWaitDialog.isShowing()) {
                    mWaitDialog.dismiss();
                }

                Logger.error("LoginActivity onFailure, " + error.getMessage());

                //Sign out of Facebook if the app didn't manage to authenticate with Spontaneous server.
                if (AccountUtils.isAuthenticatedWithFacebook()) {
                    AccountUtils.logout(getApplicationContext());
                    Logger.error("Logged out of Facebook since the app couldn't authenticate with Spontaneous server.");
                }
            }
        };
    }

    /**
     * Save authenticated user data in memory and start main flow.
     */
    private void onAuthenticationFinished(User user) {
        AccountUtils.setFacebookToken(user.getFacebookToken());
        AccountUtils.setAuthenticatedUser(user);

        if (mWaitDialog != null && mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }

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
