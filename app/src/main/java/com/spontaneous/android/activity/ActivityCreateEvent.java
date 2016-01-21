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
import com.spontaneous.android.util.Logger;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Using this activity, the user can create a new event.
 */
public class ActivityCreateEvent extends BaseActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @SuppressWarnings("FieldCanBeLocal")
    private final String ACTIVITY_TITLE = "Create Event";

    private PlacesAutoCompleteAdapter mPlacesAutoCompleteAdapter;
    private Calendar mCalendar;

    //Views
    private EditText mEventTitle;
    private EditText mEventDescription;
    private AutoCompleteTextView mEventLocation;
    private RecipientEditTextView mInvitedUsers;
    private EditText mEventDate;
    private EditText mEventTime;

    /**
     * Create the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set toolbar
        setToolbarMessage(ACTIVITY_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        mEventTitle = (EditText) findViewById(R.id.create_event_title);
        mEventDescription = (EditText) findViewById(R.id.create_event_description);
        mPlacesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this);
        mInvitedUsers = (RecipientEditTextView) findViewById(R.id.create_event_invited_users);
        mEventDate = (EditText) findViewById(R.id.create_event_date);

        //Initialize places autocompletion
        mEventLocation = (AutoCompleteTextView) findViewById(R.id.create_event_location);
        mEventLocation.setAdapter(mPlacesAutoCompleteAdapter);

        //Initialize contacts bubble autocompletion
        mInvitedUsers.setTokenizer(new Rfc822Tokenizer());
        mInvitedUsers.setAdapter(new BaseRecipientAdapter(
                BaseRecipientAdapter.QUERY_TYPE_EMAIL,
                this
        ));

        //Initialize date dialog
        mCalendar = Calendar.getInstance();
        mEventDate.setOnTouchListener(showDateTimePickerDialog(
                DatePickerDialog.newInstance(
                        ActivityCreateEvent.this,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)
                )));

        //Initialize time dialog
        mEventTime = (EditText) findViewById(R.id.create_event_time);
        mEventTime.setOnTouchListener(showDateTimePickerDialog(
                TimePickerDialog.newInstance(
                        ActivityCreateEvent.this,
                        mCalendar.get(Calendar.HOUR_OF_DAY),
                        mCalendar.get(Calendar.MINUTE),
                        true
                )));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_event;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Handle toolbar button clicks
        switch (item.getItemId()) {

            //Go back
            case android.R.id.home:
                Logger.info("Navigating back to main activity");
                finish();
                return true;

            //Submit event
            case R.id.event_done_button:
                submitEvent();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @param picker To show.
     * @return OnClick listener that shows date time picker dialog.
     */
    private View.OnTouchListener showDateTimePickerDialog(final DialogFragment picker) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP != event.getAction()) {
                    return false;
                }

                picker.show(getFragmentManager(), Logger.getTag());
                return true;
            }
        };
    }

    /**
     * Handle date set in picker dialog.
     */
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, E");

        mCalendar.set(year, monthOfYear, dayOfMonth);

        mEventDate.setText(dateFormat.format(mCalendar.getTime()));
    }

    /**
     * Handle time set in picker dialog.
     */
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");

        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, second);

        mEventTime.setText(dateFormat.format(mCalendar.getTime()));
    }

    /**
     * @return Datetime from {@link #mCalendar} field.
     */
    private DateTime getDateTime() {
        return new DateTime(mCalendar.getTime());
    }

    /**
     * @return List of emails of invited users - from {@link #mInvitedUsers}.
     */
    private List<String> getInvitedUsersList() {

        //Get the array of invited users.
        DrawableRecipientChip[] recipientChips = mInvitedUsers.getRecipients();

        //Create a new ArrayList and preallocate its size.
        ArrayList<String> emails = new ArrayList<>(recipientChips.length);

        //Iterate over the emails in the array, and add them to the list.
        for (DrawableRecipientChip chip : recipientChips) {
            emails.add(chip.getEntry().getDestination());
        }

        return emails;
    }

    /**
     * @return New event generated from the user input.
     */
    private CreateEventRequest generateEvent() {
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
     * Submit the event.
     */
    private void submitEvent() {
        final CreateEventRequest event = generateEvent();
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
