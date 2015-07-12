package com.me.interfaces;

import com.artemis.Entity;
import com.me.systems.GameEntityProcessingSystem;

/**
 * Created by hateftadayon on 7/12/15.
 */
public class GameEvent {

    protected GameEventType m_eventType;

    public GameEvent(GameEventType type){
        m_eventType = type;
    }

    public GameEventType getEventType() {
        return m_eventType;
    }

    public void notify(Entity entity, GameEntityProcessingSystem entityProcessingSystem){
        entityProcessingSystem.notifyObservers(entity, this);
    }
}
