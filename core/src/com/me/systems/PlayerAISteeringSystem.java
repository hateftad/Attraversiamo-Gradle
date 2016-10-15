package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.me.ai.utils.BehaviourFactory;
import com.me.component.*;
import com.me.events.states.PlayerState;

/**
 * Created by hateftadayon on 10/5/16.
 */
public class PlayerAISteeringSystem extends GameEntityProcessingSystem {

    @Mapper
    private ComponentMapper<PhysicsComponent> physicsComponentMapper;
    @Mapper
    private ComponentMapper<EyeRayCastComponent> rayCastComponentMapper;
    @Mapper
    private ComponentMapper<PlayerAIComponent> aiComponentMapper;
    @Mapper
    private ComponentMapper<CharacterMovementComponent> characterMovementMapper;
    @Mapper
    private ComponentMapper<PlayerAnimationComponent> animationComponentMapper;
    @Mapper
    private ComponentMapper<PlayerComponent> playerComps;

    public PlayerAISteeringSystem() {
        super(Aspect.getAspectForOne(PlayerAIComponent.class));
    }

    @Override
    protected void process(Entity entity) {
        PlayerAIComponent playerAIComponent = aiComponentMapper.get(entity);
        if(playerAIComponent.shouldBeControlled()){
            SteeringEntity steeringComponent = playerAIComponent.getSteeringEntity();
            steeringComponent.update(world.delta);
            if(steeringComponent.getSteeringBehavior() == null){
                steeringComponent.setSteeringBehavior(BehaviourFactory.createSeek(playerAIComponent.getSteeringEntity(), playerAIComponent.getTarget()));
            }
            CharacterMovementComponent movementComponent = characterMovementMapper.getSafe(entity);
            float velocity = steeringComponent.getLinearVelocity().x;
            if(Math.abs(velocity) > 1) {
                movementComponent.setVelocity(velocity);
            } else {
                movementComponent.standStill();
                setPlayerState(entity, PlayerState.Idle);
            }
        }
    }

    private void setPlayerState(Entity entity, PlayerState state) {
        animationComponentMapper.get(entity).setAnimationState(state);
        playerComps.get(entity).setState(state);
    }
}
