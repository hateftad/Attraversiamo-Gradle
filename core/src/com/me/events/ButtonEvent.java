package com.me.events;

import com.me.level.tasks.BodyInfo;

/**
 * Created by hateftadayon on 7/13/15.
 */
public abstract class ButtonEvent extends MultiStateEvent {

    public ButtonEvent(BodyInfo bodyInfo) {
        super(bodyInfo);
    }


}
