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

import java.util.LinkedList;
import java.util.List;

/**
 * Adapter for EventListView.
 */
public class EventListAdapter extends BaseAdapter {

    /**
     * Context of the activity.
     */
    private Context mContext;

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
        this.mEvents = new LinkedList<>();
    }

    public void addAll(List<Event> events) {
        if (null != events) {
            mEvents.addAll(events);
        }
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
            convertView = mInflater.inflate(R.layout.list_item_events, null);
        }

        //Declare event details
        TextView eventTitle = (TextView) convertView.findViewById(R.id.title);
        TextView eventDescription = (TextView) convertView.findViewById(R.id.description);
        ImageView userIsComing = (ImageView) convertView.findViewById(R.id.is_coming);
        TextView eventWhen = (TextView) convertView.findViewById(R.id.when);
        TextView eventWhere = (TextView) convertView.findViewById(R.id.where);

/*        //Set event details
        Event currEvent = mEvents.get(position);
        eventTitle.setText(currEvent.getTitle());
        eventDescription.setText(currEvent.getDescription());
        eventWhen.setText(currEvent.getWhen());
        eventWhere.setText(currEvent.getWhere());

        //Set whether authenticated user is coming to the event.

        User currUser = AccountUtils.getAuthenticatedUser();
        for (InvitedUser invitedUser : currEvent.getInvitedUsers()) {
            if (invitedUser.getUser().getUid() == currUser.getUid()) {
                Drawable isComingDrawable;

                if (invitedUser.isComing()) {
                    isComingDrawable = mContext.getResources().getDrawable(R.drawable.ic_cab_done_holo_light);
                } else {
                    isComingDrawable = mContext.getResources().getDrawable(R.drawable.abc_ic_clear_search_api_holo_light);
                }
                userIsComing.setImageDrawable(isComingDrawable);
            }
        }*/

        return convertView;
    }
}
