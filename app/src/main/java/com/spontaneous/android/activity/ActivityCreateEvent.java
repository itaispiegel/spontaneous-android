package com.spontaneous.android.activity;

import android.content.Intent;
import android.widget.Toast;

import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.http.request.model.SaveEventRequest;
import com.spontaneous.android.http.request.service.EventService;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.util.Logger;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Using this activity, the user can create a new event.
 */
public class ActivityCreateEvent extends BaseSaveEventActivity {

    /**
     * Submit the created event to the server.
     * Show a wait dialog, and when succeeded return to {@link ActivityMain}.
     * If failed, show an error to the user.
     */
    @Override
    protected void submitEvent() {
        final SaveEventRequest event = generateEditEventRequest();

        showWaitDialog();
        Logger.info("Creating new event: " + event);

        //Submit event to server.
        SpontaneousApplication.getInstance()
                .getService(EventService.class)
                .createEvent(event, new Callback<BaseResponse<Event>>() {
                    @Override
                    public void success(BaseResponse<Event> eventBaseResponse, Response response) {
                        Intent intent = new Intent();

                        dismissDialog();

                        Logger.info("Event created successfully on server.");
                        intent.putExtra(getString(R.string.created_event_intent_extra), eventBaseResponse.getBody());
                        setResult(RESULT_OK, intent);

                        finish();
                    }

                    //In case of failure show an error.
                    @Override
                    public void failure(RetrofitError error) {
                        dismissDialog();

                        Logger.error("Event creation on server failed.");
                        Logger.error(error.getMessage());

                        Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    @Override
    protected void initializeViews() {
        //No need to initialize anything if creating a new event.
    }
}
