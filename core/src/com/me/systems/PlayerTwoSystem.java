package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.me.component.*;
import com.me.component.PlayerComponent.State;
import com.me.component.AnimationComponent.AnimState;
import com.me.ui.InputManager;
import com.me.ui.InputManager.PlayerSelection;

public class PlayerTwoSystem extends PlayerSystem {

	@Mapper	ComponentMapper<PlayerComponent> m_playerComps;
	@Mapper	ComponentMapper<PlayerAnimationComponent> m_animComps;
	@Mapper	ComponentMapper<MovementComponent> m_movComps;
	@Mapper	ComponentMapper<TouchComponent> m_touchComps;
	@Mapper	ComponentMapper<JumpComponent> m_jumpComps;
	@Mapper	ComponentMapper<GrabComponent> m_grabComps;
	@Mapper	ComponentMapper<PhysicsComponent> m_physComps;
	@Mapper	ComponentMapper<VelocityLimitComponent> m_velComps;
	@Mapper	ComponentMapper<CrawlComponent> m_crawlComps;
	@Mapper	ComponentMapper<PushComponent> m_pushComps;
    @Mapper ComponentMapper<EventComponent> m_eventComps;
    @Mapper ComponentMapper<CharacterMovementComponent> m_movementComps;
    @Mapper ComponentMapper<FeetComponent> m_feetComps;

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
		CrawlComponent crawlComp = m_crawlComps.get(entity);
        CharacterMovementComponent movementComponent = m_movementComps.get(entity);
		boolean finish = player.isFinishing();
		if (m_inputMgr.m_playerSelected == PlayerSelection.TWO) {
			ps.makeDynamic("center", 0.001f);
			player.setActive(true);
		} else if (player.canDeActivate() && player.getState() != State.WAITTILDONE) {
			player.setActive(false);
        }

        if (!player.isActive() && !finish) {
            if (!g.m_gettingLifted && feet.hasCollided() && !crawlComp.isCrawling) {
                animation.setAnimationState(AnimState.IDLE);
                movementComponent.standStill();
                if (!touch.m_feetToBox) {
                    ps.makeStatic("center");
                }
            }
        }

        if (crawlComp.isStanding && animation.isCompleted(AnimState.STANDUP)) {
            crawlComp.isStanding = false;
            player.setState(State.IDLE);
        }

        MovementComponent m = m_movComps.get(entity);
        m.set(m_inputMgr.isDown(left), m_inputMgr.isDown(right),
				m_inputMgr.isDown(up), m_inputMgr.isDown(down),
				m_inputMgr.isDown(jump));
        if (player.isActive() && !m.m_lockControls && !g.m_gettingLifted
				&& !finish && !crawlComp.isStanding && player.getState() != State.WAITTILDONE) {

            if (feet.hasCollided() && !crawlComp.isCrawling) {
                animation.setupPose();
            }

            if (isIdle(entity)) {
                animation.setAnimationState(AnimState.IDLE);
                m_velComps.get(entity).m_velocity = 0;
                movementComponent.standStill();
            }

            if (m.m_left && feet.hasCollided()) {
                if (crawlComp.isCrawling) {
                    crawlLeft(entity);
                } else {
                    walkLeft(entity);
                }
                player.setFacingLeft(true);
                jumpComponent.m_jumped = false;
            }

            if (m.m_right && feet.hasCollided()) {
                if (crawlComp.isCrawling) {
                    crawlRight(entity);
                } else {
                    walkRight(entity);
                }
                player.setFacingLeft(false);
                jumpComponent.m_jumped = false;
            }
			if (m.m_jump && canJump(entity)) {
                jumpComponent.m_jumped = true;
				animation.setAnimationState(AnimState.UPJUMP);
				player.setState(State.JUMPING);
			}

			if (jumpComponent.m_jumped) {
                if(animation.getState()== AnimState.UPJUMP && animation.shouldJump()){
					ps.setLinearVelocity(ps.getLinearVelocity().x,	m_velComps.get(entity).m_jumpLimit * 2.5f);
                    animation.getEvent().resetEvent();
				}
			}
            if(animation.isCompleted(AnimState.UPJUMP)){
                jumpComponent.m_jumped = false;
                animation.setAnimationState(AnimState.IDLE);
            }

			if (m_inputMgr.isDown(action)) {
				if (crawlComp.canCrawl && !crawlComp.isCrawling) {
					animation.setAnimationState(AnimState.LIEDOWN);
					player.setState(State.LYINGDOWN);
				}
				if(touch.m_pushArea){
                    EventComponent component = m_eventComps.get(entity);
					if(touch.m_leftPushArea){
						player.setFacingLeft(false);
						animation.setAnimationState(AnimState.PRESSBUTTON);
						player.setState(State.WAITTILDONE);
                        component.getEventInfo().notify(this);
                    }
					if(touch.m_rightPushArea){
						player.setFacingLeft(true);
						animation.setAnimationState(AnimState.PRESSBUTTON);
						player.setState(State.WAITTILDONE);
                        component.getEventInfo().notify(this);
                    }
				}
			}

			if (animation.isCompleted(AnimState.LIEDOWN)) {
				animation.setAnimationState(AnimState.LYINGDOWN);
				crawlComp.isCrawling = true;
				ps.disableBody("center");
			}

			if (player.getState().equals(State.CRAWLING) && !m.isMoving()) {
				animation.setAnimationState(AnimState.LYINGDOWN);
                movementComponent.standStill();
			}

			if (!crawlComp.canCrawl && crawlComp.isCrawling) {
				animation.setAnimationState(AnimState.STANDUP);
				crawlComp.isCrawling = false;
				crawlComp.isStanding = true;
				ps.enableBody("center");
                movementComponent.standStill();
			}
		}
		
