package com.me.component.interfaces;

import com.me.events.ParticleEvent;

/**
 * Created by hateftadayon on 1/31/16.
 */
public interface ParticleEventObserverComponent {
    void onNotify(ParticleEvent particleEvent);
}
