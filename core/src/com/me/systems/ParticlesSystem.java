package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.IntervalEntityProcessingSystem;
import com.me.component.ContinuousParticles;

/**
 * Created by hateftadayon on 16/10/15.
 */
public class ParticlesSystem extends IntervalEntityProcessingSystem {

    @Mapper
    ComponentMapper<ContinuousParticles> m_particleComps;

    public ParticlesSystem(float interval) {
        super(Aspect.getAspectForOne(ContinuousParticles.class), interval);

    }

    @Override
    protected void process(Entity e) {
        ContinuousParticles particle = m_particleComps.get(e);
        //if (particle.isCompleted()) {
            particle.start();
        //}
    }
}
