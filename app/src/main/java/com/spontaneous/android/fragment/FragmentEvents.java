package com.spontaneous.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.spontaneous.android.R;
import com.spontaneous.android.adapter.EventListAdapter;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.model.InvitedUser;
import com.spontaneous.android.model.User;
import com.spontaneous.android.util.AccountUtils;

import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Itai on 17-Nov-15.
 */
public class FragmentEvents extends Fragment {

    private ListView mEventsListView;
    private View mListEmptyView;

    private EventListAdapter mEventListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_events, container, false);

        mListEmptyView = layout.findViewById(R.id.empty);

        mEventListAdapter = new EventListAdapter(getActivity());
        mEventListAdapter.addAll(getDummyEvents());

        mEventsListView = (ListView) layout.findViewById(R.id.events_listview);
        mEventsListView.setAdapter(mEventListAdapter);
        mEventsListView.setEmptyView(mListEmptyView);

        return layout;
    }

    private List<Event> getDummyEvents() {
        Event event = new Event();

        User user = AccountUtils.getAuthenticatedUser();
        List<InvitedUser> invited = new LinkedList<>();
        invited.add(new InvitedUser(user, "It's gona be LEGENDARY", true));

        event.setDescription("Having fun at the mall");
        event.setHost(user);
        event.setInvitedUsers(invited);
        event.setTitle("Fun at the mall");
        event.setWhen(new DateTime());
        event.setWhere("At the mall");
        event.setId(1);

        LinkedList<Event> events = new LinkedList<>();
        events.add(event);

        return events;
    }
}
