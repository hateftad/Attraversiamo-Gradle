package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.me.component.*;
import com.me.events.GameEventType;
import com.me.events.TelegramEvent;
import com.me.events.states.PlayerState;
import com.me.ui.InputManager;
import com.me.ui.InputManager.PlayerSelection;

public class PlayerTwoSystem extends PlayerSystem {

    @Mapper
    ComponentMapper<PlayerComponent> m_playerComps;
    @Mapper
    ComponentMapper<PlayerAnimationComponent> m_animComps;
    @Mapper
    ComponentMapper<KeyInputComponent> m_movComps;
    @Mapper
    ComponentMapper<TouchComponent> m_touchComps;
    @Mapper
    ComponentMapper<JumpComponent> m_jumpComps;
    @Mapper
    ComponentMapper<GrabComponent> m_grabComps;
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
    @Mapper
    ComponentMapper<HandHoldComponent> m_handHoldComps;

    private float VELOCITY = 5.5f;

    @SuppressWarnings("unchecked")
    public PlayerTwoSystem() {
        super(Aspect.getAspectForOne(PlayerTwoComponent.class));
        m_inputMgr = InputManager.getInstance();
    }

    @Override
    protected void process(Entity entity) {

        PhysicsComponent ps = m_physComps.get(entity);
        PlayerComponent player = m_playerComps.get(entity);
        JumpComponent jumpComponent = m_jumpComps.get(entity);
        PlayerAnimationComponent animation = m_animComps.get(entity);
        GrabComponent g = m_grabComps.get(entity);
        TouchComponent touch = m_touchComps.get(entity);
        FeetComponent feet = m_feetComps.get(entity);
        CharacterMovementComponent movementComponent = m_movementComps.get(entity);
        HandHoldComponent handHoldComponent = m_handHoldComps.get(entity);

        KeyInputComponent movementComp = m_movComps.get(entity);
        movementComp.set(m_inputMgr.isDown(left),
                m_inputMgr.isDown(right),
                m_inputMgr.isDown(up),
                m_inputMgr.isDown(down),
                m_inputMgr.isDown(jump));

        boolean finish = player.isFinishing();
        if (m_inputMgr.m_playerSelected == PlayerSelection.TWO) {
            ps.makeDynamic("center", 0.001f);
            player.setActive(true);
        } else if (player.canDeActivate() && player.getState() != PlayerState.WaitTilDone) {
            player.setActive(false);
        }

        if (!player.isActive() && !finish && !handHoldComponent.isHoldingHands()) {
//            if (crawlComp.isStanding && animation.isCompleted(PlayerState.StandUp)) {
//                crawlComp.isStanding = false;
//                player.setState(PlayerState.Idle);
//            }
            if (!g.m_gettingLifted && feet.hasCollided()) {
                animation.setAnimationState(PlayerState.Idle);
                movementComponent.standStill();
//                if (!touch.m_feetToBox) {
//                    ps.makeStatic("center");
//                }
            }
        }


        if (player.isActive() && !movementComp.m_lockControls && !g.m_gettingLifted
                && !finish && player.getState() != PlayerState.WaitTilDone) {

            if (feet.hasCollided()) {
                animation.setupPose();
            }

            if (isIdle(entity)) {
                animation.setAnimationState(PlayerState.Idle);
                m_velComps.get(entity).m_velocity = 0;
                movementComponent.standStill();
            }

            if (movementComp.m_left && feet.hasCollided()) {
//                if (crawlComp.isCrawling) {
//                    crawlLeft(entity);
//                } else {
//                    walkLeft(entity);
//                }
                player.setFacingLeft(true);
                jumpComponent.m_jumped = false;
            }

            if (movementComp.m_right && feet.hasCollided()) {
//                if (crawlComp.isCrawling) {
//                    crawlRight(entity);
//                } else {
//                    walkRight(entity);
//                }
                player.setFacingLeft(false);
                jumpComponent.m_jumped = false;
            }
            if (movementComp.m_jump && canJump(entity)) {
                jumpComponent.m_jumped = true;
                animation.setAnimationState(PlayerState.UpJump);
                player.setState(PlayerState.Jumping);
            }

            if (jumpComponent.m_jumped) {
                if (animation.getState() == PlayerState.UpJump && animation.shouldJump()) {
                    ps.setLinearVelocity(ps.getLinearVelocity().x, m_velComps.get(entity).m_jumpLimit * 2.5f);
                    animation.getEvent().resetEvent();
                }
            }
            if (animation.isCompleted(PlayerState.UpJump)) {
                jumpComponent.m_jumped = false;
                animation.setAnimationState(PlayerState.Idle);
            }

            if (m_inputMgr.isDown(action)) {
//                if (crawlComp.m_canCrawl && !crawlComp.isCrawling) {
//                    animation.setAnimationState(PlayerState.LieDown);
//                    player.setState(PlayerState.LyingDown);
//                }
                if (touch.m_pushArea) {
                    EventComponent component = m_eventComps.get(entity);
                    if (touch.m_leftPushArea) {
                        player.setFacingLeft(false);
                        animation.setAnimationState(PlayerState.PressButton);
                        player.setState(PlayerState.WaitTilDone);
                        component.getEventInfo().notify(this, player.getPlayerNr());
                    }
                    if (touch.m_rightPushArea) {
                        player.setFacingLeft(true);
                        animation.setAnimationState(PlayerState.PressButton);
                        player.setState(PlayerState.WaitTilDone);
                        component.getEventInfo().notify(this, player.getPlayerNr());
                    }
                    movementComponent.standStill();
                }
                if (touch.m_handHoldArea) {
                    handHoldComponent.setHoldingHands(true);
                    if (touch.m_rightHoldArea) {
                        TelegramEvent telegramEvent;
                        if(player.isFacingLeft()){
                            animation.setAnimationState(PlayerState.HoldHandLeading);
                            handHoldComponent.setIsLeading(true);
                            telegramEvent = new TelegramEvent(GameEventType.HoldingHandsLeading);
                        } else {
                            animation.setAnimationState(PlayerState.HoldHandFollowing);
                            handHoldComponent.setIsLeading(false);
                            telegramEvent = new TelegramEvent(GameEventType.HoldingHandsFollowing);
                        }
                        telegramEvent.notify(this, entity);
                        //player.setState(State.WAITTILDONE);
                    }
                    if (touch.m_leftHoldArea) {
                        TelegramEvent telegramEvent;
                        if(player.isFacingLeft()){
                            animation.setAnimationState(PlayerState.HoldHandFollowing);
                            handHoldComponent.setIsLeading(true);
                            telegramEvent = new TelegramEvent(GameEventType.HoldingHandsFollowing);
                        } else {
                            animation.setAnimationState(PlayerState.HoldHandLeading);
                            handHoldComponent.setIsLeading(false);
                            telegramEvent = new TelegramEvent(GameEventType.HoldingHandsLeading);
                        }
                        telegramEvent.notify(this, entity);
                        //player.setState(State.WAITTILDONE);
                    }
                    movementComponent.standStill();
                }
            }

            if (animation.isCompleted(PlayerState.LieDown)) {
                animation.setAnimationState(PlayerState.LyingDown);
                player.setState(PlayerState.WaitTilDone);
//                crawlComp.isCrawling = true;
                ps.disableBody("center");
            }

            if (player.getState().equals(PlayerState.Crawl) && !movementComp.isMoving()) {
                animation.setAnimationState(PlayerState.LyingDown);
                movementComponent.standStill();
            }

//            if (!crawlComp.m_canCrawl && crawlComp.isCrawling) {
//                animation.setAnimationState(PlayerState.StandUp);
//                crawlComp.isCrawling = false;
//                crawlComp.isStanding = true;
//                ps.enableBody("center");
//                movementComponent.standStill();
//            }
        }

        if (animation.isCompleted() && player.getState() == PlayerState.WaitTilDone) {
            player.setState(PlayerState.Idle);
        }

        if (finish) {
            if (animation.isCompleted()) {
                player.setIsFinishedAnimating(true);
            }
        }

        if (ps.isFalling() && ps.isMovingForward() && !feet.hasCollided()) {
            animation.setAnimationState(PlayerState.Falling);
        }

        if (isDead(ps)) {
            m_inputMgr.callRestart();
        }

        animateBody(ps, player, animation);
        animation.setFacing(player.isFacingLeft());

    }

