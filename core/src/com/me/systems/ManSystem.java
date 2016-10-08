package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.me.component.*;
import com.me.config.GameConfig;
import com.me.config.GlobalConfig;
import com.me.events.GameEventType;
import com.me.events.LevelEvent;
import com.me.events.LevelEventType;
import com.me.events.TelegramEvent;
import com.me.events.states.PlayerState;
import com.me.level.Level;
import com.me.ui.InputManager;

/**
 * Created by hateftadayon on 12/31/15.
 */
public class ManSystem extends PlayerSystem {

    private float VELOCITY = 11.0f;
    private float VELOCITYINR = 3.0f;
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
    ComponentMapper<FeetRayCastComponent> rayCastComps;
    @Mapper
    ComponentMapper<HandHoldComponent> handHoldComps;
    @Mapper
    ComponentMapper<PlayerAIComponent> aiComponentMapper;

    @SuppressWarnings("unchecked")
    public ManSystem(Level currentLevel) {
        super(Aspect.getAspectForOne(PlayerOneComponent.class), currentLevel);

        inputMgr = InputManager.getInstance();
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
        RayCastComponent rayCastComponent = rayCastComps.get(entity);

        animation.setFacing(player.isFacingLeft());

        choose(entity);

        keyInputComponent.update(inputMgr);

        if (canBeControlled(player)) {

            if (!keyInputComponent.jump && !player.isLanding() && !player.isFalling()) {
                if (keyInputComponent.left) {
                    moveLeft(entity);
                } else if (keyInputComponent.right) {
                    moveRight(entity);
                }
            }

            if (keyInputComponent.jump) {
                if (touch.boxHandTouch && !player.isClimbingBox() && !player.isJumping()) {
                    movementComponent.standStill();
                    setPlayerState(entity, PlayerState.ClimbBox);
                    touch.boxHandTouch = false;
                } else {
                    jump(entity);
                }
                if (player.isHanging()) {
                    if (touch.pullEdgeTouch) {
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

                    if (touch.pullEdgeTouch) {
                        setPlayerState(entity, PlayerState.PullingLedge);
                    } else {
                        //temp fix for ground dust when climbing up
                        particleComps.get(entity).setEnabled(false);
                        setPlayerState(entity, PlayerState.ClimbingLedge);
                    }
                }
                if(physicsComponent.getTarget() != null){
                    PlayerAIComponent aiComponent = aiComponentMapper.get(entity);
                    aiComponent.setActivate(false);
                    TelegramEvent telegramEvent = new TelegramEvent(GameEventType.HoldingHandsLeading);
                    telegramEvent.notify(this, entity);
                }
            }
        }
        setPlayerState(entity);

        if (isDead(physicsComponent)) {
            notifyObservers(new LevelEvent(LevelEventType.OnDied, currentLevel));
        }

        checkFinished(touch, player, rayCastComponent);

        animateBody(physicsComponent, player, animation);

    }

    private void setPlayerState(Entity entity) {
        CharacterMovementComponent movementComponent = movementComps.get(entity);
        KeyInputComponent keyInput = movComps.get(entity);
        PhysicsComponent physicsComponent = physComps.get(entity);
        VelocityLimitComponent velocityLimitComponent = velComps.get(entity);
        PlayerComponent playerComponent = playerComps.get(entity);
        RayCastComponent rayCastComponent = rayCastComps.get(entity);
        TouchComponent touchComponent = touchComps.get(entity);
        PlayerAIComponent playerAIComponent = aiComponentMapper.get(entity);

        if (!keyInput.moved() && !playerAIComponent.shouldBeControlled()) {
            movementComponent.standStill();
            velocityLimitComponent.velocity = 0;
            if (playerComponent.shouldBeIdle() && !physicsComponent.isFalling()) {
                setPlayerState(entity, PlayerState.Idle);
                movementComponent.standStill();
            }
            if (playerComponent.isFalling() && rayCastComponent.hasCollided()) {
                setPlayerState(entity, PlayerState.Landing);
                movementComponent.standStill();
            }
        }

        if (physicsComponent.isFalling() &&
                !rayCastComponent.hasCollided() &&
                !playerComponent.isFalling()) {
            if (!playerComponent.isUpJumping()) {
                setPlayerState(entity, PlayerState.RunFalling);
            } else {
                setPlayerState(entity, PlayerState.Falling);
            }
        }
        if (playerComponent.isFalling() && rayCastComponent.hasCollided()) {
            if (playerComponent.getState() == PlayerState.RunFalling && movementComponent.shouldFallAndRun()) {
                setPlayerState(entity, PlayerState.RunLanding);
            } else {
                setPlayerState(entity, PlayerState.Landing);
                movementComponent.standStill();
            }
        }

        if (rayCastComponent.hasCollided() && !playerComponent.isHanging() && !playerComponent.isLanding()) {
            if (touchComponent.shouldPush()) {
                setPlayerState(entity, PlayerState.Pushing);
            } else {
                if (Math.abs(movementComponent.getSpeed()) >= velocityLimitComponent.walkLimit) {
                    setPlayerState(entity, PlayerState.Running);
                } else if (Math.abs(movementComponent.getSpeed()) > 0) {
                    setPlayerState(entity, PlayerState.Jogging);
                }
            }
        }

        if (!playerComponent.isActive() &&
                rayCastComponent.hasCollided() &&
                playerComponent.shouldBeIdle() &&
                !playerAIComponent.isBeingControlled()) {
            setPlayerState(entity, PlayerState.Idle);
            movementComponent.standStill();
        }
        if(movementComponent.isMoving()){
            playerComponent.setFacingLeft(movementComponent.runningLeft());
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
        RayCastComponent rayCastComponent = rayCastComps.get(entity);

        if (rayCastComponent.hasCollided() && !player.isJumping() && !player.isFalling()) {
            if (keyInputComponent.isMoving()) {
                if (velocityLimitForJumpBoost(entity)) {
                    physicsComponent.applyLinearImpulseAtPoint(PhysicsComponent.Center, new Vector2((keyInputComponent.left ? -20 : 20), physicsComponent.getBody(PhysicsComponent.Center).getMass() * 25));
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
        RayCastComponent rayCastComponent = rayCastComps.get(entity);
        JointComponent jointComponent = jointComp.get(entity);


        if (rayCastComponent.hasCollided() && !player.isHanging() && !player.isLanding()) {
            if (vel.velocity > 0) {
                vel.velocity = -VELOCITYINR;
            }
            if (touch.shouldPush()) {
                movementComponent.setVelocity(-vel.pushlimit);
            } else {
                vel.velocity -= VELOCITY * world.delta;
                movementComponent.setVelocity(vel.velocity);
                if (movementComponent.getSpeed() < -vel.walkLimit) {
                    movementComponent.setVelocity(-vel.walkLimit);
                    vel.velocity = -vel.walkLimit;
                }
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
    }

    private void moveRight(Entity entity) {

        VelocityLimitComponent vel = velComps.get(entity);
        PhysicsComponent ps = physComps.get(entity);
        CharacterMovementComponent movementComponent = movementComps.get(entity);
        TouchComponent touch = touchComps.get(entity);
        HangComponent hangComponent = hangComps.get(entity);
        PlayerComponent player = playerComps.get(entity);
        RayCastComponent rayCastComponent = rayCastComps.get(entity);
        JointComponent jointComponent = jointComp.get(entity);

        if (rayCastComponent.hasCollided() && !player.isHanging() && !player.isLanding()) {
            if (vel.velocity < 0) {
                vel.velocity = VELOCITYINR;
            }
            if (touch.shouldPush()) {
                movementComponent.setVelocity(vel.pushlimit);
            } else {
                vel.velocity += VELOCITY * world.delta;
                movementComponent.setVelocity(vel.velocity);
                if (movementComponent.getSpeed() > vel.walkLimit) {
                    movementComponent.setVelocity(vel.walkLimit);
                    vel.velocity = vel.walkLimit;
                }
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
        if (inputMgr.getSelected() == InputManager.PlayerOne) {
            playerComps.get(entity).setActive(true);
            aiComponentMapper.get(entity).setActivate(false);
        } else if (playerComps.get(entity).canDeActivate()) {
            playerComps.get(entity).setActive(false);
            velComps.get(entity).velocity = 0;
        }

    }

    private boolean velocityLimitForJumpBoost(Entity entity) {

        PhysicsComponent physicsComponent = physComps.get(entity);
        KeyInputComponent keyInputComponent = movComps.get(entity);
        return !(keyInputComponent.left && physicsComponent.getLinearVelocity().x < -4) && !(keyInputComponent.right && physicsComponent.getLinearVelocity().x > 4);
    }

    @Override
    public boolean keyDown(int keycode) {
        inputMgr.keyDown(keycode);
        if (keycode == Input.Keys.R) {
            notifyObservers(new LevelEvent(LevelEventType.OnRestart));
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        inputMgr.keyUp(keycode);
        return true;
    }
}
