package com.spontaneous.android.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spontaneous.android.R;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.model.InvitedUser;
import com.spontaneous.android.model.User;
import com.spontaneous.android.util.AccountUtils;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Adapter for EventListView.
 */
public class EventListAdapter extends BaseAdapter {

    /**
     * Context of the activity.
     */
    private final Context mContext;

    /**
     * Connect between adapter logic and view.
     */
    private LayoutInflater mInflater;

    /**
     * List of events to include.
     */
    private List<Event> mEvents;


    public EventListAdapter(Context mContext) {
        this.mContext = mContext;
        this.mEvents = new ArrayList<>();
    }

    public void add(Event event) {
        mEvents.add(event);
        notifyDataSetChanged();
    }

    public void addAll(List<Event> events) {
        mEvents.addAll(events);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public Event getItem(int position) {
        return mEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_events, parent, false);
        }

        //Initialize views
        TextView eventTitle = (TextView) convertView.findViewById(R.id.title);
        TextView eventDescription = (TextView) convertView.findViewById(R.id.description);
        ImageView isUserAttending = (ImageView) convertView.findViewById(R.id.is_attending);
        TextView eventDate = (TextView) convertView.findViewById(R.id.when);
        TextView eventLocation = (TextView) convertView.findViewById(R.id.location);

        //Set event details
        Event currEvent = mEvents.get(position);

        DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd-MM-yyyy");

        eventTitle.setText(currEvent.getTitle());
        eventDescription.setText(currEvent.getDescription());
        eventDate.setText(dateFormat.print(
                currEvent.getDate()
        ));
        eventLocation.setText(currEvent.getLocation());

        //TODO: Set whether authenticated user is attending the event.

        return convertView;
    }
}
