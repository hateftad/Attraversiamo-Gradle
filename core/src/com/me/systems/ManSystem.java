package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.component.*;
import com.me.config.GameConfig;
import com.me.config.GlobalConfig;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;
import com.me.events.TelegramEvent;
import com.me.events.states.PlayerState;
import com.me.level.Level;
import com.me.listeners.LevelEventListener;
import com.me.ui.InputManager;

/**
 * Created by hateftadayon on 12/31/15.
 */
public class ManSystem extends PlayerSystem {

    private float VELOCITY = 11.0f;
    private float VELOCITYINR = 3.0f;
    private Level currentLevel;
    @Mapper
    ComponentMapper<SingleParticleComponent> particleComps;
    @Mapper
    ComponentMapper<JointComponent> jointComp;
    @Mapper
    ComponentMapper<PlayerComponent> playerComps;
    @Mapper
    ComponentMapper<PhysicsComponent> physComps;
    @Mapper
    ComponentMapper<PlayerAnimationComponent> animComps;
    @Mapper
    ComponentMapper<KeyInputComponent> movComps;
    @Mapper
    ComponentMapper<TouchComponent> touchComps;
    @Mapper
    ComponentMapper<HangComponent> hangComps;
    @Mapper
    ComponentMapper<VelocityLimitComponent> velComps;
    @Mapper
    ComponentMapper<PushComponent> pushComps;
    @Mapper
    ComponentMapper<CharacterMovementComponent> movementComps;
    @Mapper
    ComponentMapper<FeetComponent> rayCastComps;
    @Mapper
    ComponentMapper<HandHoldComponent> handHoldComps;

    @SuppressWarnings("unchecked")
    public ManSystem(Level currentLevel) {
        super(Aspect.getAspectForOne(PlayerOneComponent.class));

        inputMgr = InputManager.getInstance();
        this.currentLevel = currentLevel;
        if (GlobalConfig.getInstance().config.platform == GameConfig.Platform.DESKTOP) {
            Gdx.input.setInputProcessor(this);
        }
    }

    @Override
    protected void process(Entity entity) {
        PlayerComponent player = playerComps.get(entity);
        PlayerAnimationComponent animation = animComps.get(entity);
        TouchComponent touch = touchComps.get(entity);
        PhysicsComponent physicsComponent = physComps.get(entity);
        CharacterMovementComponent movementComponent = movementComps.get(entity);
        KeyInputComponent keyInputComponent = movComps.get(entity);
        FeetComponent feetComponent = rayCastComps.get(entity);

        animation.setFacing(player.isFacingLeft());

        choose(entity);

        keyInputComponent.update(inputMgr);

        if (canBeControlled(player)) {

            if(!keyInputComponent.jump && !player.isLanding() && !player.isFalling()) {
                if (keyInputComponent.left) {
                    moveLeft(entity);
                } else if (keyInputComponent.right) {
                    moveRight(entity);
                }
            }

            if (keyInputComponent.jump) {
                if(touch.boxTouch){
                    movementComponent.standStill();
                    setPlayerState(entity, PlayerState.ClimbBox);
                } else {
                    movementComponent.standStill();
                    jump(entity);
                }
                if (player.isHanging()) {
                    if(touch.pullEdgeTouch){
                        setPlayerState(entity, PlayerState.PullingLedge);
                    } else {
                        //temp fix for ground dust when climbing up
                        particleComps.get(entity).setEnabled(false);
                        setPlayerState(entity, PlayerState.ClimbingLedge);
                    }
                }
            }

            if (inputMgr.isDown(action)) {
                if (touch.footEdge) {
                    if (touch.footEdgeL) {
                        player.setFacingLeft(true);
                    }
                    if (touch.footEdgeR) {
                        player.setFacingLeft(false);
                    }
                    physicsComponent.warp("feet", touch.touchCenter);
                    setPlayerState(entity, PlayerState.LieDown);
                    movementComponent.standStill();
                }
                if (touch.pushArea) {
                    if (touch.leftPushArea) {
                        player.setFacingLeft(false);
                        setPlayerState(entity, PlayerState.PressButton);
                    }
                    if (touch.rightPushArea) {
                        player.setFacingLeft(true);
                        setPlayerState(entity, PlayerState.PressButton);
                    }
                    movementComponent.standStill();
                }
                if (touch.handHoldArea) {
                    holdHands(entity);
                }
                if (player.isHanging()) {

                    if(touch.pullEdgeTouch){
                        setPlayerState(entity, PlayerState.PullingLedge);
                    } else {
                        //temp fix for ground dust when climbing up
                        particleComps.get(entity).setEnabled(false);
                        setPlayerState(entity, PlayerState.ClimbingLedge);
                    }
                }
            }
        }
        setPlayerState(entity);
        System.out.println("State "+player.getState());
        if (isDead(physicsComponent, currentLevel) || animation.isCompleted(PlayerState.Drowning)) {
            inputMgr.callRestart();
        }

        checkFinished(touch, player, feetComponent);

        animateBody(physicsComponent, player, animation);

    }

