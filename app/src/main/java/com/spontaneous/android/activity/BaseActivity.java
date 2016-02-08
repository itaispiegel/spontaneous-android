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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.spontaneous.android.R;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Base activity template.
 * Contains custom action bar integration.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * The application toolbar.
     * The toolbar has a light blue color and a textview with the text: "SPONTANEOUS".
     */
    private Toolbar mToolbar;

    /**
     * A dialog with the text "Loading".
     */
    protected ProgressDialog mWaitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        if (showToolbar()) {
            setCustomToolbar();
        }
    }

    /**
     * Initialize the toolbar.
     */
    private void setCustomToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        mToolbar.setClickable(true);

        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    /**
     * @return The application toolbar.
     */
    final Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * @return Layout resource id of the activity.
     */
    abstract int getLayoutResourceId();

    /**
     * @return A boolean deciding whether or not to show the toolbar in the sub activity.
     */
    abstract boolean showToolbar();

    /**
     * Show a back button on the action bar.
     * The button will show up only if the {@link ActionBar} is displayed, in the current activity, and only if it isn't null.
     */
    void showBackButtonOnToolbar() {
        ActionBar actionBar = getSupportActionBar();

        if (showToolbar() && actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Show a given message in the toolbar.
     *
     * @param message The message to display.
     */
    final void setToolbarMessage(String message) {
        TextView toolbarMessage = (TextView) mToolbar.findViewById(R.id.toolbar_message);
        toolbarMessage.setText(message);
    }

    /**
     * Show a progress dialog with the text "Loading" in the given activity.
     */
    final public void showWaitDialog() {

        //Set the message in the dialog.
        final String message = getString(R.string.message_loading);

        //Initialize the dialog itself.
        mWaitDialog = new ProgressDialog(this);
        mWaitDialog.setMessage(message);
        mWaitDialog.setCancelable(true);
        mWaitDialog.setIndeterminate(true);
        mWaitDialog.show();
    }

    /**
     * Dismiss the progress dialog if it is showing.
     */
    final public void dismissDialog() {

        //Dismiss the wait dialog.
        if (mWaitDialog != null && mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }
    }

    /**
     * Setup email auto complete to an AutoCompleteTextView.
     * Uses email from device data.
     *
     * @param view    to set the AutoComplete to.
     * @param context of the activity.
     */
    final void setupEmailAutoComplete(AutoCompleteTextView view, Context context) {
        final Uri emailContentUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        ContentResolver cr = context.getContentResolver();

        Cursor emailCursor = cr.query(emailContentUri, null, null, null, null);

        //Exit method if email cursor is null
        if (emailCursor == null) {
            return;
        }

        //In this case we are using a HashSet since we want the emails to be unique.
        HashSet<String> emailsCollection = new HashSet<>(emailCursor.getCount());

        //Iterate over the emails and add them to the collection.
        while (emailCursor.moveToNext()) {
            int columnIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            String email = emailCursor.getString(columnIndex);

            emailsCollection.add(email);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line,
                new ArrayList<>(emailsCollection));
        view.setAdapter(adapter);

        //Close the email cursor
        emailCursor.close();
    }

    /**
     * Get finish animation for the loading image.
     *
     * @param loadingImage       The loading spinner image.
     * @param viewsToMakeVisible Views to show after the animation is done.
     * @return The finish animation.
     */
    final Animation getFinishAnimation(final ImageView loadingImage, final View... viewsToMakeVisible) {

        final Animation finishAnimation = AnimationUtils.loadAnimation(this, R.anim.animate_exit_down);
        finishAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loadingImage.setVisibility(View.GONE);

                for (View view : viewsToMakeVisible) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        });

        return finishAnimation;
    }

    /**
     * Hide the keyboard on the give activity.
     */
    public void hideKeyboard() {
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
    public final void startActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        if (Build.VERSION.SDK_INT >= 11) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        startActivity(intent);
    }
}
