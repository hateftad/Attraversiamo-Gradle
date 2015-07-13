package com.me.event;

import com.me.level.tasks.BodyInfo;

/**
 * Created by hateftadayon on 7/13/15.
 */
public abstract class MultiStateEvent extends GameEvent {

    protected int m_eventId;

    public int getEventId() {
        return m_eventId;
    }

    public MultiStateEvent(BodyInfo bodyInfo) {
        super(bodyInfo.getEventType());
        m_eventId = bodyInfo.getEventId();
    }

}
