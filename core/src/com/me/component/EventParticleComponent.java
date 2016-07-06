package com.me.component;

import com.badlogic.gdx.math.Vector2;
import com.me.component.interfaces.ParticleEventObserverComponent;
import com.me.events.ParticleEvent;
import com.me.events.TaskEvent;

/**
 * Created by hateftadayon on 1/31/16.
 */
public class EventParticleComponent extends ParticleComponent implements ParticleEventObserverComponent {

    private int id;
    private ParticleEvent event;

    public EventParticleComponent(String effect, int id, Vector2 position) {
        super(effect, 10);
        this.id = id;
        setPosition(position);
    }

    @Override
    public void onNotify(ParticleEvent particleEvent) {
        if (particleEvent.getEventId() == id) {
            if(!isStarted()) {
                start();
                event.setStarted(true);
            }
        }
    }

    public ParticleEvent getEvent() {
        return event;
    }

    public void setEvent(ParticleEvent event) {
        this.event = event;
    }

    @Override
    public void onNotify(TaskEvent event) {

    }
}
