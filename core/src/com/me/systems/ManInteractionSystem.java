package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.me.component.*;
import com.me.events.AnimationEvent;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;
import com.me.events.states.PlayerState;

/**
 * Created by hateftadayon on 1/2/16.
 */
public class ManInteractionSystem extends PlayerSystem {

    @Mapper
    ComponentMapper<PlayerAnimationComponent> m_animComps;
    @Mapper
    ComponentMapper<PlayerComponent> m_playerComp;
    @Mapper
    ComponentMapper<PhysicsComponent> m_physComp;
    @Mapper
    ComponentMapper<TouchComponent> m_touchComp;
    @Mapper
    ComponentMapper<HangComponent> m_hangComp;
    @Mapper
    ComponentMapper<JointComponent> m_jointComp;

    @SuppressWarnings("unchecked")
    public ManInteractionSystem() {
        super(Aspect.getAspectForAll(PlayerOneComponent.class));
    }

    @Override
    protected void process(Entity entity) {
        JointComponent jointComponent = m_jointComp.get(entity);
        TouchComponent touchComponent = m_touchComp.get(entity);
        PlayerComponent playerComponent = m_playerComp.get(entity);
        PlayerAnimationComponent animation = m_animComps.get(entity);
        PhysicsComponent physicsComponent = m_physComp.get(entity);
        HangComponent hangComponent = m_hangComp.get(entity);

        if (touchComponent.m_edgeTouch) {
            if (!playerComponent.isHanging()) {
                jointComponent.createHangJoint();
            }
        }

        if (jointComponent.isHanging() && !playerComponent.isClimbingLedge()) {
            if(!playerComponent.isPullingLedge()) {
                setPlayerState(entity, PlayerState.Hanging);
            }
            if(animation.getEvent().getEventType() == AnimationEvent.AnimationEventType.PULLLEDGE){
                notifyObservers(new TaskEvent(GameEventType.PullingLedge));
                jointComponent.destroyHangJoint();
                setPlayerState(entity, PlayerState.Idle);
            }
        }
        if (playerComponent.isClimbingLedge()) {
            if (animation.isCompleted(PlayerState.ClimbingLedge)) {
                jointComponent.destroyHangJoint();
                physicsComponent.setAllBodiesPosition(animation.getPositionRelative("left upper leg"));
                physicsComponent.setBodyActive(true);
                hangComponent.notHanging();
                setPlayerState(entity, PlayerState.Idle);
            }
        }
        if (touchComponent.canPullUp() &&
                !playerComponent.isPullingUp() &&
                playerComponent.lyingDown()) {
            setPlayerState(entity, PlayerState.PullUp);
        }
        if (playerComponent.isPullingUp()) {
            if (animation.isCompleted(PlayerState.PullUp)) {
                touchComponent.m_handTouch = false;
                setPlayerState(entity, PlayerState.Idle);
            }
        }

        if (playerComponent.isJumping()) {
            if (animation.getEvent().getEventType() == AnimationEvent.AnimationEventType.JUMPUP) {
                animation.getEvent().resetEvent();
                physicsComponent.applyLinearImpulse(physicsComponent.getLinearVelocity().x, 100);
            }
            if (animation.isCompleted(PlayerState.UpJump)) {
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
