package com.spontaneous.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.activity.ActivityEventPage;
import com.spontaneous.android.activity.BaseActivity;
import com.spontaneous.android.adapter.EventListAdapter;
import com.spontaneous.android.http.request.service.EventService;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.model.Guest;
import com.spontaneous.android.model.User;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.Logger;
import com.spontaneous.android.util.UserInterfaceUtils;

import java.util.Locale;

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

        ListView eventsListView = (ListView) layout.findViewById(R.id.events_listview);
        eventsListView.setAdapter(mEventListAdapter);
        eventsListView.setEmptyView(mListEmptyView);

        //OnClickListener, OnLongClickListener and OnScrollListener
        eventsListView.setOnItemClickListener(itemClickListener());
        eventsListView.setOnItemLongClickListener(itemLongClickListener());
        eventsListView.setOnScrollListener(UserInterfaceUtils.listViewScrollListener(eventsListView));

        return layout;
    }

    /**
     * When the user clicks on an event in the listview, open the event page.
     *
     * @return The {@link View.OnClickListener}.
     */
    private AdapterView.OnItemClickListener itemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Logger.info("Event at position #" + position + " was clicked.");

                Event event = mEventListAdapter.getItem(position);

                Intent eventActivity = new Intent(getContext(), ActivityEventPage.class);
                eventActivity.putExtra(getString(R.string.event_card_intent_extras), event);

                startActivity(eventActivity);
            }
        };
    }

    /**
     * When the user long clicks an event in the listview, delete it.
     *
     * @return The {@link AdapterView.OnItemLongClickListener}.
     */
    private AdapterView.OnItemLongClickListener itemLongClickListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, final int position, long id) {

                Event event = mEventListAdapter.getItem(position);
                User authenticatedUser = AccountUtils.getAuthenticatedUser();

                //If the authenticated is not the host, remove the authenticated user as the guest.
                if (!event.getHost().equals(authenticatedUser)) {
                    Guest self = event.getGuestByUser(authenticatedUser);

                    if (self == null) {
                        return true;
                    }

                    ((BaseActivity) getActivity()).showWaitDialog();

                    SpontaneousApplication.getInstance()
                            .getService(EventService.class)
                            .deleteGuest(self.getId(), new Callback<BaseResponse>() {
                                @Override
                                public void success(BaseResponse baseResponse, Response response) {
                                    ((BaseActivity) getActivity()).dismissDialog();

                                    Logger.info("You left the event.");
                                    Toast.makeText(getContext(), "You left the event", Toast.LENGTH_SHORT)
                                            .show();

                                    mEventListAdapter.remove(position);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    ((BaseActivity) getActivity()).dismissDialog();

                                    Toast.makeText(getActivity(), "Error while trying to leave the event", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });

                    return true;
                }

                ((BaseActivity) getActivity()).showWaitDialog();

                Logger.info(String.format(Locale.getDefault(), "Deleting item at position #%d.", position));

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
