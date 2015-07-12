package com.me.component;

import com.me.interfaces.GameEvent;

/**
 * Created by hateftadayon on 6/7/15.
 */
public class EventComponent extends BaseComponent {

    private GameEvent m_eventInfo;

    public GameEvent getEventInfo() {
        return m_eventInfo;
    }

    public void setEventInfo(GameEvent eventInfo){
        m_eventInfo = eventInfo;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }
}
