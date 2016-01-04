package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.me.component.*;
import com.me.events.states.PlayerState;

/**
 * Created by hateftadayon on 1/2/16.
 */
public class PlayerInteractionSystem extends PlayerSystem {
    @Mapper
    ComponentMapper<PlayerComponent> m_playerComps;
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
    @Mapper
    ComponentMapper<KeyInputComponent> m_movComp;
    @Mapper
    ComponentMapper<GrabComponent> m_grabComps;

    @SuppressWarnings("unchecked")
    public PlayerInteractionSystem() {
        super(Aspect.getAspectForAll(PlayerOneComponent.class));
    }

    @Override
    protected void process(Entity entity) {
        JointComponent jointComponent = m_jointComp.get(entity);
        TouchComponent touchComponent = m_touchComp.get(entity);
        PlayerComponent playerComponent = m_playerComp.get(entity);
        AnimationComponent animation = m_animComps.get(entity);
        PhysicsComponent physicsComponent = m_physComp.get(entity);
        HangComponent hangComponent = m_hangComp.get(entity);

        if (touchComponent.m_edgeTouch) {
            if(!playerComponent.isHanging()) {
                jointComponent.createHangJoint();
                setPlayerState(entity, PlayerState.Hanging);
            }
        }
        if(playerComponent.isClimbingLedge()) {
            if (animation.isCompleted(PlayerState.ClimbingLedge)) {
                jointComponent.destroyHangJoint();
                physicsComponent.setAllBodiesPosition(animation.getPositionRelative("left upper leg"));
                physicsComponent.setBodyActive(true);
                hangComponent.notHanging();
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
