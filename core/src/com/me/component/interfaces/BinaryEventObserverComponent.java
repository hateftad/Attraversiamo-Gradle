package com.me.component.interfaces;

import com.me.events.BinaryEvent;
/**
 * Created by hateftadayon on 12/30/15.
 */
public interface BinaryEventObserverComponent {
    void onNotify(BinaryEvent event);
}
