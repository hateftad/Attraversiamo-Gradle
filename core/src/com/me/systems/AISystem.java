package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Vector2;
import com.me.component.AIComponent;
import com.me.component.PhysicsComponent;
import com.me.component.RayCastComponent;
import com.me.component.SteeringComponent;

/**
 * Created by hateftadayon on 9/28/16.
 */
public class AISystem extends GameEntityProcessingSystem {

    @Mapper
    private ComponentMapper<SteeringComponent> steeringComponentManager;
    @Mapper
    private ComponentMapper<PhysicsComponent> physicsComponentMapper;
    @Mapper
    private ComponentMapper<RayCastComponent> rayCastComponentMapper;

    public AISystem() {
        super(Aspect.getAspectForOne(AIComponent.class));
    }

    @Override
    protected void process(Entity e) {

        if(steeringComponentManager.has(e)) {
            SteeringComponent steeringComponent = steeringComponentManager.get(e);
            RayCastComponent rayCastComponent = rayCastComponentMapper.get(e);
            steeringComponent.update(world.delta);
            if(steeringComponent.getSteeringBehavior() == null && rayCastComponent.hasCollided()){
                Seek<Vector2> seek = new Seek<>(steeringComponent, rayCastComponent.getTarget());
                steeringComponent.setSteeringBehavior(seek);
            }
            PhysicsComponent physicsComponent = physicsComponentMapper.get(e);
            if(rayCastComponent.hasCollided()) {
                if (physicsComponent.getBody() != null) {
                    physicsComponent.setLinearVelocity(steeringComponent.getLinearVelocity());
                }
            } else {
                physicsComponent.setLinearVelocity(0);
            }
        }

    }
}
