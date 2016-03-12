package com.spontaneous.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This is an adapter for a list view of items in the event card.
 */
public class ItemsListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<Item> mItems;

    public ItemsListAdapter(Context mContext) {
        this.mContext = mContext;
        this.mItems = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Item getItem(int position) {
        return mItems.get(position);
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
        NetworkImageView bringerPhoto = (NetworkImageView) convertView.findViewById(R.id.items_list_bringer_photo);

        Item item = mItems.get(position);

        title.setText(item.getTitle());
        isBringing.setImageDrawable(item.isBringing()
                ? mContext.getDrawable(R.drawable.ic_done_black)
                : mContext.getDrawable(R.drawable.ic_close_black));

        bringerPhoto.setImageUrl(item.getBringer()
                .getUserProfile()
                .getProfilePicture(), SpontaneousApplication.getInstance().getImageLoader());

        return convertView;
    }

    /**
     * Add a collection of items to the adapter.
     *
     * @param items Items to add.
     */
    public void addAll(Collection<Item> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * Add a given item to the adapter.
     *
     * @param item Given item to add.
     */
    public void addItem(Item item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    /**
     * Remove an item from the adapter given its position.
     *
     * @param position Index of the item.
     */
    public void removeItem(int position) {
        mItems.remove(position);
        notifyDataSetChanged();
    }
}
