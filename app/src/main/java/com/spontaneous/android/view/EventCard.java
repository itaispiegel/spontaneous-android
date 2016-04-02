package com.spontaneous.android.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.activity.BaseActivity;
import com.spontaneous.android.adapter.GuestsListAdapter;
import com.spontaneous.android.adapter.ItemsListAdapter;
import com.spontaneous.android.http.request.service.EventService;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.model.Guest;
import com.spontaneous.android.model.Item;
import com.spontaneous.android.model.User;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.DateTimeFormatter;
import com.spontaneous.android.util.Logger;
import com.spontaneous.android.util.UserInterfaceUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This is a representational view for an event.
 */
public class EventCard extends FrameLayout {

    private Context mContext;
    private Event mEvent;

    //Google maps API constants.
    @SuppressWarnings("FieldCanBeLocal")
    private final String GOOGLE_MAPS_STATIC_MAPS_API_URL = "https://maps.googleapis.com/maps/api/staticmap?";
    private final String GOOGLE_MAPS_NAVIGATION_API_URL = "http://maps.google.com/maps?q=";

    // Views
    private TextView mEventTitleTextView;
    private TextView mEventDescriptionTextView;
    private TextView mEventDateTextView;
    private TextView mEventLocationTextView;
    private NetworkImageView mEventMapImage;

    //Guests list
    private GuestsListAdapter mGuestsListAdapter;
    private ListView mGuestsListView;

    //Items list
    private ItemsListAdapter mItemsListAdapter;
    private ListView mItemsListView;
    private TextView mItemsTextView;

    public EventCard(Context context) {
        super(context);
        this.mContext = context;

        init();
    }

    public EventCard(Context context, Event event) {
        this(context);

        this.mContext = context;
        setEvent(event);
    }

    /**
     * Initialize the views in the EventCard.
     */
    private void init() {

        //Inflate the card.
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.view_event_card, this);

        //Initialize views in card.
        mEventTitleTextView = (TextView) layout.findViewById(R.id.event_card_title);
        mEventDescriptionTextView = (TextView) layout.findViewById(R.id.event_card_description);
        mEventDateTextView = (TextView) layout.findViewById(R.id.event_card_date);
        mEventLocationTextView = (TextView) layout.findViewById(R.id.event_card_location);

        mEventMapImage = (NetworkImageView) layout.findViewById(R.id.event_card_map);

        //Initialize guests list view.
        mGuestsListAdapter = new GuestsListAdapter(mContext);

        mGuestsListView = (ListView) layout.findViewById(R.id.event_card_guests_list);
        mGuestsListView.setAdapter(mGuestsListAdapter);
        mGuestsListView.setOnItemClickListener(guestItemClick());

        mItemsListAdapter = new ItemsListAdapter(mContext);

        mItemsListView = (ListView) layout.findViewById(R.id.event_card_items_list);
        mItemsListView.setAdapter(mItemsListAdapter);
        mItemsListView.setOnItemLongClickListener(itemsLongClick());

