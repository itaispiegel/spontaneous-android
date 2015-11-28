package com.spontaneous.android.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.util.Rfc822Tokenizer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.android.ex.chips.BaseRecipientAdapter;
import com.android.ex.chips.RecipientEditTextView;
import com.spontaneous.android.R;
import com.spontaneous.android.adapter.PlacesAutoCompleteAdapter;
import com.spontaneous.android.util.Logger;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Itai on 23-Nov-15.
 */
public class ActivityCreateEvent extends BaseActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private PlacesAutoCompleteAdapter mPlacesAutoCompleteAdapter;

    private Calendar calendar;

    //Views
    private AutoCompleteTextView eventLocation;
    private RecipientEditTextView invitedUsers;
    private EditText eventDate;
    private EditText eventTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlacesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, android.R.layout.simple_list_item_1);
        eventLocation = (AutoCompleteTextView) findViewById(R.id.create_event_location);
        eventLocation.setAdapter(mPlacesAutoCompleteAdapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        invitedUsers = (RecipientEditTextView) findViewById(R.id.create_event_invited_users);
        invitedUsers.setTokenizer(new Rfc822Tokenizer());
        invitedUsers.setAdapter(new BaseRecipientAdapter(BaseRecipientAdapter.QUERY_TYPE_EMAIL, this));

        calendar = Calendar.getInstance();

        eventDate = (EditText) findViewById(R.id.create_event_date);
        eventDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP != event.getAction()) {
                    return false;
                }

                DatePickerDialog datePicker = DatePickerDialog.newInstance(
                        ActivityCreateEvent.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                datePicker.show(getFragmentManager(), "Spontaneous");
                return true;
            }
        });

        eventTime = (EditText) findViewById(R.id.create_event_time);
        eventTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP != event.getAction()) {
                    return false;
                }

                TimePickerDialog datePicker = TimePickerDialog.newInstance(
                        ActivityCreateEvent.this,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                );

                datePicker.show(getFragmentManager(), "Spontaneous");
                return true;
            }
        });
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

        switch (item.getItemId()) {
            case android.R.id.home:
                Logger.info("Navigating back to main activity");
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);

        String dayOfWeek = calendar.getDisplayName(
                Calendar.DAY_OF_WEEK,
                Calendar.SHORT,
                Locale.getDefault()
        );

        String date = dayOfMonth + "/" + (++monthOfYear) + "/" + year + ", " + dayOfWeek;
        eventDate.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        String time = hourOfDay + ":" + minute;
        eventTime.setText(time);
    }
}
