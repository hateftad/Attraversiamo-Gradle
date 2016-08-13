package com.me.component;

import com.badlogic.gdx.math.Vector2;
import com.me.component.interfaces.ButtonStateObserverComponent;
import com.me.events.ButtonEvent;
import com.me.events.GameEventType;
import com.me.events.HorizontalButtonEvent;
import com.me.events.TaskEvent;
import com.me.utils.Converters;
import com.me.utils.Direction;

/**
 * Created by hateftadayon on 16/10/15.
 */
public class ContinuousParticles extends ParticleComponent implements ButtonStateObserverComponent {
    public ContinuousParticles(String effect, Vector2 position) {
        super(effect, position);
        setPosition(position);
    }

    @Override
    public void onNotify(ButtonEvent event) {
        if (event.getEventType() == GameEventType.HorizontalButton) {
            HorizontalButtonEvent buttonEvent = (HorizontalButtonEvent) event;

            if (buttonEvent.getDirection() == Direction.Left) {
                effects.clear();
                particle.getEmitters().get(0).getEmission().setHigh(100, 100);
                particle.getEmitters().get(0).getAngle().setHigh(5, 20);
                particle.getEmitters().get(0).getAngle().setLow(0, 0);
                particle.getEmitters().get(0).getVelocity().setHigh(1000, 1000);
                particle.getEmitters().get(0).getVelocity().setLow(0, 0);
            } else if (buttonEvent.getDirection() == Direction.Right) {
                effects.clear();
                particle.getEmitters().get(0).getEmission().setHigh(100, 100);
                particle.getEmitters().get(0).getAngle().setHigh(-10, 0);
                particle.getEmitters().get(0).getAngle().setLow(0, 0);
                particle.getEmitters().get(0).getVelocity().setHigh(-1000, -1000);
                particle.getEmitters().get(0).getVelocity().setLow(0, 0);
            }

        }
    }

    @Override
    public void onNotify(TaskEvent event) {

    }
}
