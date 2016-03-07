package com.spontaneous.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spontaneous.android.R;
import com.spontaneous.android.model.Guest;
import com.spontaneous.android.model.Item;

/**
 * This is an adapter for a list view of items in the event card.
 */
public class ItemsListAdapter extends BaseAdapter {

    private Guest mGuest;

    private final Context mContext;
    private LayoutInflater mInflater;

    public ItemsListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setGuest(Guest guest) {
        this.mGuest = guest;
    }

    @Override
    public int getCount() {
        return mGuest.getItems().size();
    }

    @Override
    public Item getItem(int position) {
        return mGuest.getItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (mInflater == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_view_item, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.item_list_title);
        ImageView isBringing = (ImageView) convertView.findViewById(R.id.item_list_is_bringing);

        Item item = mGuest.getItems().get(position);

        title.setText(item.getTitle());
        isBringing.setImageDrawable(item.isBringing()
                ? mContext.getDrawable(R.drawable.ic_done_black)
                : mContext.getDrawable(R.drawable.ic_close_black));

        return null;
    }
}
