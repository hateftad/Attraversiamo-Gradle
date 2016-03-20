package com.me.component;

import com.me.events.GameEvent;

/**
 * Created by hateftadayon on 6/7/15.
 */
public class EventComponent extends BaseComponent {

    private GameEvent eventInfo;

    public GameEvent getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(GameEvent eventInfo){
        this.eventInfo = eventInfo;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }
}
