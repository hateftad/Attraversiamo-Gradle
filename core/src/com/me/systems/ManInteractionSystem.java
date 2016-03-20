package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.me.component.*;
import com.me.events.AnimationEvent;
import com.me.events.GameEventType;
import com.me.events.ParticleEvent;
import com.me.events.TaskEvent;
import com.me.events.states.PlayerState;

/**
 * Created by hateftadayon on 1/2/16.
 */
public class ManInteractionSystem extends PlayerSystem {

    @Mapper
    ComponentMapper<PlayerAnimationComponent> animComps;
    @Mapper
    ComponentMapper<PlayerComponent> playerComp;
    @Mapper
    ComponentMapper<PhysicsComponent> physComp;
    @Mapper
    ComponentMapper<TouchComponent> touchComp;
    @Mapper
    ComponentMapper<HangComponent> hangComp;
    @Mapper
    ComponentMapper<JointComponent> jointComp;
    @Mapper
    ComponentMapper<EventComponent> eventComps;

    @Mapper
    ComponentMapper<VelocityLimitComponent> velComps;
    @Mapper
    ComponentMapper<CharacterMovementComponent> movementComps;
    @SuppressWarnings("unchecked")
    public ManInteractionSystem() {
        super(Aspect.getAspectForAll(PlayerOneComponent.class));
    }

    @Override
    protected void process(Entity entity) {
        JointComponent jointComponent = jointComp.get(entity);
        TouchComponent touchComponent = touchComp.get(entity);
        PlayerComponent playerComponent = playerComp.get(entity);
        PlayerAnimationComponent animation = animComps.get(entity);
        PhysicsComponent physicsComponent = physComp.get(entity);
        HangComponent hangComponent = hangComp.get(entity);
        CharacterMovementComponent movementComponent = movementComps.get(entity);
        VelocityLimitComponent velocityLimitComponent = velComps.get(entity);

        if (touchComponent.edgeTouch) {
            if (!playerComponent.isHanging()) {
                jointComponent.createHangJoint();
            }
        }

        if (jointComponent.isHanging() && !playerComponent.isClimbing()) {
            if(!playerComponent.isPullingLedge()) {
                setPlayerState(entity, PlayerState.Hanging);
            }
            if(animation.getEvent().getEventType() == AnimationEvent.AnimationEventType.PULLLEDGE){
                notifyObservers(new TaskEvent(GameEventType.PullingLedge));
                notifyObservers(new ParticleEvent(GameEventType.Particle, 1));
                jointComponent.destroyHangJoint();
                setPlayerState(entity, PlayerState.Idle);
            }
        }
        if (playerComponent.isClimbing()) {
            if (animation.isCompleted(PlayerState.ClimbingLedge)) {
                jointComponent.destroyHangJoint();
                physicsComponent.setAllBodiesPosition(animation.getPositionRelative("left upper leg"));
                physicsComponent.setBodyActive(true);
                hangComponent.notHanging();

                movementComponent.standStill();
                velocityLimitComponent.standStill();
                setPlayerState(entity, PlayerState.Idle);
            }
            if(animation.isCompleted(PlayerState.ClimbBox)){
                physicsComponent.setAllBodiesPosition(animation.getPositionRelative("left upper leg"));
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
                touchComponent.handTouch = false;
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

        if(playerComponent.isPressingButton()){
            if(animation.isCompleted(PlayerState.PressButton)){
                setPlayerState(entity, PlayerState.Idle);
            }
        }

        if(playerComponent.isFinishing()){
            if(animation.isCompleted(PlayerState.RunOut)){
                notifyObservers(new TaskEvent(GameEventType.FinishAnimationDone, playerComponent.getPlayerNr()));
            }
        }

        if(playerComponent.isPressingButton()){
            if (animation.getEvent().getEventType() == AnimationEvent.AnimationEventType.PRESSINGBUTTON){
                animation.getEvent().resetEvent();
                EventComponent component = eventComps.get(entity);
                component.getEventInfo().notify(this, playerComponent.getPlayerNr());
            }

            if(animation.isCompleted(PlayerState.PressButton)){
                setPlayerState(entity, PlayerState.Idle);
            }
        }

        if(touchComponent.waterTouch){
            setPlayerState(entity, PlayerState.Drowning);
        }
    }

    @Override
    protected void setPlayerState(Entity entity, PlayerState state) {
        animComps.get(entity).setAnimationState(state);
        playerComp.get(entity).setState(state);
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
