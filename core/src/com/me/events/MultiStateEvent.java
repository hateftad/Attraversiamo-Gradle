package com.me.events;

import com.me.level.tasks.BodyInfo;

/**
 * Created by hateftadayon on 7/13/15.
 */
public abstract class MultiStateEvent extends GameEvent {

    protected int eventId;

    public int getEventId() {
        return eventId;
    }

    public MultiStateEvent(BodyInfo bodyInfo) {
        super(bodyInfo.getEventType());
        eventId = bodyInfo.getEventId();
    }

}
