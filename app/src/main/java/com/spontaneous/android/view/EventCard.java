package com.spontaneous.android.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.adapter.GuestsListAdapter;
import com.spontaneous.android.adapter.ItemsListAdapter;
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

    private Context mContext;
    private Event mEvent;

    //Google maps API constants.
    @SuppressWarnings("FieldCanBeLocal")
    private final String GOOGLE_MAPS_STATIC_MAPS_API_URL = "https://maps.googleapis.com/maps/api/staticmap?";
    private final String GOOGLE_MAPS_NAVIGATION_API_URL = "http://maps.google.com/maps?q=";

    // Views
    private TextView mEventTitleTextView;
    private TextView mEventDescriptionTextView;
    private TextView mEventDateTextView;
    private TextView mEventLocationTextView;
    private NetworkImageView mEventMapImage;

    private GuestsListAdapter mGuestsListAdapter;
    private ListView mGuestsListView;

    private ItemsListAdapter mItemsListAdapter;
    private ListView mItemsListView;
    private TextView mItemsTextView;

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

        mItemsListAdapter = new ItemsListAdapter(mContext);

        mItemsListView = (ListView) layout.findViewById(R.id.event_card_items_list);
        mItemsListView.setAdapter(mItemsListAdapter);

        mItemsTextView = (TextView) layout.findViewById(R.id.event_card_items_text);
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

        //Populate the guests list view.
        mGuestsListAdapter.addAll(event.getGuests());
        UserInterfaceUtils.setListViewHeightBasedOnChildren(mGuestsListView);

        //Populate the items listview.
        mItemsListAdapter.addAll(event.getItems());
        UserInterfaceUtils.setListViewHeightBasedOnChildren(mItemsListView);

        //If items list view is not empty, show the text view.
        if (!mItemsListAdapter.isEmpty()) {
            mItemsTextView.setVisibility(VISIBLE);
        } else {
            mItemsTextView.setVisibility(GONE);
        }

        //Show the date of the event in format.
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

        //If the authenticated user clicked is the host, and he clicked on a guest, assign an item to the guest.
        if (!guest.getUserProfile().equals(authenticatedUser)) {
            if (mEvent.getHost().equals(authenticatedUser)) {
                showItemAssignmentDialog();
            }

            //Return void in any case that the authenticated user clicks on another guest.
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

    public void showItemAssignmentDialog() {

        final EditText userInput = new EditText(mContext);

        Dialog.OnClickListener onPositiveClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Assigning item.", Toast.LENGTH_SHORT)
                        .show();
            }
        };

        UserInterfaceUtils.showAlertDialog(getContext(), "Assign an item", "OK", "Cancel", onPositiveClick, userInput);
    }
}
