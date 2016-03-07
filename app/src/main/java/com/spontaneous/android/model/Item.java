package com.spontaneous.android.model;

/**
 * Created by Itai on 06-Mar-16.
 */
public class Item extends BaseEntity {

    private String title;
    private boolean isBringing;

    public Item(String title, boolean isBringing) {
        this.title = title;
        this.isBringing = isBringing;
    }

    public String getTitle() {
        return title;
    }

    public boolean isBringing() {
        return isBringing;
    }
}
