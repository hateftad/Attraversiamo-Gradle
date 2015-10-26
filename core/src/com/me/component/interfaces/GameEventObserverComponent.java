package com.me.component.interfaces;

import com.me.event.GameEventType;

/**
 * Created by hateftadayon on 7/7/15.
 */
public interface GameEventObserverComponent{
    void onNotify(GameEventType type);
}