    private boolean canJump(Entity entity) {
        FeetComponent feet = m_feetComps.get(entity);
        KeyInputComponent m = m_movComps.get(entity);
        JumpComponent jumpComponent = m_jumpComps.get(entity);

        return feet.hasCollided() && !m.isMoving() && !jumpComponent.m_jumped;
    }

    private boolean isIdle(Entity e) {

        PlayerComponent player = m_playerComps.get(e);
        AnimationComponent animation = m_animComps.get(e);
        KeyInputComponent m = m_movComps.get(e);
        FeetComponent feetComponent = m_feetComps.get(e);

        if (!m.m_left && !m.m_right && feetComponent.hasCollided()
                && !player.getState().equals(PlayerState.Jumping)
                && !animation.getAnimationState().equals(PlayerState.PullUp)
                && !animation.getAnimationState().equals(PlayerState.HoldHandFollowing)
                && !animation.getAnimationState().equals(PlayerState.HoldHandLeading)
                && !player.getState().equals(PlayerState.LyingDown)
                && !player.getState().equals(PlayerState.Crawl)) {
            player.setState(PlayerState.Idle);
            return true;
        }
        return false;
    }

    private void crawlLeft(Entity e) {
        CharacterMovementComponent movementComponent = m_movementComps.get(e);
        PlayerComponent player = m_playerComps.get(e);
        AnimationComponent animation = m_animComps.get(e);
        VelocityLimitComponent vel = m_velComps.get(e);

        vel.m_velocity = -vel.m_crawlLimit * 2;
        movementComponent.setVelocity(vel.m_velocity);
        animation.setAnimationState(PlayerState.Crawl);
        player.setState(PlayerState.Crawl);
    }

