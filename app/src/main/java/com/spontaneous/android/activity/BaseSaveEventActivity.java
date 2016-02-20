package com.spontaneous.android.activity;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.util.Rfc822Tokenizer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.android.ex.chips.BaseRecipientAdapter;
import com.android.ex.chips.RecipientEditTextView;
import com.android.ex.chips.recipientchip.DrawableRecipientChip;
import com.spontaneous.android.R;
import com.spontaneous.android.adapter.PlacesAutoCompleteAdapter;
import com.spontaneous.android.http.request.model.SaveEventRequest;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.DateTimeFormatter;
import com.spontaneous.android.util.Logger;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;
import static com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance;
import static com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;
import static com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance;

/**
 * This is a base activity for creating/editing an event.
 */
public abstract class BaseSaveEventActivity extends BaseActivity implements OnDateSetListener, OnTimeSetListener {


    protected final String dateFormat = "dd/MM/yyyy, E";
    protected final String timeFormat = "HH:mm";

    private Calendar mCalendar;

    //Views
    protected EditText mEventTitle;
    protected EditText mEventDescription;
    protected AutoCompleteTextView mEventLocation;
    protected RecipientEditTextView mInvitedUsers;
    protected EditText mEventDate;
    protected EditText mEventTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set toolbar
        showBackButtonOnToolbar();

        // Initialize views
        mEventTitle = (EditText) findViewById(R.id.create_event_title);
        mEventDescription = (EditText) findViewById(R.id.create_event_description);
        mInvitedUsers = (RecipientEditTextView) findViewById(R.id.create_event_invited_users);
        mEventDate = (EditText) findViewById(R.id.create_event_date);

        //Initialize places autocompletion
        PlacesAutoCompleteAdapter mPlacesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this);
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
                newInstance(
                        BaseSaveEventActivity.this,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)
                )));

        //Initialize time dialog
        mEventTime = (EditText) findViewById(R.id.create_event_time);
        mEventTime.setOnTouchListener(showDateTimePickerDialog(
                newInstance(
                        BaseSaveEventActivity.this,
                        mCalendar.get(Calendar.HOUR_OF_DAY),
                        mCalendar.get(Calendar.MINUTE),
                        true
                )));

        initializeViews();
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
        mCalendar.set(year, monthOfYear, dayOfMonth);
        mEventDate.setText(DateTimeFormatter.format(dateFormat, mCalendar.getTime()));
    }

    /**
     * Handle time set in picker dialog.
     */
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, second);

        mEventTime.setText(DateTimeFormatter.format(timeFormat, mCalendar.getTime()));
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
     * @return New create event request generated from the user input.
     */
    protected SaveEventRequest generateEditEventRequest() {
        return new SaveEventRequest(
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
    abstract void submitEvent();

    /**
     * Initialize the values of the views in the activity.
     */
    abstract void initializeViews();
}
