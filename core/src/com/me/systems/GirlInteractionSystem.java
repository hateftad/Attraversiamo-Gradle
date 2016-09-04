package com.me.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.me.component.*;
import com.me.events.*;
import com.me.events.states.PlayerState;
import com.me.level.Level;

import static com.artemis.Aspect.getAspectForAll;

/**
 * Created by hateftadayon on 1/5/16.
 */
public class GirlInteractionSystem extends GameEntityProcessingSystem  {


    @Mapper
    ComponentMapper<PlayerAnimationComponent> animComps;
    @Mapper
    ComponentMapper<PlayerComponent> playerComp;
    @Mapper
    ComponentMapper<PhysicsComponent> physComp;
    @Mapper
    ComponentMapper<TouchComponent> touchComp;
    @Mapper
    ComponentMapper<GrabComponent> grabComps;
    @Mapper
    ComponentMapper<CharacterMovementComponent> movementComps;
    @Mapper
    ComponentMapper<EventComponent> eventComps;
    @Mapper
    ComponentMapper<VelocityLimitComponent> velComps;

    @SuppressWarnings("unchecked")
    public GirlInteractionSystem() {
        super(getAspectForAll(PlayerTwoComponent.class));
    }

    @Override
    protected void process(Entity entity) {

        TouchComponent touchComponent = touchComp.get(entity);
        PlayerComponent playerComponent = playerComp.get(entity);
        PlayerAnimationComponent animation = animComps.get(entity);
        PhysicsComponent physicsComponent = physComp.get(entity);
        CharacterMovementComponent movementComponent = movementComps.get(entity);
        VelocityLimitComponent velocityLimitComponent = velComps.get(entity);
        GrabComponent grabComponent = grabComps.get(entity);

        if (touchComponent.handTouch && playerComponent.isJumping()) {
            if (!playerComponent.isPullingUp()) {
                physicsComponent.makeStatic("torso");
                setPlayerState(entity, PlayerState.PullUp);
            }
        }
        if (playerComponent.isPullingUp()) {
            if (animation.isCompleted(PlayerState.PullUp)) {
                setPlayerState(entity, PlayerState.Idle);
                physicsComponent.setAllBodiesPosition(animation.getPositionRelative("left foot"));
                physicsComponent.makeDynamic("torso");
                touchComponent.handTouch = false;
            }
        }
        if (playerComponent.lyingDown()) {
            if (animation.isCompleted(PlayerState.LieDown)) {
                physicsComponent.disableBody(PhysicsComponent.Center);
                setPlayerState(entity, PlayerState.LyingDown);
            }
        }
        if (playerComponent.isCrawling() && !touchComponent.canCrawl) {
            setPlayerState(entity, PlayerState.StandUp);
        }

        if (playerComponent.isGettingUp()) {
            if (animation.isCompleted(PlayerState.StandUp)) {
                setPlayerState(entity, PlayerState.Idle);
            }
            movementComponent.standStill();
        }
        if (playerComponent.isJumping()) {
            if (animation.getEvent().getEventType() == AnimationEvent.AnimationEventType.JUMPUP) {
                animation.getEvent().resetEvent();
                physicsComponent.applyLinearImpulse(0, 3.5f);
            } else if (animation.getEvent().getEventType() == AnimationEvent.AnimationEventType.JUMP) {
                animation.getEvent().resetEvent();
                physicsComponent.applyLinearImpulse(physicsComponent.getLinearVelocity().x, 0.4f);
            }
            if (animation.isCompleted(PlayerState.UpJump) || animation.isCompleted(PlayerState.Jumping)) {
                setPlayerState(entity, PlayerState.Idle);
            }
        }
        if(playerComponent.isSwingingCage()){
            if (animation.getEvent().getEventType() == AnimationEvent.AnimationEventType.SWING) {
                animation.getEvent().resetEvent();
                notifyObservers(new TaskEvent(playerComponent.isFacingLeft() ? GameEventType.LeftImpulse : GameEventType.RightImpulse));
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

        if(playerComponent.isFinishing()){
            if(animation.isCompleted(PlayerState.RunOut)){
                notifyObservers(new TaskEvent(GameEventType.FinishAnimationDone, playerComponent.getPlayerNr()));
            }
        }

        if(touchComponent.waterTouch){
            setPlayerState(entity, PlayerState.Drowning);
        }

        if(touchComponent.boxTouch && !playerComponent.isActive()){
            if(!physicsComponent.isImmovable()) {
                physicsComponent.makeImmovable();
            }
        } else {
            if(physicsComponent.isImmovable()) {
                physicsComponent.makeMovable();
            }
        }

        if(playerComponent.isLanding()){
            if(animation.isCompleted(PlayerState.Landing)){
                velocityLimitComponent.velocity = 0;
                setPlayerState(entity, PlayerState.Idle);
            }
            if(animation.isCompleted(PlayerState.RunLanding)){
                setPlayerState(entity, PlayerState.Running);
            }
        }

        if(playerComponent.isDrowning()){
            if(animation.isCompleted(PlayerState.Drowning)){
                notifyObservers(new LevelEvent(LevelEventType.OnDied));
                setPlayerState(entity, PlayerState.Idle);
            }
        }
    }

    protected void setPlayerState(Entity entity, PlayerState state) {
        animComps.get(entity).setAnimationState(state);
        playerComp.get(entity).setState(state);
    }

}
