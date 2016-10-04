package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.me.component.*;
import com.me.events.LevelEvent;
import com.me.events.LevelEventType;
import com.me.events.states.PlayerState;
import com.me.level.Level;
import com.me.ui.InputManager;

/**
 * Created by hateftadayon on 1/5/16.
 */
public class GirlSystem extends PlayerSystem {

    private Level currentLevel;
    @Mapper
    ComponentMapper<PlayerComponent> playerComps;
    @Mapper
    ComponentMapper<PlayerAnimationComponent> animComps;
    @Mapper
    ComponentMapper<KeyInputComponent> movComps;
    @Mapper
    ComponentMapper<TouchComponent> touchComps;
    @Mapper
    ComponentMapper<PhysicsComponent> physComps;
    @Mapper
    ComponentMapper<VelocityLimitComponent> velComps;
    @Mapper
    ComponentMapper<PushComponent> pushComps;
    @Mapper
    ComponentMapper<CharacterMovementComponent> movementComps;
    @Mapper
    ComponentMapper<FeetRayCastComponent> feetComps;

    private float VELOCITY = 4.5f;

    @SuppressWarnings("unchecked")
    public GirlSystem(Level currentLevel) {
        super(Aspect.getAspectForOne(PlayerTwoComponent.class), currentLevel);
        inputMgr = InputManager.getInstance();
        this.currentLevel = currentLevel;
    }

    @Override
    protected void process(Entity entity) {

        PhysicsComponent physicsComponent = physComps.get(entity);
        PlayerComponent player = playerComps.get(entity);
        PlayerAnimationComponent animation = animComps.get(entity);
        TouchComponent touch = touchComps.get(entity);
        CharacterMovementComponent movementComponent = movementComps.get(entity);
        KeyInputComponent keyInputComponent = movComps.get(entity);

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
                jump(entity);
            }
            if (inputMgr.isDown(action)) {
                if (touch.canCrawl && !player.isCrawling()) {
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
                if (touch.cageTouch) {
                    if (!player.isHoldingCage()) {
                        setPlayerState(entity, PlayerState.HoldingCage);
                    }
                    if (!player.isSwingingCage() && player.isHoldingCage()) {
                        setPlayerState(entity, PlayerState.Swinging);
                    }
                }
            }
        }

        if (isDead(physicsComponent)) {
            notifyObservers(new LevelEvent(LevelEventType.OnDied, currentLevel));
        }

        setPlayerState(entity);

        checkFinished(touch, player, feetComps.get(entity));

