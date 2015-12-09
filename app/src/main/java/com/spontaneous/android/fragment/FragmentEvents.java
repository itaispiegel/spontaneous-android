package com.spontaneous.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.spontaneous.android.R;
import com.spontaneous.android.activity.ActivityEventPage;
import com.spontaneous.android.adapter.EventListAdapter;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.util.Logger;

/**
 * This fragment holds a listview showing past events relating to the user.
 */
public class FragmentEvents extends Fragment {

    private ListView mEventsListView;
    private View mListEmptyView;

    private EventListAdapter mEventListAdapter;

    private FrameLayout mCardContainer;
    private ScrollView mScroll;
    private View mEventCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_events, container, false);

        mListEmptyView = layout.findViewById(R.id.empty);

        mEventListAdapter = new EventListAdapter(getActivity());
        mEventsListView = (ListView) layout.findViewById(R.id.events_listview);
        mEventsListView.setAdapter(mEventListAdapter);
        mEventsListView.setEmptyView(mListEmptyView);
        mEventsListView.setOnItemClickListener(getListViewClickListener());

        return layout;
    }

    /**
     * When the user clicks on an event in the listview, open the event page.
     */
    private AdapterView.OnItemClickListener getListViewClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Logger.info(
                        String.format("Event (%d) clicked", position)
                );

                Event event = mEventListAdapter.getItem(position);

                Intent eventActivity = new Intent(getContext(), ActivityEventPage.class);
                eventActivity.putExtra(getString(R.string.event_card_intent_extras), event);

                startActivity(eventActivity);
            }
        };
    }

    public void addEvent(Event event) {
        mEventListAdapter.add(event);
    }
}
