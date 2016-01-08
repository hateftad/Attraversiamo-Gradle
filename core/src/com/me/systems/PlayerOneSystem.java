package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.component.*;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;
import com.me.events.TelegramEvent;
import com.me.events.states.PlayerState;
import com.me.listeners.LevelEventListener;
import com.me.ui.InputManager;
import com.me.ui.InputManager.PlayerSelection;
import com.me.config.GameConfig.Platform;
import com.me.config.GlobalConfig;

public class PlayerOneSystem extends PlayerSystem {

    private float VELOCITY = 11.0f;
    private float VELOCITYINR = 3.0f;
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
    ComponentMapper<JumpComponent> m_jumpComps;
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
    public Vector2 currentPosition;

    @SuppressWarnings("unchecked")
    public PlayerOneSystem(LevelEventListener listener) {
        super(Aspect.getAspectForOne(PlayerOneComponent.class));

        m_inputMgr = InputManager.getInstance();
        InputManager.getInstance().addEventListener(listener);

        if (GlobalConfig.getInstance().config.platform == Platform.DESKTOP) {
            Gdx.input.setInputProcessor(this);
        }
        currentPosition = Vector2.Zero;
    }

    @Override
    protected void process(Entity entity) {

        LadderClimbComponent ladderClimbComponent = m_ladderComps.get(entity);
        HangComponent hangComponent = m_hangComps.get(entity);
        PlayerComponent player = m_playerComps.get(entity);
        PlayerAnimationComponent animation = m_animComps.get(entity);
        GrabComponent grabComponent = m_grabComps.get(entity);
        TouchComponent touch = m_touchComps.get(entity);
        PhysicsComponent physicsComponent = m_physComps.get(entity);
        CharacterMovementComponent movementComponent = m_movementComps.get(entity);
        FeetComponent feetComponent = m_rayCastComps.get(entity);
        JumpComponent jumpComponent = m_jumpComps.get(entity);
        HandHoldComponent handHoldComponent = m_handHoldComps.get(entity);
        VelocityLimitComponent vel = m_velComps.get(entity);
        KeyInputComponent m = m_movComps.get(entity);
        m.set(m_inputMgr.isDown(left),
                m_inputMgr.isDown(right),
                m_inputMgr.isDown(action),
                m_inputMgr.isDown(down),
                m_inputMgr.isDown(jump));

        boolean finish = player.isFinishing();
        currentPosition = physicsComponent.getPosition();

        if (m_inputMgr.isDown(skinChange)) {
            animation.setSkin(m_inputMgr.toggleSkins());
        }

//        if (m_inputMgr.m_playerSelected == PlayerSelection.ONE) {
//            player.setActive(true);
//        } else if (player.canDeActivate() && player.getState() != PlayerState.WaitTilDone) {
//            player.setActive(false);
//        }

//        if (!player.isActive() && !finish) {
//            if (!grabComponent.m_gonnaGrab &&
//                    !hangComponent.m_isHanging &&
//                    !grabComponent.m_lifting &&
//                    !handHoldComponent.isHoldingHands()) {
//                animation.setAnimationState(PlayerState.Idle);
//                movementComponent.standStill();
//            }
//        }
//
//
//        if (player.isActive() && !m.m_lockControls && !grabComponent.m_lifting && !finish && player.getState() != PlayerState.WaitTilDone) {
//            if (feetComponent.hasCollided() && !touch.m_boxTouch && !touch.m_footEdge) {
//                animation.setupPose();
//            }
//            if (!m.isMoving() && feetComponent.hasCollided() && !touch.m_ladderTouch) {
//                vel.m_velocity = 0;
//                if (!grabComponent.m_gonnaGrab) {
//                    if (!animation.getState().equals(PlayerState.PullUp) && !animation.getState().equals(PlayerState.HoldHandLeading)) {
//                        animation.setAnimationState(PlayerState.Idle);
//                    }
//                    movementComponent.standStill();
//                }
//            }
//            if (m_hangComps.has(entity)) {
//
//                if (m.m_up && hangComponent.m_isHanging) {
//                    animation.setAnimationState(PlayerState.ClimbingLedge);
//                    hangComponent.m_climbingUp = true;
//                    m.m_lockControls = true;
//                }
//                if (hangComponent.m_isHanging && !hangComponent.m_climbingUp) {
//                    animation.setAnimationState(PlayerState.Hanging);
//                }
//            }
//            if (m_ladderComps.has(entity)) {
//                if (touch.m_ladderTouch) {
//                    climbLadder(entity);
//                }
//            }
//            if (m.m_left) {
//                moveLeft(entity);
//            }
//            if (m.m_right) {
//                moveRight(entity);
//            }
//
//            if (m_jumpComps.has(entity)) {
//                if (m.m_jump && feetComponent.hasCollided() && !jumpComponent.m_jumped) {
//                    if (m.isMoving()) {
//                        animation.setAnimationState(PlayerState.Jumping);
//                        if (velocityLimitForJumpBoost(entity)) {
//                            physicsComponent.applyLinearImpulse((m.m_left ? -10 : 10) + physicsComponent.getLinearVelocity().x, 60);
//                        } else {
//                            physicsComponent.applyLinearImpulse(physicsComponent.getLinearVelocity().x, 60);
//                        }
//                    } else if (!touch.m_boxTouch) {
//                        physicsComponent.applyLinearImpulse(physicsComponent.getLinearVelocity().x, 25);
//                        animation.setAnimationState(PlayerState.UpJump);
//                    }
//                    player.setState(PlayerState.Jumping);
//                }
//            }
//            if (m_inputMgr.isDown(action)) {
//                if (touch.m_footEdge) {
//                    if (touch.m_footEdgeL) {
//                        player.setFacingLeft(true);
//                    }
//                    if (touch.m_footEdgeR) {
//                        player.setFacingLeft(false);
//                    }
//                    physicsComponent.warp("feet", touch.m_touchCenter);
//                    animation.setAnimationState(PlayerState.LieDown);
//                    grabComponent.m_gonnaGrab = true;
//                    movementComponent.standStill();
//                }
//                if (touch.m_ladderTouch) {
//                    vel.m_ladderClimbVelocity = 3;
//                    ladderClimbComponent.m_goingUp = true;
//                    movementComponent.standStill();
//                }
//                if (touch.m_pushArea) {
//                    EventComponent component = m_taskComps.get(entity);
//                    if (touch.m_leftPushArea) {
//                        player.setFacingLeft(false);
//                        animation.setAnimationState(PlayerState.PressButton);
//                        player.setState(PlayerState.WaitTilDone);
//                        component.getEventInfo().notify(this, player.getPlayerNr());
//                    }
//                    if (touch.m_rightPushArea) {
//                        player.setFacingLeft(true);
//                        animation.setAnimationState(PlayerState.PressButton);
//                        player.setState(PlayerState.WaitTilDone);
//                        component.getEventInfo().notify(this, player.getPlayerNr());
//                    }
//                    movementComponent.standStill();
//                }
//                if (touch.m_handHoldArea) {
//                    handHoldComponent.setHoldingHands(true);
//                    if (touch.m_rightHoldArea) {
//                        TelegramEvent telegramEvent;
//                        if(player.isFacingLeft()){
//                            animation.setAnimationState(PlayerState.HoldHandLeading);
//                            handHoldComponent.setIsLeading(true);
//                            telegramEvent = new TelegramEvent(GameEventType.HoldingHandsLeading);
//                        } else {
//                            animation.setAnimationState(PlayerState.HoldHandFollowing);
//                            handHoldComponent.setIsLeading(false);
//                            telegramEvent = new TelegramEvent(GameEventType.HoldingHandsFollowing);
//                        }
//                        telegramEvent.notify(this, entity);
//                    }
//                    if (touch.m_leftHoldArea) {
//                        TelegramEvent telegramEvent;
//                        if(player.isFacingLeft()){
//                            animation.setAnimationState(PlayerState.HoldHandFollowing);
//                            handHoldComponent.setIsLeading(false);
//                            telegramEvent = new TelegramEvent(GameEventType.HoldingHandsFollowing);
//                        } else {
//                            animation.setAnimationState(PlayerState.HoldHandLeading);
//                            handHoldComponent.setIsLeading(true);
//                            telegramEvent = new TelegramEvent(GameEventType.HoldingHandsLeading);
//                        }
//                        telegramEvent.notify(this, entity);
//                    }
//                }
//            }
//
//            if (grabComponent.m_gonnaGrab) {
//                physicsComponent.warp("feet", touch.m_touchCenter);
//            }
//            if (m.moved()) {
//                grabComponent.m_gonnaGrab = false;
//            }
//        }
//
//        if (animation.isCompleted() && player.getState() == PlayerState.WaitTilDone) {
//            player.setState(PlayerState.Idle);
//        }
//
//        if (finish) {
//            if (animation.isCompleted()) {
//                notifyObservers(new TaskEvent(GameEventType.LevelFinished));
//                player.setIsFinishedAnimating(true);
//            }
//        }
//
//        if (physicsComponent.isFalling() && physicsComponent.isMovingForward()) {
//            if (!feetComponent.hasCollided()) {
//                animation.setAnimationState(PlayerState.Falling);
//            }
//        }
//
//        if (physicsComponent.isSubmerged()) {
//            animation.setAnimationState(PlayerState.Idle);
//        }
//
//        if (isDead(physicsComponent)) {
//            // world should call restart
//            m_inputMgr.callRestart();
//        }
//
//        // TOO MUCH PROCESSING!!
//        animateBody(physicsComponent, player, animation);
//
//        animation.setFacing(player.isFacingLeft());

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

    private void moveLeft(Entity e) {

        AnimationComponent animation = m_animComps.get(e);
        VelocityLimitComponent vel = m_velComps.get(e);
        PhysicsComponent ps = m_physComps.get(e);
        FeetComponent feetComponent = m_rayCastComps.get(e);
        CharacterMovementComponent movementComponent = m_movementComps.get(e);
        TouchComponent touch = m_touchComps.get(e);
        LadderClimbComponent ladderComp = m_ladderComps.get(e);
        HangComponent h = m_hangComps.get(e);
        PlayerComponent player = m_playerComps.get(e);
        if (h.m_hangingLeft || h.m_hangingRight) {
            vel.m_velocity = 0;
        }
        if (vel.m_velocity > 0)
            vel.m_velocity = -VELOCITYINR;
        if (feetComponent.hasCollided()) {
            if (m_hangComps.has(e)) {
                PushComponent push = m_pushComps.get(e);
                if (m_ladderComps.has(e) && !h.m_isHanging) {
                    if (!ladderComp.m_leftClimb && !touch.m_boxTouch) {
                        vel.m_velocity -= VELOCITY * world.delta;
                        movementComponent.setVelocity(vel.m_velocity);
                        if (movementComponent.getSpeed() < -vel.m_walkLimit) {
                            movementComponent.setVelocity(-vel.m_walkLimit);
                            animation.setAnimationState(PlayerState.Running);
                            vel.m_velocity = -vel.m_walkLimit;
                        } else {
                            animation.setAnimationState(PlayerState.Jogging);
                        }
                    }
                    if (touch.m_boxTouch && push.m_pushLeft) {
                        movementComponent.setVelocity(-vel.m_pushlimit);
                        animation.setAnimationState(PlayerState.Pushing);
                    }
                    if (touch.m_boxTouch && !push.m_pushLeft) {
                        animation.setAnimationState(PlayerState.Walking);
                        movementComponent.setVelocity(-vel.m_pushlimit);
                    }
                    if (ladderComp.m_rightClimb && !ps.isDynamic()) {
                        animation.setAnimationState(PlayerState.Walking);
                        ps.makeDynamic();
                        ps.setLinearVelocity(-vel.m_walkLimit, ps.getLinearVelocity().y);
                        touch.m_ladderTouch = false;
                        ladderComp.m_rightClimb = false;
                    }
                }
            }
        }
        player.setFacingLeft(true);
        player.setState(PlayerState.Walking);
    }

    private void moveRight(Entity e) {

        AnimationComponent animation = m_animComps.get(e);
        VelocityLimitComponent vel = m_velComps.get(e);
        PhysicsComponent ps = m_physComps.get(e);
        CharacterMovementComponent movementComponent = m_movementComps.get(e);
        FeetComponent feetComponent = m_rayCastComps.get(e);
        TouchComponent touch = m_touchComps.get(e);
        LadderClimbComponent l = m_ladderComps.get(e);
        HangComponent h = m_hangComps.get(e);
        PlayerComponent player = m_playerComps.get(e);

        if (vel.m_velocity < 0)
            vel.m_velocity = VELOCITYINR;
        if (feetComponent.hasCollided()) {
            if (m_hangComps.has(e)) {
                PushComponent push = m_pushComps.get(e);
                if (m_ladderComps.has(e) && !h.m_isHanging) {
                    if (!l.m_rightClimb && !touch.m_boxTouch) {
                        vel.m_velocity += VELOCITY * world.delta;
                        movementComponent.setVelocity(vel.m_velocity);
                        if (movementComponent.getSpeed() > vel.m_walkLimit) {
                            movementComponent.setVelocity(vel.m_walkLimit);
                            animation.setAnimationState(PlayerState.Running);
                            vel.m_velocity = vel.m_walkLimit;
                        } else {
                            animation.setAnimationState(PlayerState.Jogging);
                        }
                    }
                    if (touch.m_boxTouch && push.m_pushRight) {
                        animation.setAnimationState(PlayerState.Pushing);
                        movementComponent.setVelocity(vel.m_pushlimit);
                    } else if (touch.m_boxTouch && !push.m_pushRight) {
                        animation.setAnimationState(PlayerState.Walking);
                        movementComponent.setVelocity(vel.m_pushlimit);
                    }
                    if (l.m_leftClimb && !ps.isDynamic()) {
                        animation.setAnimationState(PlayerState.Walking);
                        ps.makeDynamic();
                        ps.setLinearVelocity(vel.m_walkLimit, ps.getLinearVelocity().y);
                        touch.m_ladderTouch = false;
                        l.m_leftClimb = false;
                    }
                }
            }
        }
        player.setFacingLeft(false);
        player.setState(PlayerState.Walking);
    }

    private void climbLadder(Entity e) {

        KeyInputComponent m = m_movComps.get(e);
        LadderClimbComponent l = m_ladderComps.get(e);
        AnimationComponent animation = m_animComps.get(e);
        VelocityLimitComponent vel = m_velComps.get(e);
        PhysicsComponent ps = m_physComps.get(e);

        ps.setLinearVelocity(0, vel.m_ladderClimbVelocity);
        if (l.m_topLadder && l.m_goingUp) {
            if (l.m_leftClimb)
                ps.setLinearVelocity(3, 0);
            else if (l.m_rightClimb) {
                ps.setLinearVelocity(-3, 0);
            }
        } else if (l.m_bottomLadder && m.m_down) {
            ps.setLinearVelocity(0, 0);
            animation.setAnimationState(PlayerState.LadderHang);
        }
        if (vel.m_ladderClimbVelocity > 0)
            animation.setAnimationState(PlayerState.LadderClimbUp);
        if (vel.m_ladderClimbVelocity < -1)
            animation.setAnimationState(PlayerState.LadderClimbDown);
        if (vel.m_ladderClimbVelocity == 0) {
            animation.setAnimationState(PlayerState.LadderHang);
        }

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


    @Override
    protected void setPlayerState(Entity entity, PlayerState state) {

    }
}
