package com.me.interfaces;

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
}