    private void setPlayerState(Entity entity) {
        CharacterMovementComponent movementComponent = movementComps.get(entity);
        KeyInputComponent keyInput = movComps.get(entity);
        PhysicsComponent physicsComponent = physComps.get(entity);
        VelocityLimitComponent velocityLimitComponent = velComps.get(entity);
        PlayerComponent playerComponent = playerComps.get(entity);
        FeetComponent feetComponent = rayCastComps.get(entity);

        if (!keyInput.moved()) {
            movementComponent.standStill();
            velocityLimitComponent.velocity = 0;
            if (playerComponent.shouldBeIdle() && !physicsComponent.isFalling()) {
                setPlayerState(entity, PlayerState.Idle);
                movementComponent.standStill();
            }
        }

        if (physicsComponent.isFalling() &&
                !playerComponent.isHanging()) {
            if(playerComponent.isFalling() && feetComponent.hasCollided()){
                setPlayerState(entity, PlayerState.Landing);
            } else {
                setPlayerState(entity, PlayerState.Falling);
            }
        }

        if(playerComponent.isFalling() && feetComponent.hasCollided()){
            setPlayerState(entity, PlayerState.Landing);
        }

        if(!playerComponent.isActive() &&
                feetComponent.hasCollided() &&
                playerComponent.shouldBeIdle()){
            setPlayerState(entity, PlayerState.Idle);
            movementComponent.standStill();
        }

    }

    protected void setPlayerState(Entity entity, PlayerState state) {
        animComps.get(entity).setAnimationState(state);
        playerComps.get(entity).setState(state);
    }

    private void jump(Entity entity) {
        PlayerComponent player = playerComps.get(entity);
        PhysicsComponent physicsComponent = physComps.get(entity);
        KeyInputComponent keyInputComponent = movComps.get(entity);
        FeetComponent feetComponent = rayCastComps.get(entity);

        if (feetComponent.hasCollided() && !player.isJumping()) {
            if (keyInputComponent.isMoving()) {
                if (velocityLimitForJumpBoost(entity)) {
                    physicsComponent.applyLinearImpulseAtPoint(PhysicsComponent.Center, new Vector2((keyInputComponent.left ? -10 : 10), physicsComponent.getBody(PhysicsComponent.Center).getMass() * 25));
                } else {
                    physicsComponent.applyLinearImpulseAtPoint(PhysicsComponent.Center, new Vector2(physicsComponent.getLinearVelocity().x * 3, physicsComponent.getBody(PhysicsComponent.Center).getMass() * 25));
                }
                setPlayerState(entity, PlayerState.Jumping);
            } else {
                setPlayerState(entity, PlayerState.UpJump);
            }
        }
    }

    private void moveLeft(Entity entity) {

        VelocityLimitComponent vel = velComps.get(entity);
        PhysicsComponent ps = physComps.get(entity);
        CharacterMovementComponent movementComponent = movementComps.get(entity);
        TouchComponent touch = touchComps.get(entity);
        HangComponent hangComponent = hangComps.get(entity);
        PlayerComponent player = playerComps.get(entity);
        PushComponent push = pushComps.get(entity);
        FeetComponent feetComponent = rayCastComps.get(entity);
        JointComponent jointComponent = jointComp.get(entity);


        if (feetComponent.hasCollided() && !player.isHanging()) {
            if (vel.velocity > 0) {
                vel.velocity = -VELOCITYINR;
            }
            if (!touch.boxTouch) {
                vel.velocity -= VELOCITY * world.delta;
                movementComponent.setVelocity(vel.velocity);
                if (movementComponent.getSpeed() < -vel.walkLimit) {
                    movementComponent.setVelocity(-vel.walkLimit);
                    setPlayerState(entity, PlayerState.Running);
                    vel.velocity = -vel.walkLimit;
                } else {
                    setPlayerState(entity, PlayerState.Jogging);
                }
            }
            if (touch.boxTouch && push.pushLeft) {
                movementComponent.setVelocity(-vel.pushlimit);
                setPlayerState(entity, PlayerState.Pushing);
            }
            if (touch.boxTouch && !push.pushLeft) {
                setPlayerState(entity, PlayerState.Walking);
                movementComponent.setVelocity(-vel.pushlimit);
            }
            if (!ps.isDynamic()) {
                setPlayerState(entity, PlayerState.Walking);
                ps.makeDynamic();
                ps.setLinearVelocity(-vel.walkLimit, ps.getLinearVelocity().y);
                touch.ladderTouch = false;
            }
        }
        if (player.isHanging()) {
            if (hangComponent.hangingRight) {
                jointComponent.destroyHangJoint();
                setPlayerState(entity, PlayerState.Falling);
            }
        }
        player.setFacingLeft(true);
    }

