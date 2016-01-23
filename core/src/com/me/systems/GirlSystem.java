package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.me.component.*;
import com.me.events.states.PlayerState;
import com.me.ui.InputManager;

/**
 * Created by hateftadayon on 1/5/16.
 */
public class GirlSystem extends PlayerSystem {

    @Mapper
    ComponentMapper<PlayerComponent> m_playerComps;
    @Mapper
    ComponentMapper<PlayerAnimationComponent> m_animComps;
    @Mapper
    ComponentMapper<KeyInputComponent> m_movComps;
    @Mapper
    ComponentMapper<TouchComponent> m_touchComps;
    @Mapper
    ComponentMapper<PhysicsComponent> m_physComps;
    @Mapper
    ComponentMapper<VelocityLimitComponent> m_velComps;
    @Mapper
    ComponentMapper<PushComponent> m_pushComps;
    @Mapper
    ComponentMapper<EventComponent> m_eventComps;
    @Mapper
    ComponentMapper<CharacterMovementComponent> m_movementComps;
    @Mapper
    ComponentMapper<FeetComponent> m_feetComps;

    private float VELOCITY = 5.5f;

    @SuppressWarnings("unchecked")
    public GirlSystem() {
        super(Aspect.getAspectForOne(PlayerTwoComponent.class));
        m_inputMgr = InputManager.getInstance();
    }

    @Override
    protected void process(Entity entity) {

        PhysicsComponent physicsComponent = m_physComps.get(entity);
        PlayerComponent player = m_playerComps.get(entity);
        PlayerAnimationComponent animation = m_animComps.get(entity);
        TouchComponent touch = m_touchComps.get(entity);
        CharacterMovementComponent movementComponent = m_movementComps.get(entity);
        KeyInputComponent keyInputComponent = m_movComps.get(entity);

        animation.setFacing(player.isFacingLeft());

        choose(entity);

        keyInputComponent.update(m_inputMgr);

        if (canBeControlled(player)) {
            if (keyInputComponent.m_left) {
                moveLeft(entity);
            } else if (keyInputComponent.m_right) {
                moveRight(entity);
            }
            if (keyInputComponent.m_jump) {
                jump(entity);
            }
            if (m_inputMgr.isDown(action)) {
                if (touch.m_canCrawl && !player.isCrawling()) {
                    setPlayerState(entity, PlayerState.LieDown);
                    movementComponent.standStill();
                }
                if (touch.m_pushArea) {
                    EventComponent component = m_eventComps.get(entity);
                    if (touch.m_leftPushArea) {
                        player.setFacingLeft(false);
                        setPlayerState(entity, PlayerState.PressButton);
                        component.getEventInfo().notify(this, player.getPlayerNr());
                    }
                    if (touch.m_rightPushArea) {
                        player.setFacingLeft(true);
                        setPlayerState(entity, PlayerState.PressButton);
                        component.getEventInfo().notify(this, player.getPlayerNr());
                    }
                    movementComponent.standStill();
                }
                if(touch.m_cageTouch){
                    if(!player.isHoldingCage()){
                        setPlayerState(entity, PlayerState.HoldingCage);
                    }
                    if(!player.isSwingingCage() && player.isHoldingCage()) {
                        setPlayerState(entity, PlayerState.Swinging);
                    }
                }
            }
        }

        if (isDead(physicsComponent)) {
            m_inputMgr.callRestart();
        }

        setPlayerState(entity);

        animateBody(physicsComponent, player, animation);

    }

    private void jump(Entity entity) {

        PlayerComponent player = m_playerComps.get(entity);
        KeyInputComponent keyInputComponent = m_movComps.get(entity);

        if (!player.isJumping()) {
            if (keyInputComponent.isMoving()) {
                setPlayerState(entity, PlayerState.Jumping);
            } else {
                setPlayerState(entity, PlayerState.UpJump);
            }
        }
    }

    private void setPlayerState(Entity entity) {
        CharacterMovementComponent movementComponent = m_movementComps.get(entity);
        KeyInputComponent keyInput = m_movComps.get(entity);
        PhysicsComponent physicsComponent = m_physComps.get(entity);
        VelocityLimitComponent velocityLimitComponent = m_velComps.get(entity);
        PlayerComponent playerComponent = m_playerComps.get(entity);


        if (!keyInput.moved()) {
            movementComponent.standStill();
            velocityLimitComponent.m_velocity = 0;
            if (playerComponent.shouldBeIdle() &&
                    !physicsComponent.isFalling()) {
                setPlayerState(entity, PlayerState.Idle);
                movementComponent.standStill();
            }
            if (playerComponent.crawling()) {
                setPlayerState(entity, PlayerState.LyingDown);
            }
        }

        if (physicsComponent.isFalling() &&
                !playerComponent.isHanging() && !playerComponent.isJumping()) {
            setPlayerState(entity, PlayerState.Falling);
        }

        if(playerComponent.isFinishing()){
            if(!playerComponent.isSuckingIn()){
                levelFinished();
            }
        }

        if(!playerComponent.isActive() &&
                !physicsComponent.isFalling() &&
                playerComponent.shouldBeIdle()){
            setPlayerState(entity, PlayerState.Idle);
            movementComponent.standStill();
        }

    }

