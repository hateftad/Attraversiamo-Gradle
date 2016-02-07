package com.me.component;

import com.badlogic.gdx.math.Vector2;
import com.me.component.interfaces.ParticleEventObserverComponent;
import com.me.events.ParticleEvent;
import com.me.utils.Converters;

/**
 * Created by hateftadayon on 1/31/16.
 */
public class EventParticleComponent extends ParticleComponent implements ParticleEventObserverComponent {

    private int m_id;

    public EventParticleComponent(String effect, int id, Vector2 position) {
        super(effect, ParticleType.SINGLE, 10);
        m_id = id;
        setPosition(Converters.ToWorld(position));
    }

    @Override
    public void onNotify(ParticleEvent particleEvent) {
        if(particleEvent.getEventId() == m_id){
            start();
        }
    }
}