        animateBody(physicsComponent, player, animation);

    }

    private void jump(Entity entity) {

        PlayerComponent player = playerComps.get(entity);
        KeyInputComponent keyInputComponent = movComps.get(entity);
        CharacterMovementComponent movementComponent = movementComps.get(entity);

        if (!player.isJumping()) {
            if (!keyInputComponent.isMoving()) {
                movementComponent.standStill();
                setPlayerState(entity, PlayerState.UpJump);
            }
        }
    }

    private void setPlayerState(Entity entity) {
        CharacterMovementComponent movementComponent = movementComps.get(entity);
        KeyInputComponent keyInput = movComps.get(entity);
        PhysicsComponent physicsComponent = physComps.get(entity);
        VelocityLimitComponent velocityLimitComponent = velComps.get(entity);
        PlayerComponent playerComponent = playerComps.get(entity);
        FeetRayCastComponent rayCastComponent = feetComps.get(entity);

        if (!keyInput.moved()) {
            movementComponent.standStill();
            velocityLimitComponent.velocity = 2;
            if (playerComponent.shouldBeIdle() &&
                    !physicsComponent.isFalling()) {
                setPlayerState(entity, PlayerState.Idle);
                movementComponent.standStill();
            }
            if (playerComponent.crawling()) {
                setPlayerState(entity, PlayerState.LyingDown);
            }
            if (playerComponent.isFalling() && rayCastComponent.hasCollided()) {
                setPlayerState(entity, PlayerState.Landing);
                movementComponent.standStill();
            }
        }

        if (physicsComponent.isFalling() &&
                !rayCastComponent.hasCollided() &&
                !playerComponent.isFalling()) {
            System.out.println("In the Air!");
            if (playerComponent.isRunning()) {
                setPlayerState(entity, PlayerState.RunFalling);
            } else {
                setPlayerState(entity, PlayerState.Falling);
            }
        }
        if (playerComponent.isFalling() && rayCastComponent.hasCollided()) {
            System.out.println("Hit the ground!");
            if (playerComponent.getState() == PlayerState.RunFalling) {
                setPlayerState(entity, PlayerState.RunLanding);
            } else {
                setPlayerState(entity, PlayerState.Landing);
                movementComponent.standStill();
            }
        }

        if (!playerComponent.isActive() &&
                !physicsComponent.isFalling() &&
                playerComponent.shouldBeIdle()) {
            setPlayerState(entity, PlayerState.Idle);
            movementComponent.standStill();
        }

    }

    private void moveLeft(Entity entity) {

        PlayerComponent player = playerComps.get(entity);
        CharacterMovementComponent movementComponent = movementComps.get(entity);
        VelocityLimitComponent vel = velComps.get(entity);
        TouchComponent touch = touchComps.get(entity);
        FeetRayCastComponent rayCastComponent = feetComps.get(entity);
        if (vel.velocity > 0) {
            vel.velocity = -4;
        }
        if (rayCastComponent.hasCollided() && !player.isCrawling()) {
            if (!touch.boxTouch) {

                vel.velocity -= VELOCITY * world.delta;
                movementComponent.setVelocity(vel.velocity);
                if (movementComponent.getSpeed() < -vel.walkLimit) {
                    movementComponent.setVelocity(-vel.walkLimit);
                    setPlayerState(entity, PlayerState.Running);
                    vel.velocity = -vel.walkLimit;
                } else {
                    setPlayerState(entity, PlayerState.Walking);
                }
            } else {
                PushComponent push = pushComps.get(entity);
                if (touch.boxTouch && push.pushLeft) {
                    setPlayerState(entity, PlayerState.Pushing);
                }
                if (touch.boxTouch && !push.pushLeft) {
                    movementComponent.setVelocity(-vel.walkLimit);
                }
            }
        }
        if (player.isLyingDown() || player.isCrawling()) {
            vel.velocity = -vel.crawlLimit * 2;
            movementComponent.setVelocity(vel.velocity);
            setPlayerState(entity, PlayerState.Crawl);
        }

        player.setFacingLeft(true);
    }

    private void moveRight(Entity entity) {

        CharacterMovementComponent movementComponent = movementComps.get(entity);
        PlayerComponent player = playerComps.get(entity);
        VelocityLimitComponent vel = velComps.get(entity);
        TouchComponent touch = touchComps.get(entity);
        FeetRayCastComponent rayCastComponent = feetComps.get(entity);
        if (vel.velocity < 0) {
            vel.velocity = 4;
        }
        if (rayCastComponent.hasCollided() && !player.isCrawling()) {
            if (!touch.boxTouch) {

                vel.velocity += VELOCITY * world.delta;
                movementComponent.setVelocity(vel.velocity);
                if (movementComponent.getSpeed() > vel.walkLimit) {
                    movementComponent.setVelocity(vel.walkLimit);
                    setPlayerState(entity, PlayerState.Running);
                    vel.velocity = vel.walkLimit;
                } else {
                    setPlayerState(entity, PlayerState.Walking);
                }
            } else {
                PushComponent push = pushComps.get(entity);
                if (touch.boxTouch && !push.pushLeft) {
                    setPlayerState(entity, PlayerState.Pushing);
                }
                if (touch.boxTouch && push.pushLeft) {
                    movementComponent.setVelocity(vel.walkLimit);
                }
            }
        }
        if (player.isLyingDown() || player.isCrawling()) {
            vel.velocity = vel.crawlLimit * 2;
            movementComponent.setVelocity(vel.velocity);
            setPlayerState(entity, PlayerState.Crawl);
        }
        player.setFacingLeft(false);
    }

    private void choose(Entity entity) {
        if (inputMgr.playerSelected == InputManager.PlayerSelection.TWO) {
            playerComps.get(entity).setActive(true);
        } else if (playerComps.get(entity).canDeActivate()) {
            playerComps.get(entity).setActive(false);
            velComps.get(entity).velocity = 0;
        }
        inputMgr.reset();
    }

    private boolean canBeControlled(PlayerComponent player) {
        return player.isActive() && !player.isFinishing() && !player.lyingDown() && !player.isGettingUp() && !player.isPullingUp();
    }

    protected void setPlayerState(Entity entity, PlayerState state) {
        animComps.get(entity).setAnimationState(state);
        playerComps.get(entity).setState(state);
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