    private void moveLeft(Entity entity) {

        PlayerComponent player = m_playerComps.get(entity);
        CharacterMovementComponent movementComponent = m_movementComps.get(entity);
        VelocityLimitComponent vel = m_velComps.get(entity);
        TouchComponent touch = m_touchComps.get(entity);
        FeetComponent feetComponent = m_feetComps.get(entity);


        if (feetComponent.hasCollided() && !player.isCrawling()) {
            if (!touch.m_boxTouch) {
                if (vel.m_velocity > 0) {
                    vel.m_velocity = 0;
                }
                vel.m_velocity -= VELOCITY * world.delta;
                movementComponent.setVelocity(vel.m_velocity);
                if (movementComponent.getSpeed() < -vel.m_walkLimit) {
                    movementComponent.setVelocity(-vel.m_walkLimit);
                    setPlayerState(entity, PlayerState.Running);
                    vel.m_velocity = -vel.m_walkLimit;
                } else {
                    setPlayerState(entity, PlayerState.Walking);
                }
            } else {
                PushComponent push = m_pushComps.get(entity);
                if (touch.m_boxTouch && push.m_pushLeft) {
                    setPlayerState(entity, PlayerState.Pushing);
                }
                if (touch.m_boxTouch && !push.m_pushLeft) {
                    movementComponent.setVelocity(-vel.m_walkLimit);
                }
            }
        }
        if (player.isLyingDown() || player.isCrawling()) {
            vel.m_velocity = -vel.m_crawlLimit * 2;
            movementComponent.setVelocity(vel.m_velocity);
            setPlayerState(entity, PlayerState.Crawl);
        }

        player.setFacingLeft(true);
    }

    private void moveRight(Entity entity) {

        CharacterMovementComponent movementComponent = m_movementComps.get(entity);
        PlayerComponent player = m_playerComps.get(entity);
        VelocityLimitComponent vel = m_velComps.get(entity);
        TouchComponent touch = m_touchComps.get(entity);
        FeetComponent feetComponent = m_feetComps.get(entity);

        if (feetComponent.hasCollided() && !player.isCrawling()) {
            if (!touch.m_boxTouch) {
                if (vel.m_velocity < 0) {
                    vel.m_velocity = 0;
                }
                vel.m_velocity += VELOCITY * world.delta;
                movementComponent.setVelocity(vel.m_velocity);
                if (movementComponent.getSpeed() > vel.m_walkLimit) {
                    movementComponent.setVelocity(vel.m_walkLimit);
                    setPlayerState(entity, PlayerState.Running);
                    vel.m_velocity = vel.m_walkLimit;
                } else {
                    setPlayerState(entity, PlayerState.Walking);
                }
            } else {
                PushComponent push = m_pushComps.get(entity);
                if (touch.m_boxTouch && !push.m_pushLeft) {
                    setPlayerState(entity, PlayerState.Pushing);
                }
                if (touch.m_boxTouch && push.m_pushLeft) {
                    movementComponent.setVelocity(vel.m_walkLimit);
                }
            }
        }
        if (player.isLyingDown() || player.isCrawling()) {
            vel.m_velocity = vel.m_crawlLimit * 2;
            movementComponent.setVelocity(vel.m_velocity);
            setPlayerState(entity, PlayerState.Crawl);
        }
        player.setFacingLeft(false);
    }

    private void choose(Entity entity) {
        if (m_inputMgr.m_playerSelected == InputManager.PlayerSelection.TWO) {
            m_playerComps.get(entity).setActive(true);
        } else if (m_playerComps.get(entity).canDeActivate()) {
            m_playerComps.get(entity).setActive(false);
            m_velComps.get(entity).m_velocity = 0;
        }
        m_inputMgr.reset();
    }

    private boolean canBeControlled(PlayerComponent player) {
        return player.isActive() && !player.isFinishing() && !player.lyingDown() && !player.isGettingUp() && !player.isPullingUp();
    }

    protected void setPlayerState(Entity entity, PlayerState state) {
        m_animComps.get(entity).setAnimationState(state);
        m_playerComps.get(entity).setState(state);
    }

    @Override
    public boolean keyDown(int keycode) {
        m_inputMgr.keyDown(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        m_inputMgr.keyUp(keycode);
        return true;

    }
}
