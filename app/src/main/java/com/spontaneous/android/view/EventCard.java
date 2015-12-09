package com.spontaneous.android.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.spontaneous.android.Application;
import com.spontaneous.android.R;
import com.spontaneous.android.model.Event;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * This is a representational view for an event.
 */
public class EventCard extends FrameLayout {

    private Event mEvent;
    private Context mContext;

    /**
     * Google maps API string constants.
     */
    private final String GOOGLE_MAPS_NAVIGATION_API_URL = "http://maps.google.com/maps?q=";
    private final String GOOGLE_MAPS_STATIC_MAPS_API_URL = "https://maps.googleapis.com/maps/api/staticmap?";

    private TextView mTitle;
    private TextView mDescription;
    private TextView mWhen;
    private TextView mWhere;

    private NetworkImageView mEventImageView;

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
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.view_event_card, this);

        mTitle = (TextView) layout.findViewById(R.id.title);
        mDescription = (TextView) layout.findViewById(R.id.description);
        mWhen = (TextView) layout.findViewById(R.id.when);
        mWhere = (TextView) layout.findViewById(R.id.where_textView);

        mEventImageView = (NetworkImageView) layout.findViewById(R.id.where_map);
    }

    private void setEvent(final Event event) {

        //Set views text
        mTitle.setText(event.getTitle());
        mDescription.setText(event.getDescription());
        mWhen.setText(mContext.getString(
                        R.string.event_page_date, event.getDate())
        );

        mWhere.setText(mContext.getString(
                        R.string.event_page_location, event.getLocation())
        );

        //Set map
        //Encode the address so the API will retrieve a valid image
        String addressEncoded;
        try {
            addressEncoded = URLEncoder.encode(event.getLocation(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            addressEncoded = event.getLocation();
        }

        String mapURL = GOOGLE_MAPS_STATIC_MAPS_API_URL +
                "center=" + addressEncoded +
                "&size=800x500&zoom=17&" +
                "markers=" + addressEncoded + "&" +
                "key=" + mContext.getString(R.string.google_api_key);

        mEventImageView.setImageUrl(mapURL, Application.getInstance().getImageLoader());
        mEventImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_MAPS_NAVIGATION_API_URL + event.getLocation()));
                getContext().startActivity(intent);
            }
        });
    }
}