        mItemsTextView = (TextView) layout.findViewById(R.id.event_card_items_text);
    }

    /**
     * Set the view's content according to the given event.
     *
     * @param event Event to display on card.
     */
    private void setEvent(final Event event) {

        //Set the event field.
        this.mEvent = event;

        //Set views text
        mEventTitleTextView.setText(event.getTitle());
        mEventDescriptionTextView.setText(event.getDescription());
        mEventLocationTextView.setText(
                mContext.getString(R.string.event_page_location, event.getLocation())
        );

        //Set map image url and click listener.
        //On click open a navigation app.
        mEventMapImage.setImageUrl(getMapUrl(event), SpontaneousApplication.getInstance().getImageLoader());
        mEventMapImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(GOOGLE_MAPS_NAVIGATION_API_URL + event.getLocation())
                );

                getContext().startActivity(intent);
            }
        });

        //Populate the guests list view.
        mGuestsListAdapter.addAll(event.getGuests());
        UserInterfaceUtils.setListViewHeightBasedOnChildren(mGuestsListView);

        //Populate the items listview.
        mItemsListAdapter.addAll(event.getItems());
        UserInterfaceUtils.setListViewHeightBasedOnChildren(mItemsListView);

        //If items list view is not empty, show the text view.
        mItemsTextView.setVisibility(mItemsListAdapter.isEmpty() ? GONE : VISIBLE);

        //Show the date of the event in format.
        mEventDateTextView.setText(mContext.getString(
                R.string.event_page_date, DateTimeFormatter.format(event.getDate())
        ));
    }

    /**
     * Get URL of of map image.
     *
     * @param event Event to get map to.
     * @return The URL of the map.
     */
    private String getMapUrl(final Event event) {
        final String defaultEncoding = "UTF-8";

        //Encode the address so the API will retrieve a valid image
        String addressEncoded;
        try {
            addressEncoded = URLEncoder.encode(event.getLocation(), defaultEncoding);
        } catch (UnsupportedEncodingException e) {
            addressEncoded = event.getLocation();
        }

        return GOOGLE_MAPS_STATIC_MAPS_API_URL +
                "center=" + addressEncoded +
                "&size=800x500&zoom=17&" +
                "markers=" + addressEncoded + "&" +
                "key=" + mContext.getString(R.string.google_api_key);
    }

    /**
     * @return OnItemClickListener for guests list - Show options menu: assign item, change status, set reminder.
     */
    private AdapterView.OnItemClickListener guestItemClick() {
        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {

                Logger.info("Guest was clicked: " + mGuestsListAdapter.getItem(position));

                //Show the dialog if the user clicked himself.
                final Guest guest = mGuestsListAdapter.getItem(position);
                final User authenticatedUser = AccountUtils.getAuthenticatedUser();

                //If the user clicked on a different user.
                if (!guest.getUserProfile().equals(authenticatedUser)) {

                    //If the user is the host, assign an item to the clicked guest.
                    if (mEvent.getHost().equals(authenticatedUser)) {
                        assignItem(guest);
                    }

                    //Return void in any case that the authenticated user is not the host.
                    return;
                }

                //The method will reach this point if the user clicked on himself.
                final String[] options = {"Update status", "Assign item", "Set reminder"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            //If user clicked on the first option, update his status.
                            case 0:
                                final UpdateGuestDialog updateGuestDialog = new UpdateGuestDialog(getContext(), guest);
                                updateGuestDialog.show();

                                //Update the listview.
                                updateGuestDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        Guest updated = ((UpdateGuestDialog) dialog).getGuest();
                                        mGuestsListAdapter.updateGuest(position, updated);
                                    }
                                });

                                break;

                            case 1:
                                //If the user clicked on the second option, assign an item.
                                assignItem(guest);
                                break;

                            case 2:
                                Toast.makeText(getContext(), "Setting reminder..", Toast.LENGTH_SHORT)
                                        .show();

                                break;
                        }
                    }
                });
                builder.show();
            }
        };
    }

    /**
     * @return On long click - delete the item if is your's or if you are the event host.
     */
    private AdapterView.OnItemLongClickListener itemsLongClick() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView parent, final View view, final int position, final long id) {

                //Log a message and show the wait dialog.
                Logger.info("Item was clicked: " + mItemsListAdapter.getItem(position));
                ((BaseActivity) mContext).showWaitDialog();

                final Item item = mItemsListAdapter.getItem(position);
                SpontaneousApplication.getInstance()
                        .getService(EventService.class)
                        .deleteItem(item.getId(), new Callback<BaseResponse>() {
                            @Override
                            public void success(BaseResponse baseResponse, Response response) {
                                //Dismiss the dialog, show a toast and update the card.
                                ((BaseActivity) mContext).dismissDialog();

                                Logger.info("Item deleted successfully.");
                                Toast.makeText(getContext(), "Item deleted successfully", Toast.LENGTH_SHORT)
                                        .show();

                                mItemsListAdapter.removeItem(position);

                                //If listview is empty, make the title textview gone.
                                if (mItemsListAdapter.isEmpty()) {
                                    mItemsTextView.setVisibility(GONE);
                                }

                                //Set listview height.
                                UserInterfaceUtils.setListViewHeightBasedOnChildren(mItemsListView);
                            }

                            @Override
                            public void failure(RetrofitError error) {

                                //Dismiss the dialog and show a toast.
                                ((BaseActivity) mContext).dismissDialog();

                                Logger.error("Item could not be deleted.");
                                Toast.makeText(getContext(), "Item could not be deleted", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });

                return false;
            }
        };
    }

    /**
     * Create a dialog for assigning an item to a given guest.
     *
     * @param guest Given guest to assign the item to.
     */
    private void assignItem(final Guest guest) {
        //Show the item assignment dialog.
        final EditText userInput = new EditText(mContext);
        userInput.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        Dialog.OnClickListener onPositiveClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Show the wait dialog.
                ((BaseActivity) mContext).showWaitDialog();

                SpontaneousApplication.getInstance()
                        .getService(EventService.class)
                        .assignItem(guest.getId(), userInput.getText().toString(), new Callback<BaseResponse>() {
                            @Override
                            public void success(BaseResponse baseResponse, Response response) {

                                //Dismiss the dialog, show a toast and update the card.
                                ((BaseActivity) mContext).dismissDialog();

                                Toast.makeText(getContext(), "Item assigned successfully", Toast.LENGTH_SHORT)
                                        .show();

                                //Add the created item to the adapter.
                                Item item = new Item(userInput.getText().toString(), guest, false);
                                mItemsListAdapter.addItem(item);

                                //Make the listview title visible.
                                mItemsTextView.setVisibility(VISIBLE);

                                //Set listview height.
                                UserInterfaceUtils.setListViewHeightBasedOnChildren(mItemsListView);
                            }

                            @Override
                            public void failure(RetrofitError error) {

                                //Dismiss the dialog and show a toast.
                                ((BaseActivity) mContext).dismissDialog();

                                Toast.makeText(getContext(), "Item was not assigned successfully", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
            }
        };

        UserInterfaceUtils.showAlertDialog(getContext(), "Assign an item", "OK", "Cancel", userInput, onPositiveClick);
    }

    /**
     * @return The guest list adapter of the event card.
     */
    public GuestsListAdapter getGuestsListAdapter() {
        return mGuestsListAdapter;
    }
}
