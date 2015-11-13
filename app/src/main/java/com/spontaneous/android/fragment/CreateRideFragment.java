package com.spontaneous.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.spontaneous.android.R;
import com.spontaneous.android.activity.MainActivity;
import com.spontaneous.android.http.ApiRestClient;
import com.spontaneous.android.http.request.CreateRideRequest;
import com.spontaneous.android.http.request.CreateRideRequestModel;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Ride;
import com.spontaneous.android.sharedprefs.LoginData;
import com.spontaneous.android.util.Logger;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by eidan on 5/24/15.
 */
public class CreateRideFragment extends Fragment {

    private ViewHolder mViewHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initViewHolder(inflater, container);
        return mViewHolder.root;
    }

    private void initViewHolder(LayoutInflater inflater, ViewGroup container) {
        mViewHolder = new ViewHolder();
        mViewHolder.root = inflater.inflate(R.layout.fragment_create_ride, container, false);
        mViewHolder.origin = (EditText) mViewHolder.root.findViewById(R.id.create_ride_origin);
        mViewHolder.destination = (EditText) mViewHolder.root.findViewById(R.id.create_ride_destination);
        mViewHolder.date = (EditText) mViewHolder.root.findViewById(R.id.create_ride_date);
        mViewHolder.price = (EditText) mViewHolder.root.findViewById(R.id.create_ride_price);
        mViewHolder.passengers = (EditText) mViewHolder.root.findViewById(R.id.create_ride_passengers);
        mViewHolder.description = (EditText) mViewHolder.root.findViewById(R.id.create_ride_description);
        mViewHolder.done = (Button) mViewHolder.root.findViewById(R.id.create_ride_done);

        mViewHolder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRideRequestModel ride = new CreateRideRequestModel();
                ride.setOrigin(mViewHolder.origin.getText().toString());
                ride.setDestination(mViewHolder.destination.getText().toString());

                String date = mViewHolder.date.getText().toString();
                ride.setStartTime(date);

                ride.setCost(Double.parseDouble(mViewHolder.price.getText().toString()));
                ride.setPassengers(Integer.parseInt(mViewHolder.passengers.getText().toString()));
                ride.setDescription(mViewHolder.description.getText().toString());
                ride.setOwnerId(LoginData.getUserId(getActivity()));

                CreateRideRequest createRideRequest = ApiRestClient.getRequest(getActivity(),
                        CreateRideRequest.class);

                createRideRequest.createRide(ride, new Callback<BaseResponse<Ride>>() {
                    @Override
                    public void success(BaseResponse<Ride> rideBaseResponse, Response response) {
                        if(getActivity() != null) {
                            ((MainActivity) getActivity()).moveToScreen(HomeFragment.class,
                                null, false, false);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Logger.e("CreateRideRequest: Failure");
                    }
                });
            }
        });
    }

    class ViewHolder {
        View root;
        EditText origin;
        EditText destination;
        EditText date;
        EditText price;
        EditText passengers;
        EditText description;
        Button done;
    }
}
