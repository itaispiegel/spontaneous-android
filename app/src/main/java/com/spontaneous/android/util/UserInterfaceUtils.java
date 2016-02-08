package com.spontaneous.android.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * This class contains an assortment of user interface util methods.
 */
public class UserInterfaceUtils {

    /**
     * Sets a ListView's height based on its number of children.
     *
     * @param listView to apply the height
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
            listItem.measure(0, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        //Set the new calculated required height
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
