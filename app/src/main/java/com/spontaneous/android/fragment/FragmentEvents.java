package com.spontaneous.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.spontaneous.android.R;
import com.spontaneous.android.adapter.EventListAdapter;
import com.spontaneous.android.model.Event;

import java.util.Collection;

/**
 * Created by Itai on 17-Nov-15.
 */
public class FragmentEvents extends Fragment {

    private Collection<Event> events;

    private ListView mEventsListView;
    private View mListEmptyView;

    private EventListAdapter mEventListAdapter;

    private FrameLayout mCardContainer;
    private ScrollView mScroll;
    private View mEventCard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_events, container, false);

        mListEmptyView = layout.findViewById(R.id.empty);

        mEventListAdapter = new EventListAdapter(getActivity());
        mEventsListView = (ListView) layout.findViewById(R.id.events_listview);
        mEventsListView.setAdapter(mEventListAdapter);
        mEventsListView.setEmptyView(mListEmptyView);

        return layout;
    }
}
