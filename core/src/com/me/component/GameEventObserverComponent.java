package com.me.component;

import com.me.event.GameEventType;

/**
 * Created by hateftadayon on 7/7/15.
 */
public abstract class GameEventObserverComponent extends BaseComponent {
    public abstract void onNotify(GameEventType type);
}
