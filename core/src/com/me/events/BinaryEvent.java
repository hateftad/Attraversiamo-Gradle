package com.me.events;

import com.me.systems.GameEntityWorld;

/**
 * Created by hateftadayon on 12/30/15.
 */
public class BinaryEvent extends GameEvent {
    private boolean m_active;
    public BinaryEvent(GameEventType type, boolean active) {
        super(type);
        m_active = active;
    }

    public boolean isActive(){
        return m_active;
    }

    public void notify(GameEntityWorld entityProcessingSystem) {
        entityProcessingSystem.onNotify(this);
    }
}
