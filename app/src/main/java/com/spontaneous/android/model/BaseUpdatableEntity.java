package com.spontaneous.android.model;

/**
 * Created by Eidan on 12/27/2014.
 */

import org.joda.time.DateTime;

public abstract class BaseUpdatableEntity extends BaseEntity {

    private DateTime lastUpdateTime;

    public DateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(DateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
