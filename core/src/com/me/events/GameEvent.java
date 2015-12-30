package com.me.events;

import com.me.component.PlayerComponent;
import com.me.systems.GameEntityProcessingSystem;
import com.me.systems.GameEntityWorld;

/**
 * Created by hateftadayon on 7/12/15.
 */
public class GameEvent extends Events {

    protected GameEventType m_eventType;

    public GameEvent(GameEventType type) {
        m_eventType = type;
    }

    public GameEventType getEventType() {
        return m_eventType;
    }

    public void notify(GameEntityProcessingSystem entityProcessingSystem, PlayerComponent.PlayerNumber playerNumber) {

    }

    @Override
    public void notify(GameEntityWorld entityProcessingSystem) {

    }
}
