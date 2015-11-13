package com.spontaneous.android.model;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by Eidan on 12/27/2014.
 */

public abstract class BaseEntity implements Serializable {

    protected long id;
    protected DateTime creationTime;

    protected BaseEntity() {
        this.creationTime = new DateTime();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        if(null == o) return false;
        if(this == o) return true;

        BaseEntity that = (BaseEntity) o;

        return getId() == that.getId();
    }
}
