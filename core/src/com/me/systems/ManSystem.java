package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.Gdx;
import com.me.component.*;
import com.me.config.GameConfig;
import com.me.config.GlobalConfig;
import com.me.events.GameEventType;
import com.me.events.TelegramEvent;
import com.me.events.states.PlayerState;
import com.me.listeners.LevelEventListener;
import com.me.ui.InputManager;

/**
 * Created by hateftadayon on 12/31/15.
 */
public class ManSystem extends PlayerSystem {

    private float VELOCITY = 11.0f;
    private float VELOCITYINR = 3.0f;
    @Mapper
    ComponentMapper<JointComponent> m_jointComp;
    @Mapper
    ComponentMapper<PlayerComponent> m_playerComps;
    @Mapper
    ComponentMapper<PhysicsComponent> m_physComps;
    @Mapper
    ComponentMapper<PlayerAnimationComponent> m_animComps;
    @Mapper
    ComponentMapper<KeyInputComponent> m_movComps;
    @Mapper
    ComponentMapper<TouchComponent> m_touchComps;
    @Mapper
    ComponentMapper<HangComponent> m_hangComps;
    @Mapper
    ComponentMapper<LadderClimbComponent> m_ladderComps;
    @Mapper
    ComponentMapper<VelocityLimitComponent> m_velComps;
    @Mapper
    ComponentMapper<GrabComponent> m_grabComps;
    @Mapper
    ComponentMapper<PushComponent> m_pushComps;
    @Mapper
    ComponentMapper<EventComponent> m_taskComps;
    @Mapper
    ComponentMapper<CharacterMovementComponent> m_movementComps;
    @Mapper
    ComponentMapper<FeetComponent> m_rayCastComps;
    @Mapper
    ComponentMapper<HandHoldComponent> m_handHoldComps;

    @SuppressWarnings("unchecked")
    public ManSystem(LevelEventListener listener) {
        super(Aspect.getAspectForOne(PlayerOneComponent.class));

        m_inputMgr = InputManager.getInstance();
        InputManager.getInstance().addEventListener(listener);

        if (GlobalConfig.getInstance().config.platform == GameConfig.Platform.DESKTOP) {
            Gdx.input.setInputProcessor(this);
        }
    }

