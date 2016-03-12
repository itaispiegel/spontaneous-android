package com.spontaneous.android.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.http.request.model.UpdateGuestRequest;
import com.spontaneous.android.http.request.service.EventService;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.model.Guest;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.Logger;
import com.spontaneous.android.util.UserInterfaceUtils;
import com.spontaneous.android.view.EventCard;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This activity represents an invitation to an event.
 * The activity launches when the user clicks the invitation notification,
 * and contains the event card, and two buttons indicating whether the user confirmed/declined his arrival.
 */
public class ActivityEventInvitation extends BaseActivity {

    private EventCard mEventCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the details of the event displaying.
        Event event = (Event) getIntent().getExtras()
                .getSerializable(getString(R.string.event_invitation));

        //Show back button on toolbar
        showBackButtonOnToolbar();

        ScrollView mScroll = (ScrollView) findViewById(R.id.scroll);

        FrameLayout mCardContainer = (FrameLayout) findViewById(R.id.card);
        mEventCard = new EventCard(this, event);

        mCardContainer.addView(mEventCard);
        mScroll.smoothScrollTo(0, 0); //For some reason the card starts on the bottom of the activity

        final ImageButton mDeclineEventButton = (ImageButton) findViewById(R.id.event_decline);
        final ImageButton mConfirmEventButton = (ImageButton) findViewById(R.id.event_confirmation);

        //Handle click on the footer button for confirmation or declination for the event.
        mDeclineEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFooterButtonClick(mDeclineEventButton.getId());
            }
        });

        mConfirmEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFooterButtonClick(mConfirmEventButton.getId());
            }
        });
    }

    @Override
    int getLayoutResourceId() {
        return R.layout.activity_event_invitation;
    }

    @Override
    boolean showToolbar() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Handle toolbar button clicks
        switch (item.getItemId()) {

            //Go back to main activity.
            case android.R.id.home:
                Logger.info("Navigating back to main activity");
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Update arrival status to the event.
     * Send the update to the server, and show a swipe animation at the end.
     */
    private void onFooterButtonClick(int viewId) {

        final boolean userConfirmedArrival = viewId == R.id.event_confirmation;

        //Set the swipe animation according to the clicked button.
        final Animation swipeAnimation = userConfirmedArrival
                ? AnimationUtils.loadAnimation(this, R.anim.animate_exit_right)
                : AnimationUtils.loadAnimation(this, R.anim.animate_exit_left);

        swipeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


        //Initialize the status edit text and make it capitalize sentences.
        final EditText statusEditText = new EditText(this);

        DialogInterface.OnClickListener onPositiveClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Get the status text.
                String status = statusEditText.getText().toString();

                //Get the instance of the authenticated guest user.
                Guest currUser = mEventCard.getGuestsListAdapter()
                        .getGuestByEmail(AccountUtils.getAuthenticatedUser().getEmail());

                //Create a new update request.
                UpdateGuestRequest updateRequest = new UpdateGuestRequest(status, userConfirmedArrival);

                //Update the guest in the database.
                SpontaneousApplication.getInstance()
                        .getService(EventService.class)
                        .updateGuest(currUser.getId(), updateRequest, new Callback<BaseResponse>() {
                            @Override
                            public void success(BaseResponse baseResponse, Response response) {
                                //Check whether the update succeeded.
                                if (baseResponse.getStatusCode() == BaseResponse.SUCCESS) {

                                    //Time until the swipe animations starts.
                                    final long INTERVAL = 300;

                                    //If succeeded, sleep for 1 seconds and then do the swipe animation.
                                    SystemClock.sleep(INTERVAL);
                                    mEventCard.startAnimation(swipeAnimation);

                                } else {

                                    //If there was an error, then show a toast message.
                                    Toast.makeText(getApplicationContext(), "Could not update guest.", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getApplicationContext(), "Could not update guest.", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
            }
        };

        UserInterfaceUtils.showAlertDialog(this, "Write a status", "OK", "Cancel", onPositiveClick, statusEditText);
    }
}