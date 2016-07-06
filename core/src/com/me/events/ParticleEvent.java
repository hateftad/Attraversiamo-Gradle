package com.me.events;

import com.me.level.tasks.BodyInfo;
import com.me.systems.GameEntityWorld;

/**
 * Created by hateftadayon on 1/31/16.
 */
public class ParticleEvent extends GameEvent {

    private int eventId;
    private boolean started;

    public ParticleEvent(BodyInfo bodyInfo) {
        super(bodyInfo.getEventType());
        eventId = bodyInfo.getEventId();
    }

    public ParticleEvent(GameEventType type, int eventId){
        super(type);
        this.eventId = eventId;
    }

    @Override
    public void notify(GameEntityWorld entityProcessingSystem) {
        entityProcessingSystem.onNotify(this);
    }

    public int getEventId() {
        return eventId;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
