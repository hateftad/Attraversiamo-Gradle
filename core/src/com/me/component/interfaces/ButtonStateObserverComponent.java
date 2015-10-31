package com.me.component.interfaces;

import com.artemis.Entity;
import com.me.events.ButtonEvent;

/**
 * Created by hateftadayon on 7/13/15.
 */
public interface ButtonStateObserverComponent extends MultiStateObserverComponent {
    void onNotify(Entity entity, ButtonEvent event);
}
