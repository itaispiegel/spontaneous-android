package com.spontaneous.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
import com.spontaneous.android.R;
import com.spontaneous.android.activity.BaseActivity;
import com.spontaneous.android.adapter.RideListAdapter;
import com.spontaneous.android.http.ApiRestClient;
import com.spontaneous.android.http.request.GetAllRides;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Ride;
import com.spontaneous.android.util.Logger;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Eidan on 4/26/2015.
 */
public class HomeFragment extends Fragment {

    private ViewHolder mViewHolder;
    private RideListAdapter mRideListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("HomeFragment: onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d("HomeFragment: onResume");
        refreshListView();

    }

    public void refreshListView() {
        Logger.d("HomeFragment: refreshListView");
        GetAllRides getAllRides = ApiRestClient.getRequest(getActivity(), GetAllRides.class);
        getAllRides.getAllRides(new Callback<BaseResponse<List<Ride>>>() {
            @Override
            public void success(BaseResponse<List<Ride>> baseResponse, Response response) {
                final List<Ride> rides = baseResponse.getBody();

                Logger.i("GetAllRides: Received " + rides.size() + " recipes!");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mRideListAdapter == null) {
                            Logger.d("HomeFragment: Creating new adapter");
                            mRideListAdapter = new RideListAdapter(getActivity(), rides);
                            mViewHolder.listView.setAdapter(mRideListAdapter);
                        } else {
                            Logger.d("HomeFragment: Refreshing adapter");
                            mRideListAdapter.getRides().clear();
                            mRideListAdapter.getRides().addAll(rides);
                            mRideListAdapter.notifyDataSetChanged();
                            mViewHolder.listView.invalidateViews();
                        }
                        mViewHolder.swipeContainer.setRefreshing(false);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Logger.i("GetAllRides: failed");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initViewHolder(inflater, container);
        return mViewHolder.root;
    }

    private void initViewHolder(LayoutInflater inflater, ViewGroup container) {
        if(mViewHolder == null) {
            mViewHolder = new ViewHolder();
            mViewHolder.root = inflater.inflate(R.layout.fragment_home, container, false);
            mViewHolder.swipeContainer = (SwipeRefreshLayout) mViewHolder.root.findViewById(R.id.swipeContainer);
            mViewHolder.listView = (ListView) mViewHolder.root.findViewById(R.id.list);
            mViewHolder.fab = (FloatingActionButton) mViewHolder.root.findViewById(R.id.fab);

            // Setup refresh listener which triggers new data loading
            mViewHolder.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    // Make sure you call swipeContainer.setRefreshing(false)
                    // once the network request has completed successfully.
                    refreshListView();
                }
            });
            // Configure the refreshing colors
            mViewHolder.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
            mViewHolder.swipeContainer.setSoundEffectsEnabled(true);

            mViewHolder.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity) getActivity()).moveToScreen(CreateRideFragment.class, null,
                        true, false);
                }
            });

            mViewHolder.fab.attachToListView(mViewHolder.listView);
        }
    }

    class ViewHolder {
        View root;
        SwipeRefreshLayout swipeContainer;
        ListView listView;
        FloatingActionButton fab;
    }
}
