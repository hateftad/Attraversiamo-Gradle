package com.me.event;

import com.artemis.Entity;
import com.me.level.tasks.BodyInfo;
import com.me.systems.GameEntityProcessingSystem;

/**
 * Created by hateftadayon on 7/13/15.
 */
public abstract class ButtonEvent extends MultiStateEvent {

    public ButtonEvent(BodyInfo bodyInfo) {
        super(bodyInfo);
    }


}
