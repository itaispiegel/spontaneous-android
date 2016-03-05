package com.spontaneous.android.activity;

import android.content.Intent;
import android.widget.Toast;

import com.android.ex.chips.BaseRecipientAdapter;
import com.android.ex.chips.RecipientEntry;
import com.android.ex.chips.recipientchip.DrawableRecipientChip;
import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.http.request.model.SaveEventRequest;
import com.spontaneous.android.http.request.service.EventService;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.model.Guest;
import com.spontaneous.android.model.User;
import com.spontaneous.android.util.DateTimeFormatter;
import com.spontaneous.android.util.Logger;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This is an activity for editing an event.
 */
public class ActivityEditEvent extends BaseSaveEventActivity {

    @Override
    void submitEvent() {
        final SaveEventRequest saveEventRequest = generateEditEventRequest();

        showWaitDialog();
        Logger.info("Creating new event: " + saveEventRequest);

        //Submit event to server.
        SpontaneousApplication.getInstance().getService(EventService.class).editEvent(getEvent().getId(), saveEventRequest, new Callback<BaseResponse<Event>>() {
            @Override
            public void success(BaseResponse<Event> eventBaseResponse, Response response) {
                Intent intent = new Intent();

                        dismissDialog();

                        Logger.info("Event saved successfully on server.");
                        intent.putExtra(getString(R.string.created_event_intent_extra), eventBaseResponse.getBody());
                        setResult(RESULT_OK, intent);

                        finish();
                    }

                    //In case of failure show an error.
                    @Override
                    public void failure(RetrofitError error) {
                        dismissDialog();

                        Logger.error("Event update on server failed.");
                        Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * @return The details of the event being edited.
     */
    private Event getEvent() {
        return (Event) getIntent().getExtras().getSerializable(getString(R.string.event_edit));
    }

    @Override
    protected void initializeViews() {
        Event event = getEvent();

        //Return void if the event is null.
        if (event == null) {
            return;
        }

        //Initialize the views based on the event details.
        mEventTitle.setText(event.getTitle());
        mEventDescription.setText(event.getDescription());
        mEventLocation.setText(event.getLocation());
        mEventDate.setText(DateTimeFormatter.format(dateFormat, event.getDate().toDate()));
        mEventTime.setText(DateTimeFormatter.format(timeFormat, event.getDate().toDate()));
    }
}