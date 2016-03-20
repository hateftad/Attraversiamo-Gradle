package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.me.component.EventParticleComponent;
import com.me.events.ParticleEvent;

/**
 * Created by hateftadayon on 3/13/16.
 */
public class EventParticlesSystem extends GameEntityProcessingSystem {

    @Mapper
    ComponentMapper<EventParticleComponent> eventParticles;

    public EventParticlesSystem() {
        super(Aspect.getAspectForOne(EventParticleComponent.class));
    }

    @Override
    protected void process(Entity entity) {
        if(eventParticles.has(entity)){
            EventParticleComponent eventParticleComponent = eventParticles.get(entity);
            ParticleEvent event = eventParticleComponent.getEvent();
            if(event.isStarted() && eventParticleComponent.isCompleted()){
                eventParticleComponent.getEvent().notify((GameEntityWorld)world);
            }
        }
    }
}