    private void moveRight(Entity entity) {

        VelocityLimitComponent vel = velComps.get(entity);
        PhysicsComponent ps = physComps.get(entity);
        CharacterMovementComponent movementComponent = movementComps.get(entity);
        TouchComponent touch = touchComps.get(entity);
        HangComponent hangComponent = hangComps.get(entity);
        PlayerComponent player = playerComps.get(entity);
        PushComponent push = pushComps.get(entity);
        FeetComponent feetComponent = rayCastComps.get(entity);
        JointComponent jointComponent = jointComp.get(entity);

        if (feetComponent.hasCollided() && !player.isHanging()) {
            if (vel.velocity < 0) {
                vel.velocity = VELOCITYINR;
            }
            if (!touch.boxTouch) {
                vel.velocity += VELOCITY * world.delta;
                movementComponent.setVelocity(vel.velocity);
                if (movementComponent.getSpeed() > vel.walkLimit) {
                    movementComponent.setVelocity(vel.walkLimit);
                    setPlayerState(entity, PlayerState.Running);
                    vel.velocity = vel.walkLimit;
                } else {
                    setPlayerState(entity, PlayerState.Jogging);
                }
            }
            if (touch.boxTouch && push.pushRight) {
                setPlayerState(entity, PlayerState.Pushing);
                movementComponent.setVelocity(vel.pushlimit);
            } else if (touch.boxTouch && !push.pushRight) {
                setPlayerState(entity, PlayerState.Walking);
                movementComponent.setVelocity(vel.pushlimit);
            }
            if (!ps.isDynamic()) {
                setPlayerState(entity, PlayerState.Walking);
                ps.makeDynamic();
                ps.setLinearVelocity(vel.walkLimit, ps.getLinearVelocity().y);
                touch.ladderTouch = false;
            }
        }
        if (player.isHanging()) {
            if (hangComponent.hangingLeft) {
                jointComponent.destroyHangJoint();
                setPlayerState(entity, PlayerState.Falling);
            }
        }
        player.setFacingLeft(false);
    }

    private void holdHands(Entity entity) {
        HandHoldComponent handHoldComponent = handHoldComps.get(entity);
        TouchComponent touch = touchComps.get(entity);
        PlayerComponent player = playerComps.get(entity);

        handHoldComponent.setHoldingHands(true);
        if (touch.rightHoldArea) {
            TelegramEvent telegramEvent;
            if (player.isFacingLeft()) {
                setPlayerState(entity, PlayerState.HoldHandLeading);
                handHoldComponent.setIsLeading(true);
                telegramEvent = new TelegramEvent(GameEventType.HoldingHandsLeading);
            } else {
                setPlayerState(entity, PlayerState.HoldHandFollowing);
                handHoldComponent.setIsLeading(false);
                telegramEvent = new TelegramEvent(GameEventType.HoldingHandsFollowing);
            }
            telegramEvent.notify(this, entity);
        }
        if (touch.leftHoldArea) {
            TelegramEvent telegramEvent;
            if (player.isFacingLeft()) {
                setPlayerState(entity, PlayerState.HoldHandFollowing);
                handHoldComponent.setIsLeading(false);
                telegramEvent = new TelegramEvent(GameEventType.HoldingHandsFollowing);
            } else {
                setPlayerState(entity, PlayerState.HoldHandLeading);
                handHoldComponent.setIsLeading(true);
                telegramEvent = new TelegramEvent(GameEventType.HoldingHandsLeading);
            }
            telegramEvent.notify(this, entity);
        }
    }

    private boolean canBeControlled(PlayerComponent player) {
        return player.isActive() && !player.isFinishing() &&
                !player.isClimbing() && !player.isPullingLedge() &&
                !player.isPullingUp();
    }

    private void choose(Entity entity) {
        if (inputMgr.playerSelected == InputManager.PlayerSelection.ONE) {
            playerComps.get(entity).setActive(true);
        } else if (playerComps.get(entity).canDeActivate()) {
            playerComps.get(entity).setActive(false);
            velComps.get(entity).velocity = 0;
        }

        inputMgr.reset();
    }

    private boolean velocityLimitForJumpBoost(Entity entity) {

        PhysicsComponent physicsComponent = physComps.get(entity);
        KeyInputComponent keyInputComponent = movComps.get(entity);
        if (keyInputComponent.left && physicsComponent.getLinearVelocity().x < -4) {
            return false;
        } else if (keyInputComponent.right && physicsComponent.getLinearVelocity().x > 4) {
            return false;
        }
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        inputMgr.keyDown(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        inputMgr.keyUp(keycode);
        return true;
    }
}
