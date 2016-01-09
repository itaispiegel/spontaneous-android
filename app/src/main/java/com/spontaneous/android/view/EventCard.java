package com.spontaneous.android.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.spontaneous.android.Application;
import com.spontaneous.android.R;
import com.spontaneous.android.adapter.InvitedUsersListAdapter;
import com.spontaneous.android.model.Event;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * This is a representational view for an event.
 */
public class EventCard extends FrameLayout {

    /**
     * The event representing in the card.
     */
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
     * Event invited users ListView.
     */
    private ListView invitedUserListView;

    /**
     * ListViewAdapter for the {@link EventCard#invitedUserListView}
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
        this.mEvent = event;

        setEvent(event);
    }

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

        invitedUserListView = (ListView) layout.findViewById(R.id.invited_list);
        invitedUserListView.setAdapter(invitedUsersListAdapter);
    }

    private void setEvent(final Event event) {

        //Set views text
        eventTitleTextView.setText(event.getTitle());
        eventDescriptionTextView.setText(event.getDescription());
        eventLocationTextView.setText(
                mContext.getString(R.string.event_page_location, event.getLocation())
        );

        //Set map image url and click listener.
        //On click open a navigation app.
        eventMapImage.setImageUrl(getMapURL(event), Application.getInstance().getImageLoader());
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

    private String getMapURL(final Event event) {
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
}
