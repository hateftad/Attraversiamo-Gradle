package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.IntervalEntityProcessingSystem;
import com.me.component.ContinuousParticles;
import com.me.component.EventParticleComponent;
import com.me.component.ParticleComponent;
import com.me.events.TaskEvent;

/**
 * Created by hateftadayon on 16/10/15.
 */
public class ContinousParticlesSystem extends IntervalEntityProcessingSystem {

    @Mapper
    ComponentMapper<ContinuousParticles> continuousParticles;


    @SuppressWarnings("unchecked")
    public ContinousParticlesSystem(float interval) {
        super(Aspect.getAspectForOne(ContinuousParticles.class, EventParticleComponent.class), interval);
    }

    @Override
    protected void process(Entity entity) {
        if(continuousParticles.has(entity)) {
            ContinuousParticles particle = continuousParticles.get(entity);
            particle.start();
        }

    }


}
