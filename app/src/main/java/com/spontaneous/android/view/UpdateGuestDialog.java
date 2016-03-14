package com.spontaneous.android.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.activity.BaseActivity;
import com.spontaneous.android.http.request.model.UpdateGuestRequest;
import com.spontaneous.android.http.request.service.EventService;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Guest;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This dialog contains an EditText representing the user's response to the event and two buttons,
 * representing whether the user is arriving.
 */
public class UpdateGuestDialog extends AlertDialog {

    private final Guest mGuest;
    private final Context mContext;

    protected UpdateGuestDialog(Context context, Guest guest) {
        super(context);

        this.mGuest = guest;
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_update_guest);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        ImageButton positive = (ImageButton) findViewById(R.id.event_confirmation);
        ImageButton negative = (ImageButton) findViewById(R.id.event_decline);

        positive.setOnClickListener(getOnClickListener());
        negative.setOnClickListener(getOnClickListener());
    }

    /**
     * @return Get the dialog input.
     */
    public String getInput() {
        return ((EditText) findViewById(R.id.dialog_status)).getText().toString();
    }

    /**
     * @return The mGuest being edited.
     */
    public Guest getGuest() {
        return this.mGuest;
    }

    /**
     * @return On click listener for buttons.
     */
    public View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Show the wait dialog
                ((BaseActivity) mContext).showWaitDialog();

                String status = getInput();
                boolean isAttending = v.getId() == R.id.event_confirmation;

                UpdateGuestRequest updateRequest = new UpdateGuestRequest(status, isAttending);
                mGuest.update(updateRequest);

                dismiss();

                SpontaneousApplication.getInstance()
                        .getService(EventService.class)
                        .updateGuest(mGuest.getId(), updateRequest, new Callback<BaseResponse>() {
                            @Override
                            public void success(BaseResponse baseResponse, Response response) {

                                //Dismiss the wait dialog and show a toast.
                                ((BaseActivity) mContext).dismissDialog();

                                Toast.makeText(getContext(), "Status updated", Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void failure(RetrofitError error) {

                                //Dismiss the wait dialog and show a toast.
                                ((BaseActivity) mContext).dismissDialog();

                                Toast.makeText(getContext(), "Couldn't update status.", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
            }
        };
    }
}
