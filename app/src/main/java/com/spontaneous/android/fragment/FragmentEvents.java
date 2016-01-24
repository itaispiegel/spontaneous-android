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
import android.widget.Toast;

import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.activity.ActivityEventPage;
import com.spontaneous.android.activity.BaseActivity;
import com.spontaneous.android.adapter.EventListAdapter;
import com.spontaneous.android.http.request.service.EventService;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.util.Logger;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This fragment holds a listview showing past events relating to the user.
 */
public class FragmentEvents extends Fragment {

    private EventListAdapter mEventListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_events, container, false);

        View mListEmptyView = layout.findViewById(R.id.empty);

        mEventListAdapter = new EventListAdapter(getActivity());
        ListView mEventsListView = (ListView) layout.findViewById(R.id.events_listview);
        mEventsListView.setAdapter(mEventListAdapter);
        mEventsListView.setEmptyView(mListEmptyView);

        //OnClickListener and OnLongClickListener
        mEventsListView.setOnItemClickListener(itemClickListener());
        mEventsListView.setOnItemLongClickListener(itemLongClickListener());

        return layout;
    }

    /**
     * When the user clicks on an event in the listview, open the event page.
     */
    private AdapterView.OnItemClickListener itemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Logger.info("Event at position (" + position + ") was clicked.");

                Event event = mEventListAdapter.getItem(position);

                Intent eventActivity = new Intent(getContext(), ActivityEventPage.class);
                eventActivity.putExtra(getString(R.string.event_card_intent_extras), event);

                startActivity(eventActivity);
            }
        };
    }

    private AdapterView.OnItemLongClickListener itemLongClickListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, final int position, long id) {

                ((BaseActivity) getActivity()).showWaitDialog();

                Logger.info(String.format("Deleting item at position #%d.", position));
                Event event = mEventListAdapter.getItem(position);

                SpontaneousApplication.getInstance().getService(EventService.class)
                        .deleteEvent(event.getId(), new Callback<BaseResponse>() {
                            @Override
                            public void success(BaseResponse baseResponse, Response response) {
                                ((BaseActivity) getActivity()).dismissDialog();

                                Logger.info("Event deleted successfully.");
                                mEventListAdapter.remove(position);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                ((BaseActivity) getActivity()).dismissDialog();

                                Toast.makeText(getActivity(), "Error while trying to delete event.", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });

                return true;
            }
        };
    }

    /**
     * @return The event list adapter.
     */
    public EventListAdapter getEventListAdapter() {
        return mEventListAdapter;
    }
}
