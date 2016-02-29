package com.spontaneous.android.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
                ((ActivityMain) listView.getContext()).setRefreshEnabled(UserInterfaceUtils.isListViewAtTop(listView));
            }
        };
    }

    /**
     * @return Returns whether the listview is at it's top.
     */
    public static boolean isListViewAtTop(ListView listView) {

        //Return false if the listview is null, or if it does not have children.
        if (listView == null || listView.getChildCount() <= 0) {
            return false;
        }

        boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
        boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;

        return firstItemVisible && topOfFirstItemVisible;
    }
}
