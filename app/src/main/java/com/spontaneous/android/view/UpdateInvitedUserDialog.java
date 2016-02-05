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
import com.spontaneous.android.http.request.model.UpdateInvitedUserRequest;
import com.spontaneous.android.http.request.service.EventService;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.InvitedUser;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This dialog contains an EditText representing the user's response to the event and two buttons,
 * representing whether the user is arriving.
 */
public class UpdateInvitedUserDialog extends AlertDialog {

    private final InvitedUser invitedUser;

    protected UpdateInvitedUserDialog(Context context, InvitedUser invitedUser) {
        super(context);
        this.invitedUser = invitedUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_update_invited_user);

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
     * @return The invited user being edited.
     */
    public InvitedUser getInvitedUser() {
        return this.invitedUser;
    }

    /**
     * @return On click listener for buttons.
     */
    public View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = getInput();
                boolean isAttending = v.getId() == R.id.event_confirmation;

                UpdateInvitedUserRequest updateRequest = new UpdateInvitedUserRequest(status, isAttending);
                invitedUser.update(updateRequest);

                SpontaneousApplication.getInstance()
                        .getService(EventService.class)
                        .updateInvitedUser(invitedUser.getId(), updateRequest, new Callback<BaseResponse>() {
                            @Override
                            public void success(BaseResponse baseResponse, Response response) {
                                Toast.makeText(getContext(), "Status updated", Toast.LENGTH_SHORT)
                                        .show();
                                dismiss();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getContext(), "Couldn't update status.", Toast.LENGTH_SHORT)
                                        .show();
                                dismiss();
                            }
                        });
            }
        };
    }
}
