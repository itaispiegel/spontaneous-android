package com.spontaneous.android.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.http.request.service.UserService;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.User;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.Logger;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This class is responsible for registering the device with the GCM server.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RegistrationIntentService(String name) {
        super(name);
    }


    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               android.content.Context#startService(Intent)}.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceId = InstanceID.getInstance(this);
            String token = instanceId.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Logger.info("GCM Registration Token: " + token);

            updateRegistrationToken(token);
        } catch (IOException e) {

            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            Logger.error("Failed to complete token refresh", e);
        }
    }

    /**
     * This method updates the registration token at the server and at the application memory.
     *
     * @param newToken The updated token.
     */
    private void updateRegistrationToken(String newToken) {
        User authenticatedUser = AccountUtils.getAuthenticatedUser();

        if (authenticatedUser.getGcmToken() != null && authenticatedUser.getGcmToken().equals(newToken)) {
            Logger.info("The GCM token didn't change, so there is no need to update it.");
        } else {
            Logger.info("The GCM token has changed, updating it at server.");

            sendRegistrationToServer(authenticatedUser.getId(), newToken);
            authenticatedUser.setGcmToken(newToken);
            AccountUtils.setAuthenticatedUser(authenticatedUser);
        }
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(long userId, String token) {
        SpontaneousApplication.getInstance()
                .getService(UserService.class)
                .updateGcmToken(userId, token, new Callback<BaseResponse>() {
                    @Override
                    public void success(BaseResponse baseResponse, Response response) {
                        Logger.info("GCM token was successfully sent to server.");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(RegistrationIntentService.this, "There was a problem with Google Cloud Messaging.", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
}
