package com.spontaneous.android.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.spontaneous.android.R;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.model.User;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.Logger;
import com.spontaneous.android.view.EventCard;

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
            getMenuInflater().inflate(R.menu.event_page_menu, menu);
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
                //TODO: Launch event edit activity.
                Toast.makeText(ActivityEventPage.this, "Editing event...", Toast.LENGTH_SHORT)
                        .show();
        }

        return super.onOptionsItemSelected(item);
    }
}