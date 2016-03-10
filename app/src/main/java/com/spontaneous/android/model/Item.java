package com.spontaneous.android.model;

/**
 * This class represents a required item for an event.
 */
public class Item extends BaseEntity {

    private String title;
    private boolean isBringing;

    private Guest bringer;

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

    public Guest getBringer() {
        return bringer;
    }

    public void setBringer(Guest bringer) {
        this.bringer = bringer;
    }
}
