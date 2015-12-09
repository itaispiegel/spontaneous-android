package com.spontaneous.android.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.spontaneous.android.R;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Base activity template.
 * Contains custom action bar integration.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        if (showToolbar()) {
            setCustomActionBar();
        }
    }

    private void setCustomActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        toolbar.setClickable(true);

        this.mToolbar = toolbar;

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    protected abstract int getLayoutResourceId();

    protected abstract boolean showToolbar();

    void setToolbarMessage(String message) {
        TextView toolbarMessage = (TextView) findViewById(R.id.toolbar_message);
        toolbarMessage.setText(message);
    }

    /**
     * Sets a ListView's height based on its number of children.
     *
     * @param listView to apply the height
     */
    void setListviewHeightBasedOnChildren(ListView listView) {
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
     *
     * @return the progress dialog
     */
    ProgressDialog showWaitDialog() {
        final String message = getString(R.string.message_loading);

        ProgressDialog waitDialog = new ProgressDialog(this);
        waitDialog.setMessage(message);
        waitDialog.setCancelable(true);
        waitDialog.setIndeterminate(true);
        waitDialog.show();

        return waitDialog;
    }


    /**
     * Setup email auto complete to an AutoCompleteTextView.
     * Uses email from device data.
     *
     * @param view    to set the AutoComplete to.
     * @param context of the activity.
     */
    void setupEmailAutoComplete(AutoCompleteTextView view, Context context) {
        final Uri emailContentUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        ContentResolver cr = context.getContentResolver();

        Cursor emailCursor = cr.query(emailContentUri, null, null, null, null);

        //Exit method if email cursor is null
        if (emailCursor == null) {
            return;
        }

        //HashSet because we want the emails to be unique.
        HashSet<String> emailsCollection = new HashSet<>(emailCursor.getCount());

        while (emailCursor.moveToNext()) {
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
    void hideKeyboard() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Start a flow by the specified class and context
     *
     * @param activityClass of the requested activity
     */
    public void startActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        if (Build.VERSION.SDK_INT >= 11) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        startActivity(intent);
    }
}
