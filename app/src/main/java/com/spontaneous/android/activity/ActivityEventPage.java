package com.spontaneous.android.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.http.request.service.EventService;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.model.User;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.Logger;
import com.spontaneous.android.view.EventCard;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This activity is a representational page of an event.
 */
public class ActivityEventPage extends BaseActivity {

    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the details of the event displaying.
        mEvent = (Event) getIntent().getExtras()
                .getSerializable(getString(R.string.event_card_intent_extras));

        //Show back button on toolbar
        showBackButtonOnToolbar();

        ScrollView mScroll = (ScrollView) findViewById(R.id.scroll);

        FrameLayout mCardContainer = (FrameLayout) findViewById(R.id.card);
        EventCard eventCard = new EventCard(this, mEvent);

        mCardContainer.addView(eventCard);
        mScroll.smoothScrollTo(0, 0); //For some reason the card starts on the bottom of the activity
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_event_page;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        User authenticatedUser = AccountUtils.getAuthenticatedUser();
        User host = mEvent.getHost();

        //Show the edit button only if the authenticated user is the host.
        if (authenticatedUser.equals(host)) {
            getMenuInflater().inflate(R.menu.event_page_host_menu, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Handle toolbar button clicks
        switch (item.getItemId()) {

            //Go back to main activity.
            case android.R.id.home:
                Logger.info("Navigating back to main activity");
                finish();
                return true;

            case R.id.event_edit_button:
                Intent editEventIntent = new Intent(ActivityEventPage.this, ActivityEditEvent.class);
                editEventIntent.putExtra(getString(R.string.event_edit), mEvent);

                startActivity(editEventIntent);
                break;

            case R.id.event_broadcast_message_button:

                final EditText messageEditText = new EditText(ActivityEventPage.this);
                messageEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

                new AlertDialog.Builder(ActivityEventPage.this)
                        .setTitle("Send a notification to your guests")
                        .setView(messageEditText)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String messageContent = messageEditText.getText().toString();

                                SpontaneousApplication.getInstance().getService(EventService.class)
                                        .notifyGuests(mEvent.getId(), messageContent, new Callback<Object>() {
                                            @Override
                                            public void success(Object o, Response response) {
                                                Toast.makeText(ActivityEventPage.this, "The message was sent successfully", Toast.LENGTH_SHORT)
                                                        .show();
                                            }

                                            @Override
                                            public void failure(RetrofitError error) {
                                                Toast.makeText(ActivityEventPage.this, "The message was not sent successfully", Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
        }

        return super.onOptionsItemSelected(item);
    }
}