    private void crawlRight(Entity e) {
        CharacterMovementComponent movementComponent = m_movementComps.get(e);
        PlayerComponent player = m_playerComps.get(e);
        AnimationComponent animation = m_animComps.get(e);
        VelocityLimitComponent vel = m_velComps.get(e);

        vel.m_velocity = vel.m_crawlLimit * 2;
        movementComponent.setVelocity(vel.m_velocity);
        animation.setAnimationState(PlayerState.Crawl);
        player.setState(PlayerState.Crawl);
    }

    private void walkLeft(Entity e) {

        PlayerComponent player = m_playerComps.get(e);
        CharacterMovementComponent movementComponent = m_movementComps.get(e);
        AnimationComponent animation = m_animComps.get(e);
        VelocityLimitComponent vel = m_velComps.get(e);
        TouchComponent touch = m_touchComps.get(e);
        if (!touch.m_boxTouch) {
            if (vel.m_velocity > 0) {
                vel.m_velocity = 0;
            }
            vel.m_velocity -= VELOCITY * world.delta;
            movementComponent.setVelocity(vel.m_velocity);
            if (movementComponent.getSpeed() < -vel.m_walkLimit) {
                movementComponent.setVelocity(-vel.m_walkLimit);
                animation.setAnimationState(PlayerState.Running);
                vel.m_velocity = -vel.m_walkLimit;
            } else {
                animation.setAnimationState(PlayerState.Walking);
            }
        } else {
            PushComponent push = m_pushComps.get(e);
            if (touch.m_boxTouch && push.m_pushLeft) {
                animation.setAnimationState(PlayerState.Pushing);
            }
            if (touch.m_boxTouch && !push.m_pushLeft) {
                movementComponent.setVelocity(-vel.m_walkLimit);
            }
        }
        player.setState(PlayerState.Walking);

    }

    private void walkRight(Entity e) {

        CharacterMovementComponent movementComponent = m_movementComps.get(e);
        PlayerComponent player = m_playerComps.get(e);
        AnimationComponent animation = m_animComps.get(e);
        VelocityLimitComponent vel = m_velComps.get(e);
        TouchComponent touch = m_touchComps.get(e);

        if (!touch.m_boxTouch) {
            if (vel.m_velocity < 0) {
                vel.m_velocity = 0;
            }

            vel.m_velocity += VELOCITY * world.delta;
            movementComponent.setVelocity(vel.m_velocity);
            if (movementComponent.getSpeed() > vel.m_walkLimit) {
                movementComponent.setVelocity(vel.m_walkLimit);
                animation.setAnimationState(PlayerState.Running);
                vel.m_velocity = vel.m_walkLimit;
            } else {
                animation.setAnimationState(PlayerState.Walking);
            }
        } else {
            PushComponent push = m_pushComps.get(e);
            if (touch.m_boxTouch && !push.m_pushLeft) {
                animation.setAnimationState(PlayerState.Pushing);
            }
            if (touch.m_boxTouch && push.m_pushLeft) {
                movementComponent.setVelocity(vel.m_walkLimit);
            }
        }
        player.setState(PlayerState.Walking);

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
