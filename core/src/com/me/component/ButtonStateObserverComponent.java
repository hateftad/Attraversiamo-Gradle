package com.me.component;

import com.artemis.Entity;
import com.me.event.ButtonEvent;

/**
 * Created by hateftadayon on 7/13/15.
 */
public abstract class ButtonStateObserverComponent extends MultiStateObserverComponent {
    public abstract void onNotify(Entity entity, ButtonEvent event);
}
