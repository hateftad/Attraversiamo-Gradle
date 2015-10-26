package com.me.component;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.math.Vector2;
import com.me.component.interfaces.ButtonStateObserverComponent;
import com.me.event.ButtonEvent;
import com.me.event.GameEventType;
import com.me.event.HorizontalButtonEvent;
import com.me.utils.Direction;

/**
 * Created by hateftadayon on 16/10/15.
 */
public class ContinuousParticles extends ParticleComponent implements ButtonStateObserverComponent {
    public ContinuousParticles(String effect, Vector2 position) {
        super(effect, ParticleType.CONTINIOUS, position);
    }

    @Override
    public void onNotify(Entity entity, ButtonEvent event) {
        if (event.getEventType() == GameEventType.HorizontalButton) {
            HorizontalButtonEvent buttonEvent = (HorizontalButtonEvent) event;

            if (buttonEvent.getDirection() == Direction.Left) {
                m_effects.clear();
                m_particle.getEmitters().get(0).getEmission().setHigh(100, 100);
                m_particle.getEmitters().get(0).getAngle().setHigh(5, 20);
                m_particle.getEmitters().get(0).getAngle().setLow(0, 0);
                m_particle.getEmitters().get(0).getVelocity().setHigh(1000, 1000);
                m_particle.getEmitters().get(0).getVelocity().setLow(0, 0);
            } else if (buttonEvent.getDirection() == Direction.Right) {
                m_effects.clear();
                m_particle.getEmitters().get(0).getEmission().setHigh(100, 100);
                m_particle.getEmitters().get(0).getAngle().setHigh(-10, 0);
                m_particle.getEmitters().get(0).getAngle().setLow(0, 0);
                m_particle.getEmitters().get(0).getVelocity().setHigh(-1000, -1000);
                m_particle.getEmitters().get(0).getVelocity().setLow(0, 0);
            }

        }
    }
}
