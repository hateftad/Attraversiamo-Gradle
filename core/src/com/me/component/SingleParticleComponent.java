package com.me.component;

import com.me.component.interfaces.GameEventObserverComponent;
import com.me.events.GameEventType;

/**
 * Created by hateftadayon on 10/26/15.
 */
public class SingleParticleComponent extends ParticleComponent implements GameEventObserverComponent {


    public SingleParticleComponent(String effect, ParticleType type, int max) {
        super(effect, type, max);
    }

    @Override
    public void onNotify(GameEventType event) {
        if (event == GameEventType.AllReachedEnd) {
            if (m_type == ParticleType.PORTAL) {
                start();
            }
        }
    }

}
