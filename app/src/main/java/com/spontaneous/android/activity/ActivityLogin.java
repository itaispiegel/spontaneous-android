package com.spontaneous.android.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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
import com.spontaneous.android.util.UIUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Itai on 13-Nov-15.
 */
public class ActivityLogin extends Activity {

    private Dialog mWaitDialog;

    private AutoCompleteTextView mEmail;
    private EditText mPassword;
    private LoginButton loginButton;

    private boolean mIsProcessing = false;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Animations, FacebookSDK and content view
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animate_fade_in, R.anim.animate_fade_out);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        //Custom action bar configuration
        UIUtils.setCustomActionBar(this);

        //Email and password edittexts
        mEmail = (AutoCompleteTextView) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);

        //Setup email auto complete
        UIUtils.setupEmailAutoComplete(mEmail, this);

        //Initialize FacebookSDK
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "user_friends", "email");
        loginButton.registerCallback(callbackManager, getFacebookCallback());
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
                Logger.error("LoginActivity onFailure, " + error.getKind());

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
        mIsProcessing = false;

        AccountUtils.startMainFlow(this);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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