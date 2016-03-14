package com.spontaneous.android.model;

import com.spontaneous.android.util.GsonFactory;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class is a superclass entity which defines the template for all entities.
 */
public abstract class BaseEntity implements Serializable {

    private long id;
    private final DateTime creationTime;

    BaseEntity() {
        this.creationTime = new DateTime();
    }

    public long getId() {
        return id;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        if(null == o) return false;
        if(this == o) return true;

        BaseEntity that = (BaseEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    public String toJson() {
        return GsonFactory.getGson().toJson(this);
    }
}
