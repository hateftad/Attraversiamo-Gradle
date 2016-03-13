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
    ComponentMapper<EventParticleComponent> m_eventParticles;

    public EventParticlesSystem() {
        super(Aspect.getAspectForOne(EventParticleComponent.class));
    }

    @Override
    protected void process(Entity entity) {
        if(m_eventParticles.has(entity)){
            EventParticleComponent eventParticleComponent = m_eventParticles.get(entity);
            ParticleEvent event = eventParticleComponent.getEvent();
            if(event.isStarted() && eventParticleComponent.isCompleted()){
                eventParticleComponent.getEvent().notify((GameEntityWorld)world);
            }
        }
    }
}
