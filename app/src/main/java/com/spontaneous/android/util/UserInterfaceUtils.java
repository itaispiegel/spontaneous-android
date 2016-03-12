package com.spontaneous.android.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.spontaneous.android.activity.ActivityMain;

/**
 * This class contains an assortment of user interface util methods.
 */
public class UserInterfaceUtils {

    /**
     * Sets a ListView's height based on its number of children.
     *
     * @param listView The listview to apply the height to.
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        //Get adapter and exit method if it is null
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        //Add current item height to the total height
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        //Set the new calculated required height
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * A scroll listener for list views colliding with Pull To Refresh.
     * Set refresh enabled if listview is at top.
     *
     * @param listView The listview to integrate with Pull To Refresh.
     * @return An OnScrollListener for the given listview.
     */
    public static AbsListView.OnScrollListener listViewScrollListener(final ListView listView) {
        return new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                ActivityMain activityMain = (ActivityMain) listView.getContext();
                activityMain.setRefreshEnabled(isListViewAtTop(listView));
            }
        };
    }

    /**
     * @return Returns whether the listview is at it's top.
     */
    public static boolean isListViewAtTop(ListView listView) {

        //Return true if the listview is null, or if it does not have children.
        if (listView == null || listView.getChildCount() == 0) {
            return true;
        }

        boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
        boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;

        return firstItemVisible && topOfFirstItemVisible;
    }

    /**
     * This method creates and shows an alert dialog.
     *
     * @param context            The context of the activity.
     * @param dialogTitle        The requested title for the dialog.
     * @param positiveButtonText The text showing on the positive button.
     * @param negativeButtonText The text showing on the negative button.
     * @param onPositiveClick    The action to perform when clicking the positive button.
     * @param userInput          EditText for user input.
     */
    public static void showAlertDialog(Context context, String dialogTitle, String positiveButtonText, String negativeButtonText,
                                       DialogInterface.OnClickListener onPositiveClick, EditText userInput) {
        new AlertDialog.Builder(context)
                .setTitle(dialogTitle)
                .setView(userInput)
                .setPositiveButton(positiveButtonText, onPositiveClick)
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //On cancel don't do anything
                    }
                }).show();
    }
}
