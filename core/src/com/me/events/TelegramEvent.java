package com.me.events;

import com.artemis.Entity;
import com.me.component.PlayerComponent;
import com.me.systems.GameEntityProcessingSystem;
import com.me.systems.GameEntityWorld;

/**
 * Created by hateftadayon on 12/30/15.
 */
public class TelegramEvent extends GameEvent {
    private Entity m_entity;

    public TelegramEvent(GameEventType type) {
        super(type);
    }

    public void notify(GameEntityWorld entityProcessingSystem) {
        entityProcessingSystem.onNotify(this);
    }

    public void notify(GameEntityProcessingSystem entityProcessingSystem, Entity entity){
        m_entity = entity;
        entityProcessingSystem.notifyObservers(this);
    }

    public Entity getEntity() {
        return m_entity;
    }
}
