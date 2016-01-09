package com.spontaneous.android.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.spontaneous.android.R;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.util.Logger;
import com.spontaneous.android.view.EventCard;

/**
 * This activity is a representational page of an event.
 */
public class ActivityEventPage extends BaseActivity {

    private Event mEvent;

    private FrameLayout mCardContainer;
    private ScrollView mScroll;
    private View mEventCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEvent = (Event) getIntent().getExtras().getSerializable(getString(R.string.event_card_intent_extras));

        //Set back button on toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //Set toolbar of the activity to event title
            if (mEvent != null) {
                setToolbarMessage(mEvent.getTitle());
            }
        }

        mScroll = (ScrollView) findViewById(R.id.scroll);

        mCardContainer = (FrameLayout) findViewById(R.id.card);
        mEventCard = new EventCard(this, mEvent);

        mCardContainer.addView(mEventCard);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        //Handle toolbar button clicks
        switch (item.getItemId()) {

            //Go back
            case android.R.id.home:
                Logger.info("Navigating back to main activity");
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