    @Override
    protected void process(Entity entity) {
        PlayerComponent player = m_playerComps.get(entity);
        PlayerAnimationComponent animation = m_animComps.get(entity);
        GrabComponent grabComponent = m_grabComps.get(entity);
        TouchComponent touch = m_touchComps.get(entity);
        PhysicsComponent physicsComponent = m_physComps.get(entity);
        CharacterMovementComponent movementComponent = m_movementComps.get(entity);
        KeyInputComponent keyInputComponent = m_movComps.get(entity);

        keyInputComponent.set(m_inputMgr.isDown(left),
                m_inputMgr.isDown(right),
                m_inputMgr.isDown(action),
                m_inputMgr.isDown(down),
                m_inputMgr.isDown(jump));

        animation.setFacing(player.isFacingLeft());

        choose(player);

        if (canBeControlled(player)) {

            if (keyInputComponent.m_left) {
                moveLeft(entity);
            }
            if (keyInputComponent.m_right) {
                moveRight(entity);
            }
            if (keyInputComponent.m_jump) {
                jump(entity);
            }

            if (m_inputMgr.isDown(action)) {
                if (touch.m_footEdge) {
                    if (touch.m_footEdgeL) {
                        player.setFacingLeft(true);
                    }
                    if (touch.m_footEdgeR) {
                        player.setFacingLeft(false);
                    }
                    physicsComponent.warp("feet", touch.m_touchCenter);
                    setPlayerState(entity, PlayerState.LieDown);
                    grabComponent.m_gonnaGrab = true;
                    movementComponent.standStill();
                }
                if (touch.m_pushArea) {
                    EventComponent component = m_taskComps.get(entity);
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
                if (touch.m_handHoldArea) {
                    holdHands(entity);
                }
                if(player.isHanging()){
                    setPlayerState(entity, PlayerState.ClimbingLedge);
                }
            }
        }

        setPlayerState(entity);

        if (isDead(physicsComponent)) {
            m_inputMgr.callRestart();
        }

        animateBody(physicsComponent, player, animation);

    }

    private void setPlayerState(Entity entity) {
        CharacterMovementComponent movementComponent = m_movementComps.get(entity);
        KeyInputComponent keyInput = m_movComps.get(entity);
        PhysicsComponent physicsComponent = m_physComps.get(entity);
        VelocityLimitComponent velocityLimitComponent = m_velComps.get(entity);
        PlayerComponent playerComponent = m_playerComps.get(entity);

        System.out.println(playerComponent.getState());

        if (!keyInput.moved()) {
            movementComponent.standStill();
            velocityLimitComponent.m_velocity = 0;
            if (playerComponent.shouldBeIdle() &&
                    !physicsComponent.isFalling()){
                setPlayerState(entity, PlayerState.Idle);
            }
        }

        if (physicsComponent.isFalling() &&
                !playerComponent.isHanging()) {
            setPlayerState(entity, PlayerState.Falling);
        }

    }

    protected void setPlayerState(Entity entity, PlayerState state) {
        m_animComps.get(entity).setAnimationState(state);
        m_playerComps.get(entity).setState(state);
    }

    private void jump(Entity entity) {
        PlayerComponent player = m_playerComps.get(entity);
        TouchComponent touch = m_touchComps.get(entity);
        PhysicsComponent physicsComponent = m_physComps.get(entity);
        KeyInputComponent keyInputComponent = m_movComps.get(entity);

        if (!player.isJumping()) {
            if (keyInputComponent.isMoving()) {
                if (velocityLimitForJumpBoost(entity)) {
                    physicsComponent.applyLinearImpulse((keyInputComponent.m_left ? -10 : 10) + physicsComponent.getLinearVelocity().x, 60);
                } else {
                    physicsComponent.applyLinearImpulse(physicsComponent.getLinearVelocity().x, 60);
                }
                setPlayerState(entity, PlayerState.Jumping);
            } else if (!touch.m_boxTouch) {
                physicsComponent.applyLinearImpulse(physicsComponent.getLinearVelocity().x, 100);
                setPlayerState(entity, PlayerState.UpJump);
            }
        }
    }

    private void moveLeft(Entity entity) {

        VelocityLimitComponent vel = m_velComps.get(entity);
        PhysicsComponent ps = m_physComps.get(entity);
        CharacterMovementComponent movementComponent = m_movementComps.get(entity);
        TouchComponent touch = m_touchComps.get(entity);
        HangComponent hangComponent = m_hangComps.get(entity);
        PlayerComponent player = m_playerComps.get(entity);
        PushComponent push = m_pushComps.get(entity);
        FeetComponent feetComponent = m_rayCastComps.get(entity);
        JointComponent jointComponent = m_jointComp.get(entity);

        if (vel.m_velocity > 0) {
            vel.m_velocity = -VELOCITYINR;
        }
        if (feetComponent.hasCollided() && !player.isHanging()) {
            if (!touch.m_boxTouch) {
                vel.m_velocity -= VELOCITY * world.delta;
                movementComponent.setVelocity(vel.m_velocity);
                if (movementComponent.getSpeed() < -vel.m_walkLimit) {
                    movementComponent.setVelocity(-vel.m_walkLimit);
                    setPlayerState(entity, PlayerState.Running);
                    vel.m_velocity = -vel.m_walkLimit;
                } else {
                    setPlayerState(entity, PlayerState.Jogging);
                }
            }
            if (touch.m_boxTouch && push.m_pushLeft) {
                movementComponent.setVelocity(-vel.m_pushlimit);
                setPlayerState(entity, PlayerState.Pushing);
            }
            if (touch.m_boxTouch && !push.m_pushLeft) {
                setPlayerState(entity, PlayerState.Walking);
                movementComponent.setVelocity(-vel.m_pushlimit);
            }
            if (!ps.isDynamic()) {
                setPlayerState(entity, PlayerState.Walking);
                ps.makeDynamic();
                ps.setLinearVelocity(-vel.m_walkLimit, ps.getLinearVelocity().y);
                touch.m_ladderTouch = false;
            }
        }
        if(player.isHanging()){
            if(hangComponent.m_hangingRight){
                jointComponent.destroyHangJoint();
                setPlayerState(entity, PlayerState.Falling);
            }
        }
        player.setFacingLeft(true);
    }

    private void moveRight(Entity entity) {

        VelocityLimitComponent vel = m_velComps.get(entity);
        PhysicsComponent ps = m_physComps.get(entity);
        CharacterMovementComponent movementComponent = m_movementComps.get(entity);
        TouchComponent touch = m_touchComps.get(entity);
        HangComponent hangComponent = m_hangComps.get(entity);
        PlayerComponent player = m_playerComps.get(entity);
        PushComponent push = m_pushComps.get(entity);
        FeetComponent feetComponent = m_rayCastComps.get(entity);
        JointComponent jointComponent = m_jointComp.get(entity);

        if (vel.m_velocity < 0) {
            vel.m_velocity = VELOCITYINR;
        }

        if (feetComponent.hasCollided() && !player.isHanging()) {
            if (!touch.m_boxTouch) {
                vel.m_velocity += VELOCITY * world.delta;
                movementComponent.setVelocity(vel.m_velocity);
                if (movementComponent.getSpeed() > vel.m_walkLimit) {
                    movementComponent.setVelocity(vel.m_walkLimit);
                    setPlayerState(entity, PlayerState.Running);
                    vel.m_velocity = vel.m_walkLimit;
                } else {
                    setPlayerState(entity, PlayerState.Jogging);
                }
            }
            if (touch.m_boxTouch && push.m_pushRight) {
                setPlayerState(entity, PlayerState.Pushing);
                movementComponent.setVelocity(vel.m_pushlimit);
            } else if (touch.m_boxTouch && !push.m_pushRight) {
                setPlayerState(entity, PlayerState.Walking);
                movementComponent.setVelocity(vel.m_pushlimit);
            }
            if (!ps.isDynamic()) {
                setPlayerState(entity, PlayerState.Walking);
                ps.makeDynamic();
                ps.setLinearVelocity(vel.m_walkLimit, ps.getLinearVelocity().y);
                touch.m_ladderTouch = false;
            }
        }
        if(player.isHanging()){
            if(hangComponent.m_hangingLeft){
                jointComponent.destroyHangJoint();
                setPlayerState(entity, PlayerState.Falling);
            }
        }
        player.setFacingLeft(false);
    }

    private void holdHands(Entity entity) {
        HandHoldComponent handHoldComponent = m_handHoldComps.get(entity);
        TouchComponent touch = m_touchComps.get(entity);
        PlayerComponent player = m_playerComps.get(entity);

        handHoldComponent.setHoldingHands(true);
        if (touch.m_rightHoldArea) {
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
        if (touch.m_leftHoldArea) {
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
        return player.isActive() && !player.isFinishing();
    }

    private void choose(PlayerComponent player) {
        if (m_inputMgr.m_playerSelected == InputManager.PlayerSelection.ONE) {
            player.setActive(true);
        } else if (player.canDeActivate()) {
            player.setActive(false);
        }
    }

    private boolean velocityLimitForJumpBoost(Entity entity) {

        PhysicsComponent physicsComponent = m_physComps.get(entity);
        KeyInputComponent keyInputComponent = m_movComps.get(entity);
        if (keyInputComponent.m_left && physicsComponent.getLinearVelocity().x < -4) {
            return false;
        } else if (keyInputComponent.m_right && physicsComponent.getLinearVelocity().x > 4) {
            return false;
        }
        return true;
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
