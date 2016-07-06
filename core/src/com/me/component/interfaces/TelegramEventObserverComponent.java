package com.me.component.interfaces;

import com.me.events.TelegramEvent;
/**
 * Created by hateftadayon on 12/30/15.
 */
public interface TelegramEventObserverComponent extends TaskEventObserverComponent {
    void onNotify(TelegramEvent event);
}
