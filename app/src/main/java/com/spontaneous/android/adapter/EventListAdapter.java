package com.spontaneous.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spontaneous.android.R;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.model.User;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.DateTimeFormatter;

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
    private final List<Event> mEvents;

    /**
     * Initialize a new EventListAdapter.
     *
     * @param mContext of the activity.
     */
    public EventListAdapter(Context mContext) {
        this.mContext = mContext;
        this.mEvents = new ArrayList<>();
    }

    /**
     * Add a new event to the adapter.
     *
     * @param event The event to add.
     */
    public void add(Event event) {
        mEvents.add(event);
        notifyDataSetChanged();
    }

    /**
     * Add a collection of events to the adapter.
     *
     * @param events The collection to add.
     */
    public void addAll(Collection<Event> events) {
        mEvents.addAll(events);
        notifyDataSetChanged();
    }

    /**
     * Add a new event to the adapter.
     * @param location Index to add at.
     * @param event Event entity to add.
     */
    public void add(int location, Event event) {
        mEvents.add(location, event);
        notifyDataSetChanged();
    }

    /**
     * Remove the event at the given index.
     *
     * @param index To remove at.
     */
    public void remove(int index) {
        mEvents.remove(index);
        notifyDataSetChanged();
    }

    /**
     * Clears the collection.
     */
    public void clear() {
        mEvents.clear();
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
        } if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_events, parent, false);
        }

        User authenticatedUser = AccountUtils.getAuthenticatedUser();

        //Initialize views
        TextView eventTitle = (TextView) convertView.findViewById(R.id.title);
        TextView eventDescription = (TextView) convertView.findViewById(R.id.description);
        ImageView isUserAttending = (ImageView) convertView.findViewById(R.id.is_attending);
        TextView eventDate = (TextView) convertView.findViewById(R.id.when);
        TextView eventLocation = (TextView) convertView.findViewById(R.id.location);

        //Set event details
        Event currEvent = mEvents.get(position);

        eventTitle.setText(currEvent.getTitle());
        eventDescription.setText(currEvent.getDescription());
        eventDate.setText(DateTimeFormatter.format(currEvent.getDate()));
        eventLocation.setText(currEvent.getLocation());

        isUserAttending.setImageDrawable(currEvent.isUserAttending(authenticatedUser)
                        ? mContext.getDrawable(R.drawable.ic_done_black)
                        : mContext.getDrawable(R.drawable.ic_close_black)
        );

        return convertView;
    }
}
