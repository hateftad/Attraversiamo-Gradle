package com.me.interfaces;

import com.artemis.Entity;
import com.me.component.BaseComponent;

/**
 * Created by hateftadayon on 7/7/15.
 */
public abstract class GameEventObserverComponent extends BaseComponent {
    public abstract void onNotify(Entity entity, GameEvent event);
}
