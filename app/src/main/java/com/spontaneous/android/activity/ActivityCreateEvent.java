package com.spontaneous.android.activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Rfc822Tokenizer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ex.chips.BaseRecipientAdapter;
import com.android.ex.chips.RecipientEditTextView;
import com.android.ex.chips.recipientchip.DrawableRecipientChip;
import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.adapter.PlacesAutoCompleteAdapter;
import com.spontaneous.android.http.request.model.CreateEventRequest;
import com.spontaneous.android.http.request.service.EventService;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.DateTimeFormatter;
import com.spontaneous.android.util.Logger;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;
import static com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance;
import static com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;
import static com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance;

/**
 * Using this activity, the user can create a new event.
 */
public class ActivityCreateEvent extends BaseEditEvent {

    /**
     * @return New create event request generated from the user input.
     */
    private CreateEventRequest generateCreateEventRequest() {
        return new CreateEventRequest(
                mEventTitle.getText().toString(),
                mEventDescription.getText().toString(),
                AccountUtils.getAuthenticatedUser().getId(),
                getInvitedUsersList(),
                getDateTime(),
                mEventLocation.getText().toString()
        );
    }

    /**
     * Submit the created event to the server.
     * Show a wait dialog, and when succeeded return to {@link ActivityMain}.
     * If failed, show an error to the user.
     */
    @Override
    protected void submitEvent() {
        final CreateEventRequest event = generateCreateEventRequest();
        final Intent intent = new Intent();

        showWaitDialog();
        Logger.info("Creating new event: " + event);

        //Submit event to server.
        SpontaneousApplication.getInstance().getService(EventService.class).createEvent(event, new Callback<BaseResponse<Event>>() {
            @Override
            public void success(BaseResponse<Event> eventBaseResponse, Response response) {
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
                Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
