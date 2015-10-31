package com.me.events;

import com.artemis.Entity;
import com.me.level.tasks.BodyInfo;
import com.me.systems.GameEntityProcessingSystem;

/**
 * Created by hateftadayon on 7/12/15.
 */
public class TaskEvent extends GameEvent {

    private int m_eventId;

    public int getEventId() {
        return m_eventId;
    }

    public TaskEvent(BodyInfo bodyInfo){
        super(bodyInfo.getEventType());
        m_eventId = bodyInfo.getEventId();
    }

    public TaskEvent(GameEventType type){
        super(type);
    }

    public void notify(Entity e, GameEntityProcessingSystem entityProcessingSystem){
        entityProcessingSystem.notifyObservers(e, this);
    }
}
