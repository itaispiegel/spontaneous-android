package com.spontaneous.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

import com.spontaneous.android.R;
import com.spontaneous.android.adapter.PlacesAutoCompleteAdapter;
import com.spontaneous.android.util.ActivityUtils;
import com.spontaneous.android.util.Logger;

/**
 * Created by Itai on 23-Nov-15.
 */
public class ActivityCreateEvent extends Activity {

    private PlacesAutoCompleteAdapter mPlacesAutoCompleteAdapter;

    //Views
    private AutoCompleteTextView eventLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        ActivityUtils.setCustomActionBar(this);

        mPlacesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, android.R.layout.simple_list_item_1);
        eventLocation = (AutoCompleteTextView) findViewById(R.id.create_event_location);
        eventLocation.setAdapter(mPlacesAutoCompleteAdapter);

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
}
