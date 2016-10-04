package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.me.component.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by hateftadayon on 9/28/16.
 */
public class AISteeringSystem extends GameEntityProcessingSystem {

    @Mapper
    private ComponentMapper<PhysicsComponent> physicsComponentMapper;
    @Mapper
    private ComponentMapper<EyeRayCastComponent> rayCastComponentMapper;
    @Mapper
    private ComponentMapper<AIComponent> aiComponentMapper;
    @Mapper
    private ComponentMapper<CharacterMovementComponent> characterMovementMapper;

    public AISteeringSystem() {
        super(Aspect.getAspectForOne(AIComponent.class));
    }

    @Override
    protected void process(Entity e) {

        AIComponent aiComponent = aiComponentMapper.get(e);
        aiComponent.update(world.delta);
        EyeRayCastComponent rayCastComponent = rayCastComponentMapper.get(e);
        if(aiComponent.getSteeringEntity() != null) {
            SteeringEntity steeringComponent = aiComponent.getSteeringEntity();
            steeringComponent.update(world.delta);
            if(rayCastComponent.hasCollided() && aiComponent.getTarget() == null){
                aiComponent.setTarget(rayCastComponent.getTarget());
            }
            CharacterMovementComponent movementComponent = characterMovementMapper.getSafe(e);
            movementComponent.setVelocity(steeringComponent.getLinearVelocity().x);
        }

        if(rayCastComponent.hasCollided() &&
                rayCastComponent.getCollisionTime() + TimeUnit.SECONDS.toMillis(10) < System.currentTimeMillis()){
            rayCastComponent.clearTarget();
            aiComponent.setTarget(null);
        }

    }

}