		if(animation.isCompleted() && player.getState() == State.WAITTILDONE){
			player.setState(State.IDLE);
		}

		if (finish) {
            if (animation.isCompleted()) {
                player.setIsFinishedAnimating(true);
            }
		}

		if (ps.isFalling() && ps.isMovingForward() && !feet.hasCollided()) {
			animation.setAnimationState(AnimState.FALLING);
		}

		if (isDead(ps)) {
			m_inputMgr.callRestart();
		}

		animateBody(ps, player, animation);
		animation.setFacing(player.isFacingLeft());

	}

    private boolean canJump(Entity entity){
        FeetComponent feet = m_feetComps.get(entity);
        MovementComponent m = m_movComps.get(entity);
        CrawlComponent crawlComp = m_crawlComps.get(entity);
        JumpComponent jumpComponent = m_jumpComps.get(entity);

        return feet.hasCollided() && !m.isMoving() && !crawlComp.isCrawling && !jumpComponent.m_jumped;
    }

	private boolean isIdle(Entity e) {

		PlayerComponent player = m_playerComps.get(e);
		AnimationComponent animation = m_animComps.get(e);
		MovementComponent m = m_movComps.get(e);
        FeetComponent feetComponent = m_feetComps.get(e);

		if (!m.m_left && !m.m_right && feetComponent.hasCollided()
				&& !player.getState().equals(State.JUMPING)
				&& !animation.getAnimationState().equals(AnimState.PULLUP)
				&& !player.getState().equals(State.LYINGDOWN)
				&& !player.getState().equals(State.CRAWLING)) {
			player.setState(State.IDLE);
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
		animation.setAnimationState(AnimState.CRAWL);
		player.setState(State.CRAWLING);
	}

	private void crawlRight(Entity e) {
        CharacterMovementComponent movementComponent = m_movementComps.get(e);
		PlayerComponent player = m_playerComps.get(e);
		AnimationComponent animation = m_animComps.get(e);
		VelocityLimitComponent vel = m_velComps.get(e);

		vel.m_velocity = vel.m_crawlLimit * 2;
        movementComponent.setVelocity(vel.m_velocity);
		animation.setAnimationState(AnimState.CRAWL);
		player.setState(State.CRAWLING);
	}

	private void walkLeft(Entity e) {

		PlayerComponent player = m_playerComps.get(e);
        CharacterMovementComponent movementComponent = m_movementComps.get(e);
		AnimationComponent animation = m_animComps.get(e);
		VelocityLimitComponent vel = m_velComps.get(e);
		TouchComponent touch = m_touchComps.get(e);
		if(!touch.m_boxTouch){
			if (vel.m_velocity > 0) {
				vel.m_velocity = 0;
			}
			vel.m_velocity -= VELOCITY * world.delta;
            movementComponent.setVelocity(vel.m_velocity);
			if (movementComponent.getSpeed() < -vel.m_walkLimit) {
                movementComponent.setVelocity(-vel.m_walkLimit);
				animation.setAnimationState(AnimState.RUNNING);
				vel.m_velocity = -vel.m_walkLimit;
			} else {
				animation.setAnimationState(AnimState.WALKING);
			}
		} else{
			PushComponent push = m_pushComps.get(e);
			if(touch.m_boxTouch && push.m_pushLeft){
				animation.setAnimationState(AnimState.PUSHING);
			}
			if(touch.m_boxTouch && !push.m_pushLeft){
                movementComponent.setVelocity(-vel.m_walkLimit);
			}
		}
		player.setState(State.WALKING);

	}

	private void walkRight(Entity e) {

        CharacterMovementComponent movementComponent = m_movementComps.get(e);
		PlayerComponent player = m_playerComps.get(e);
		AnimationComponent animation = m_animComps.get(e);
		VelocityLimitComponent vel = m_velComps.get(e);
		TouchComponent touch = m_touchComps.get(e);
		
		if(!touch.m_boxTouch){
			if (vel.m_velocity < 0) {
				vel.m_velocity = 0;
			}
	
			vel.m_velocity += VELOCITY * world.delta;
            movementComponent.setVelocity(vel.m_velocity);
			if (movementComponent.getSpeed() > vel.m_walkLimit) {
                movementComponent.setVelocity(vel.m_walkLimit);
				animation.setAnimationState(AnimState.RUNNING);
				vel.m_velocity = vel.m_walkLimit;
			} else {
				animation.setAnimationState(AnimState.WALKING);
			}
		} else{
			PushComponent push = m_pushComps.get(e);
			if(touch.m_boxTouch && !push.m_pushLeft){
				animation.setAnimationState(AnimState.PUSHING);
			}
			if(touch.m_boxTouch && push.m_pushLeft){
                movementComponent.setVelocity(vel.m_walkLimit);
			}
		}
		player.setState(State.WALKING);

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
