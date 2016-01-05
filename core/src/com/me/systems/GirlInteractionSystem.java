package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.me.component.*;
import com.me.events.AnimationEvent;
import com.me.events.states.PlayerState;

import static com.artemis.Aspect.getAspectForAll;

/**
 * Created by hateftadayon on 1/5/16.
 */
public class GirlInteractionSystem extends PlayerSystem {


    @Mapper
    ComponentMapper<PlayerAnimationComponent> m_animComps;
    @Mapper
    ComponentMapper<PlayerComponent> m_playerComp;
    @Mapper
    ComponentMapper<PhysicsComponent> m_physComp;
    @Mapper
    ComponentMapper<TouchComponent> m_touchComp;
    @Mapper
    ComponentMapper<GrabComponent> m_grabComps;
    @Mapper
    ComponentMapper<CharacterMovementComponent> m_movementComps;

    @SuppressWarnings("unchecked")
    public GirlInteractionSystem() {
        super(getAspectForAll(PlayerTwoComponent.class));
    }

    @Override
    protected void process(Entity entity) {

        TouchComponent touchComponent = m_touchComp.get(entity);
        PlayerComponent playerComponent = m_playerComp.get(entity);
        PlayerAnimationComponent animation = m_animComps.get(entity);
        PhysicsComponent physicsComponent = m_physComp.get(entity);
        CharacterMovementComponent movementComponent = m_movementComps.get(entity);

        if(touchComponent.m_handTouch && playerComponent.isJumping()){
            if(!playerComponent.isPullingUp()){
                GrabComponent grabComponent = m_grabComps.get(entity);
                physicsComponent.setBodyActive(false);
                physicsComponent.setPosition(grabComponent.handPositionX, physicsComponent.getPosition().y);
                setPlayerState(entity, PlayerState.PullUp);
            }
        }
        if (playerComponent.isPullingUp()) {
            if(animation.isCompleted(PlayerState.PullUp)){
                physicsComponent.setBodyActive(true);
                physicsComponent.setAllBodiesPosition(animation.getPositionRelative("left foot"));
                touchComponent.m_handTouch = false;
                setPlayerState(entity, PlayerState.Idle);
            }
        }
        if(playerComponent.lyingDown()){
            if(animation.isCompleted(PlayerState.LieDown)){
                physicsComponent.disableBody("center");
                setPlayerState(entity, PlayerState.LyingDown);
            }
        }
        if(playerComponent.isCrawling() && !touchComponent.m_canCrawl){
            setPlayerState(entity, PlayerState.StandUp);
        }

        if(playerComponent.isGettingUp()){
            if(animation.isCompleted(PlayerState.StandUp)){
                physicsComponent.enableBody("center");
                setPlayerState(entity, PlayerState.Idle);
            }
            movementComponent.standStill();
        }
        if(playerComponent.isJumping()){
            if(animation.getEvent().getEventType() == AnimationEvent.AnimationEventType.JUMPUP){
                animation.getEvent().resetEvent();
                physicsComponent.setLinearVelocity(0, 25);
            } else if(animation.getEvent().getEventType() == AnimationEvent.AnimationEventType.JUMP){
                animation.getEvent().resetEvent();
                physicsComponent.setLinearVelocity(physicsComponent.getLinearVelocity().x, 7f);
            }
            if(animation.isCompleted(PlayerState.UpJump) || animation.isCompleted(PlayerState.Jumping)) {
                setPlayerState(entity, PlayerState.Idle);
            }
        }
    }

    @Override
    protected void setPlayerState(Entity entity, PlayerState state) {
        m_animComps.get(entity).setAnimationState(state);
        m_playerComp.get(entity).setState(state);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
}
