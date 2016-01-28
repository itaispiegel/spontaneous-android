package com.spontaneous.android.view;

import android.app.Activity;
import android.content.Context;
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
import com.spontaneous.android.adapter.InvitedUsersListAdapter;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.util.Logger;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * This is a representational view for an event.
 */
public class EventCard extends FrameLayout implements AdapterView.OnItemClickListener {

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
    private TextView eventTitleTextView;

    /**
     * Event description TextView.
     */
    private TextView eventDescriptionTextView;

    /**
     * Event date TextView.
     */
    private TextView eventDateTextView;

    /**
     * Event location TextView.
     */
    private TextView eventLocationTextView;

    /**
     * Event map NetworkImageView.
     */
    private NetworkImageView eventMapImage;

    /**
     * ListViewAdapter for the invited users.
     */
    private InvitedUsersListAdapter invitedUsersListAdapter;

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
        eventTitleTextView = (TextView) layout.findViewById(R.id.title);
        eventDescriptionTextView = (TextView) layout.findViewById(R.id.description);
        eventDateTextView = (TextView) layout.findViewById(R.id.when);
        eventLocationTextView = (TextView) layout.findViewById(R.id.where_textView);

        eventMapImage = (NetworkImageView) layout.findViewById(R.id.where_map);

        invitedUsersListAdapter = new InvitedUsersListAdapter(mContext);

        ListView invitedUserListView = (ListView) layout.findViewById(R.id.invited_list);
        invitedUserListView.setAdapter(invitedUsersListAdapter);
        invitedUserListView.setOnItemClickListener(this);
    }

    /**
     * Set the view's content according to the given event.
     * @param event Event to display on card.
     */
    private void setEvent(final Event event) {

        //Set views text
        eventTitleTextView.setText(event.getTitle());
        eventDescriptionTextView.setText(event.getDescription());
        eventLocationTextView.setText(
                mContext.getString(R.string.event_page_location, event.getLocation())
        );

        //Set map image url and click listener.
        //On click open a navigation app.
        eventMapImage.setImageUrl(getMapUrl(event), SpontaneousApplication.getInstance().getImageLoader());
        eventMapImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(GOOGLE_MAPS_NAVIGATION_API_URL + event.getLocation())
                );

                getContext().startActivity(intent);
            }
        });

        invitedUsersListAdapter.addAll(event.getInvitedUsers());

        DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd-MM-yyyy, H:m");
        eventDateTextView.setText(mContext.getString(
                R.string.event_page_date, dateFormat.print(event.getDate())
        ));
    }

    /**
     * Get URL of of map image.
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
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        Logger.info("Invited user was clicked: " + invitedUsersListAdapter.getItem(position));

        UpdateInvitedUserDialog dialog = new UpdateInvitedUserDialog();
        dialog.show(((Activity) mContext).getFragmentManager(), "");
    }

    public InvitedUsersListAdapter getInvitedUsersListAdapter() {
        return invitedUsersListAdapter;
    }
}
