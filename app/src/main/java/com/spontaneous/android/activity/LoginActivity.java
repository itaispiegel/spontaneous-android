package com.spontaneous.android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;

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
import com.spontaneous.android.sharedprefs.LoginData;
import com.spontaneous.android.util.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends BaseActivity {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        printKeyHash();

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "user_friends", "email");

        final Context context = LoginActivity.this;
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken token = loginResult.getAccessToken();
                String permissions = "";
                for(String s : token.getPermissions()) {
                    permissions += s+",";
                }
                if(!TextUtils.isEmpty(permissions)) {
                    permissions = permissions.substring(0, permissions.length()-1);
                }

                Logger.i("Success! token = " + token.getToken() + ", userId = " + token.getUserId() +
                        ", app id = " + token.getApplicationId() + ", permissions = " + permissions);

                LoginRequest loginRequest = ApiRestClient.getRequest(context, LoginRequest.class);
                loginRequest.login(new FacebookLoginRequestModel(token.getUserId(), token.getToken()), new Callback<BaseResponse<User>>() {
                    @Override
                    public void success(BaseResponse<User> baseResponse, Response response) {
                        User user = baseResponse.getBody();
                        Logger.i("LoginRequest: success, user = "+user);
                        LoginData.login(context, user);
                        moveToMainActivity();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Logger.e("LoginActivity onFailure, "+error.getKind());
                    }
                });
            }

            @Override
            public void onCancel() {
                Logger.i("cancel!");
            }

            @Override
            public void onError(FacebookException e) {
                Logger.i("error! " + e.getMessage());
            }
        });

        if(LoginData.isLoggedIn(LoginActivity.this)) {
            moveToMainActivity();
        }
    }

    private void moveToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void printKeyHash(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ridefind.ridefind",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Logger.d(Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Logger.d(e.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
