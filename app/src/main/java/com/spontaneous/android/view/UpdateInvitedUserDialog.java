package com.spontaneous.android.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.spontaneous.android.R;

/**
 * This dialog contains an EditText representing the user's response to the event and two buttons,
 * representing whether the user is arriving.
 */
public class UpdateInvitedUserDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update_invited_user, null);

        dialog.setView(view);
        return dialog.create();
    }
}
