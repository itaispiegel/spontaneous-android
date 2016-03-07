package com.spontaneous.android.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.adapter.GuestsListAdapter;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.model.Guest;
import com.spontaneous.android.model.User;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.DateTimeFormatter;
import com.spontaneous.android.util.Logger;
import com.spontaneous.android.util.UserInterfaceUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * This is a representational view for an event.
 */
public class EventCard extends FrameLayout implements AdapterView.OnItemClickListener {

    private Event mEvent;

    /**
     * Context of the application.
     */
    private Context mContext;

    //Google maps API constants.
    private final String GOOGLE_MAPS_NAVIGATION_API_URL = "http://maps.google.com/maps?q=";
    private final String GOOGLE_MAPS_STATIC_MAPS_API_URL = "https://maps.googleapis.com/maps/api/staticmap?";

    /**
     * Event title TextView.
     */
    private TextView mEventTitleTextView;

    /**
     * Event description TextView.
     */
    private TextView mEventDescriptionTextView;

    /**
     * Event date TextView.
     */
    private TextView mEventDateTextView;

    /**
     * Event location TextView.
     */
    private TextView mEventLocationTextView;

    /**
     * Event map NetworkImageView.
     */
    private NetworkImageView mEventMapImage;

    /**
     * ListViewAdapter for the guests.
     */
    private GuestsListAdapter mGuestsListAdapter;
    private ListView mGuestsListView;

    public EventCard(Context context) {
        super(context);
        this.mContext = context;

        init();
    }

    public EventCard(Context context, Event event) {
        this(context);

        this.mContext = context;
        setEvent(event);
    }

    /**
     * Initialize the views in the EventCard.
     */
    private void init() {

        //Inflate the card.
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.view_event_card, this);

        //Initialize views in card.
        mEventTitleTextView = (TextView) layout.findViewById(R.id.event_card_title);
        mEventDescriptionTextView = (TextView) layout.findViewById(R.id.event_card_description);
        mEventDateTextView = (TextView) layout.findViewById(R.id.event_card_date);
        mEventLocationTextView = (TextView) layout.findViewById(R.id.event_card_location);

        mEventMapImage = (NetworkImageView) layout.findViewById(R.id.event_card_map);

        //Initialize guests list view.
        mGuestsListAdapter = new GuestsListAdapter(mContext);

        mGuestsListView = (ListView) layout.findViewById(R.id.event_card_guests_list);
        mGuestsListView.setAdapter(mGuestsListAdapter);
        mGuestsListView.setOnItemClickListener(this);
    }

    /**
     * Set the view's content according to the given event.
     *
     * @param event Event to display on card.
     */
    private void setEvent(final Event event) {

        //Set the event field.
        this.mEvent = event;

        //Set views text
        mEventTitleTextView.setText(event.getTitle());
        mEventDescriptionTextView.setText(event.getDescription());
        mEventLocationTextView.setText(
                mContext.getString(R.string.event_page_location, event.getLocation())
        );

        //Set map image url and click listener.
        //On click open a navigation app.
        mEventMapImage.setImageUrl(getMapUrl(event), SpontaneousApplication.getInstance().getImageLoader());
        mEventMapImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(GOOGLE_MAPS_NAVIGATION_API_URL + event.getLocation())
                );

                getContext().startActivity(intent);
            }
        });

        mGuestsListAdapter.addAll(event.getGuests());
        UserInterfaceUtils.setListViewHeightBasedOnChildren(mGuestsListView);

        mEventDateTextView.setText(mContext.getString(
                R.string.event_page_date, DateTimeFormatter.format(event.getDate())
        ));
    }

    /**
     * Get URL of of map image.
     *
     * @param event Event to get map to.
     * @return The URL of the map.
     */
    private String getMapUrl(final Event event) {
        final String defaultEncoding = "UTF-8";

        //Encode the address so the API will retrieve a valid image
        String addressEncoded;
        try {
            addressEncoded = URLEncoder.encode(event.getLocation(), defaultEncoding);
        } catch (UnsupportedEncodingException e) {
            addressEncoded = event.getLocation();
        }

        return GOOGLE_MAPS_STATIC_MAPS_API_URL +
                "center=" + addressEncoded +
                "&size=800x500&zoom=17&" +
                "markers=" + addressEncoded + "&" +
                "key=" + mContext.getString(R.string.google_api_key);
    }

    @Override
    public void onItemClick(final AdapterView parent, final View view, final int position, final long id) {
        Logger.info("Guest was clicked: " + mGuestsListAdapter.getItem(position));

        //Show the dialog if the user clicked himself.
        Guest guest = mGuestsListAdapter.getItem(position);
        User authenticatedUser = AccountUtils.getAuthenticatedUser();

        if (!guest.getUserProfile().equals(authenticatedUser)) {
            return;
        }

        //Show the dialog.
        final UpdateGuestDialog dialog = new UpdateGuestDialog(getContext(), guest);
        dialog.show();

        //Update the listview.
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Guest updated = ((UpdateGuestDialog) dialog).getGuest();
                mGuestsListAdapter.updateGuest(position, updated);
            }
        });
    }

    public GuestsListAdapter getGuestsListAdapter() {
        return mGuestsListAdapter;
    }
}
