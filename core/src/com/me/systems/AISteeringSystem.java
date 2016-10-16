package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.me.component.*;
import com.me.events.states.PlayerState;

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
    private ComponentMapper<FeetRayCastComponent> feetRayCastComponentMapper;
    @Mapper
    private ComponentMapper<AIComponent> aiComponentMapper;
    @Mapper
    private ComponentMapper<CharacterMovementComponent> characterMovementMapper;
    @Mapper
    private ComponentMapper<AIAnimationComponent> animationComponentMapper;
    @Mapper
    private ComponentMapper<VelocityLimitComponent> velComps;

    public AISteeringSystem() {
        super(Aspect.getAspectForOne(AIComponent.class));
    }

    @Override
    protected void process(Entity entity) {

        AIComponent aiComponent = aiComponentMapper.get(entity);
        aiComponent.update(world.delta);
        if(aiComponent.getSteeringEntity() != null) {
            EyeRayCastComponent rayCastComponent = rayCastComponentMapper.get(entity);
            SteeringEntity steeringComponent = aiComponent.getSteeringEntity();
            steeringComponent.update(world.delta);
            if(rayCastComponent.hasCollided() && aiComponent.getTarget() == null){
                aiComponent.setTarget(rayCastComponent.getTarget());
            }
            CharacterMovementComponent movementComponent = characterMovementMapper.getSafe(entity);
            movementComponent.setVelocity(steeringComponent.getLinearVelocity().x);
        }

        if(rayCastComponentMapper.has(entity)) {
            EyeRayCastComponent rayCastComponent = rayCastComponentMapper.get(entity);
            if (rayCastComponent.hasCollided() &&
                    rayCastComponent.getCollisionTime() + TimeUnit.SECONDS.toMillis(10) < System.currentTimeMillis()) {
                rayCastComponent.clearTarget();
                aiComponent.setTarget(null);
            }
        }

        setAiState(entity);

    }

    private void setAiState(Entity entity){
        CharacterMovementComponent movementComponent = characterMovementMapper.get(entity);
        VelocityLimitComponent velocityLimitComponent = velComps.get(entity);
        AIAnimationComponent aiAnimationComponent = animationComponentMapper.get(entity);
        RayCastComponent rayCastComponent = feetRayCastComponentMapper.get(entity);

        if(rayCastComponent.hasCollided()){
            if (Math.abs(movementComponent.getSpeed()) >= velocityLimitComponent.walkLimit) {
                setPlayerState(entity, PlayerState.Running);
            } else if (Math.abs(movementComponent.getSpeed()) > 0.5) {
                setPlayerState(entity, PlayerState.Walking);
            } else {
                setPlayerState(entity, PlayerState.Idle);
            }
        }

        if(movementComponent.isMoving()){
            aiAnimationComponent.setFacing(movementComponent.runningLeft());
        }

    }

    private void setPlayerState(Entity entity, PlayerState state) {
        animationComponentMapper.get(entity).setAnimationState(state);
//        playerComps.get(entity).setState(state);
    }

}
