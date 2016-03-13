package com.me.events;

import com.me.level.tasks.BodyInfo;
import com.me.systems.GameEntityWorld;

/**
 * Created by hateftadayon on 1/31/16.
 */
public class ParticleEvent extends GameEvent {

    private int m_eventId;
    private boolean m_started;

    public ParticleEvent(BodyInfo bodyInfo) {
        super(bodyInfo.getEventType());
        m_eventId = bodyInfo.getEventId();
    }

    public ParticleEvent(GameEventType type, int eventId){
        super(type);
        m_eventId = eventId;
    }

    @Override
    public void notify(GameEntityWorld entityProcessingSystem) {
        entityProcessingSystem.onNotify(this);
    }

    public int getEventId() {
        return m_eventId;
    }

    public boolean isStarted() {
        return m_started;
    }

    public void setStarted(boolean m_started) {
        this.m_started = m_started;
    }
}
