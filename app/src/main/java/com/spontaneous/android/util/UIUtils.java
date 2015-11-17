package com.spontaneous.android.util;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.spontaneous.android.R;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * An assortment of UI Utils.
 */
public class UIUtils {

    /**
     * Sets a ListView's height based on its number of children.
     * @param listView to apply the height
     */
    public static void setListviewHeightBasedOnChildren(ListView listView) {
        //Get adapter and exit method if it is null
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        //Add current item height to the total height
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        //Set the new calculated required height
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * Show a progress dialog with the text "Loading" in the given activity.
     * @return the progress dialog
     */
    public static ProgressDialog showWaitDialog(Activity activity) {
        final String message = activity.getString(R.string.message_loading);

        ProgressDialog waitDialog = new ProgressDialog(activity);
        waitDialog.setMessage(message);
        waitDialog.setCancelable(true);
        waitDialog.setIndeterminate(true);
        waitDialog.show();

        return waitDialog;
    }

    /**
     * Sets the application's custom ActionBar to the given context.
     */
    public static void setCustomActionBar(Context ctx) {
        ActionBar actionBar = ((Activity) ctx).getActionBar();

        LayoutInflater inflater = LayoutInflater.from(ctx);

        View mCustomActionBar = inflater.inflate(R.layout.action_bar, null);

        //No reason that action bar should be null. But just in case.
        if (actionBar != null) {
            actionBar.setCustomView(mCustomActionBar);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    /**
     * Setup email auto complete to an AutoCompleteTextView.
     * Uses email from device data.
     * @param view to set the AutoComplete to.
     * @param context of the activity.
     */
    public static void setupEmailAutoComplete(AutoCompleteTextView view, Context context) {
        final Uri emailContentUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        ContentResolver cr = context.getContentResolver();

        Cursor emailCursor = cr.query(emailContentUri, null, null, null, null);

        //Exit method if email cursor is null
        if(emailCursor == null) {
            return;
        }

        //HashSet because we want the emails to be unique.
        HashSet<String> emailsCollection = new HashSet<>(emailCursor.getCount());

        while(emailCursor.moveToNext()) {
            int columnIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            String email = emailCursor.getString(columnIndex);

            emailsCollection.add(email);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, new ArrayList<>(emailsCollection));
        view.setAdapter(adapter);

        //Close the email cursor
        emailCursor.close();
    }

    /**
     * Hide the keyboard on the give activity.
     */
    public static void hideKeyboard(Activity activity) {
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
