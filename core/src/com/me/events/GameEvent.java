package com.me.events;

import com.me.component.PlayerComponent;
import com.me.systems.GameEntityProcessingSystem;
import com.me.systems.GameEntityWorld;

/**
 * Created by hateftadayon on 7/12/15.
 */
public class GameEvent extends Events {

    protected GameEventType eventType;

    public GameEvent(GameEventType type) {
        eventType = type;
    }

    public GameEventType getEventType() {
        return eventType;
    }

    public void notify(GameEntityProcessingSystem entityProcessingSystem, PlayerComponent.PlayerNumber playerNumber) {

    }

    @Override
    public void notify(GameEntityWorld entityProcessingSystem) {

    }
}
