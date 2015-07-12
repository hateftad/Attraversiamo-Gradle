package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.me.component.*;
import com.me.component.AnimationComponent.AnimState;
import com.me.component.PlayerComponent.State;
import com.me.listeners.LevelEventListener;
import com.me.ui.InputManager;
import com.me.ui.InputManager.PlayerSelection;
import com.me.utils.Converters;
import com.me.config.GameConfig.Platform;
import com.me.config.GlobalConfig;

public class PlayerOneSystem extends GameEntityProcessingSystem implements
		InputProcessor {

	private final static int left = 0, right = 1, up = 2, down = 3, jump = 4, rag = 5,
			changePlayer = 6, action = 7, skinChange = 8;

	private boolean m_process = true;

	private InputManager m_inputMgr;

	@Mapper
	ComponentMapper<PlayerComponent> m_playerComps;

	@Mapper
	ComponentMapper<PhysicsComponent> m_physComps;

	@Mapper
	ComponentMapper<AnimationComponent> m_animComps;

	@Mapper
	ComponentMapper<MovementComponent> m_movComps;

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

	private float VELOCITY = 11.0f;
	private float VELOCITYINR = 3.0f;

	@SuppressWarnings("unchecked")
	public PlayerOneSystem(LevelEventListener listener) {
		super(Aspect.getAspectForOne(PlayerOneComponent.class));

		m_inputMgr = InputManager.getInstance();
		InputManager.getInstance().addEventListener(listener);

		if (GlobalConfig.getInstance().config.platform == Platform.DESKTOP) {
			Gdx.input.setInputProcessor(this);
		}

	}


	@Override
	protected void process(Entity entity) {

		LadderClimbComponent l = m_ladderComps.get(entity);
		HangComponent h = m_hangComps.get(entity);
		PlayerComponent player = m_playerComps.get(entity);
		AnimationComponent animation = m_animComps.get(entity);
		GrabComponent g = m_grabComps.get(entity);
		TouchComponent touch = m_touchComps.get(entity);
		PhysicsComponent ps = m_physComps.get(entity);

		boolean finish = false;//m_levelManager.isTaskDoneForAll(TaskType.ReachedEnd);

		if (m_inputMgr.isDown(skinChange)) {
			animation.setSkin(m_inputMgr.toggleSkins());
		}

		if (m_inputMgr.m_playerSelected == PlayerSelection.ONE) {
			player.setActive(true);
		} else if (player.canDeActivate() && player.getState() != State.WAITTILDONE) {
			player.setActive(false);
		}

		if (!player.isActive() && !finish) {
			if (!g.m_gonnaGrab && !h.m_isHanging && !g.m_lifting) {
				animation.setAnimationState(AnimState.IDLE);
			}
		}

		MovementComponent m = m_movComps.get(entity);
		m.set(m_inputMgr.isDown(left), m_inputMgr.isDown(right),
				m_inputMgr.isDown(action), m_inputMgr.isDown(down),
				m_inputMgr.isDown(jump));

		VelocityLimitComponent vel = m_velComps.get(entity);

		if (player.isActive() && !m.m_lockControls && !g.m_lifting && !finish && player.getState() != State.WAITTILDONE) {
			if (touch.m_groundTouch && !touch.m_boxTouch && !touch.m_footEdge) {
				animation.setupPose();
			}
			if (!m.m_left && !m.m_right && touch.m_groundTouch && !touch.m_ladderTouch) {
				vel.m_velocity = 0;
				if (!g.m_gonnaGrab) {
					if (!animation.getAnimationState().equals(AnimState.PULLUP)) {
						animation.setAnimationState(AnimState.IDLE);
					}
				}
			}
			if (m_hangComps.has(entity)) {

				if (m.m_up && h.m_isHanging) {
					animation.setAnimationState(AnimState.CLIMBING);
					h.m_climbingUp = true;
					m.m_lockControls = true;
				}
				if (h.m_isHanging && !h.m_climbingUp) {
					animation.setAnimationState(AnimState.HANGING);
				}
			}
			if (m_ladderComps.has(entity)) {
				if (touch.m_ladderTouch) {
					climbLadder(entity);
				}
			}
			if (m.m_left) {
				moveLeft(entity);
			}
			if (m.m_right) {
				moveRight(entity);
			}

			if (m_jumpComps.has(entity)) {
				if (m.m_jump && touch.m_groundTouch) {
					player.setOnGround(false);
					if (m.m_left || m.m_right) {
						animation.setAnimationState(AnimState.JUMPING);
                        if(velocityLimitForJumpBoost(entity)) {
                            ps.setLinearVelocity((m.m_left ? -3 : 3) + ps.getLinearVelocity().x, vel.m_jumpLimit);
                        } else {
                            ps.setLinearVelocity(ps.getLinearVelocity().x, vel.m_jumpLimit);
                        }
					} else if (!touch.m_boxTouch) {
						ps.setLinearVelocity(ps.getLinearVelocity().x, 8);
						animation.setAnimationState(AnimState.UPJUMP);
					}
					player.setState(State.JUMPING);
				}
			}
			if (m_inputMgr.isDown(action)) {
				if (touch.m_footEdge) {
					if (touch.m_footEdgeL) {
						player.setFacingLeft(true);
					}
					if (touch.m_footEdgeR) {
						player.setFacingLeft(false);
					}
					ps.warp("feet", touch.m_touchCenter);
					animation.setAnimationState(AnimState.LIEDOWN);
					g.m_gonnaGrab = true;
				}
				if (touch.m_ladderTouch) {
					vel.m_ladderClimbVelocity = 3;
					l.m_goingUp = true;
				}
				if(touch.m_pushArea){
                    EventComponent component = m_taskComps.get(entity);
					if(touch.m_leftPushArea){
						player.setFacingLeft(false);
						animation.setAnimationState(AnimState.PRESSBUTTON);
						player.setState(State.WAITTILDONE);
                        component.getEventInfo().notify(entity, this);
					}
					if(touch.m_rightPushArea){
						player.setFacingLeft(true);
						animation.setAnimationState(AnimState.PRESSBUTTON);
						player.setState(State.WAITTILDONE);
                        component.getEventInfo().notify(entity, this);

                    }
				}
			}

			if (g.m_gonnaGrab) {
				ps.warp("feet", touch.m_touchCenter);
			}
			if (m.moved()) {
				g.m_gonnaGrab = false;
			}
		}
		
		if(animation.isCompleted() && player.getState() == State.WAITTILDONE){
			player.setState(State.IDLE);
		}
		
		if (finish) {
			animation.setAnimationState(player.getFinishAnimation());
			if (animation.isCompleted()) {
				player.setIsFinishedAnimating(true);
			}
		}

		if (ps.isFalling() && ps.movingForward()) {
			if(!ps.isSubmerged() && !touch.m_feetToBox) {
				animation.setAnimationState(AnimState.FALLING);
			}
		}

		if(ps.isSubmerged()){
			animation.setAnimationState(AnimState.IDLE);
		}

		if (isDead(ps)) {
			m_inputMgr.callRestart();
		}

		// TOO MUCH PROCESSING!!
		animateBody(ps, player, animation);

		animation.setFacing(player.isFacingLeft());

	}

    private boolean velocityLimitForJumpBoost(Entity entity){

        PhysicsComponent physicsComponent = m_physComps.get(entity);
        MovementComponent movementComponent = m_movComps.get(entity);
        System.out.println("Velocity" + physicsComponent.getLinearVelocity().x);
        if(movementComponent.m_left && physicsComponent.getLinearVelocity().x < -4){
            return false;
        } else if(movementComponent.m_right && physicsComponent.getLinearVelocity().x > 4){
            return false;
        }
        return true;
    }

	private void moveLeft(Entity e) {

		AnimationComponent animation = m_animComps.get(e);
		VelocityLimitComponent vel = m_velComps.get(e);
		PhysicsComponent ps = m_physComps.get(e);
		TouchComponent touch = m_touchComps.get(e);
		LadderClimbComponent ladderComp = m_ladderComps.get(e);
		HangComponent h = m_hangComps.get(e);
		PlayerComponent player = m_playerComps.get(e);
		if (h.m_hangingLeft || h.m_hangingRight) {
			vel.m_velocity = 0;
		}
		if (vel.m_velocity > 0)
			vel.m_velocity = -VELOCITYINR;
		if (touch.m_groundTouch) {
			if (m_hangComps.has(e)) {
				PushComponent push = m_pushComps.get(e);
				if (m_ladderComps.has(e) && !h.m_isHanging) {
					if (!ladderComp.m_leftClimb && !touch.m_boxTouch) {
						vel.m_velocity -= VELOCITY * world.delta;
						ps.setLinearVelocity(vel.m_velocity, ps.getLinearVelocity().y);
						if (ps.getLinearVelocity().x < -vel.m_walkLimit) {
							ps.setLinearVelocity(-vel.m_walkLimit, ps.getLinearVelocity().y);
							animation.setAnimationState(AnimState.RUNNING);
							vel.m_velocity = -vel.m_walkLimit;
						} else {
							animation.setAnimationState(AnimState.JOGGING);
						}
					}
					if (touch.m_boxTouch && push.m_pushLeft) {
						ps.setLinearVelocity(-vel.m_pushlimit,
								ps.getLinearVelocity().y);
						animation.setAnimationState(AnimState.PUSHING);
					}
					if (touch.m_boxTouch && !push.m_pushLeft) {
						animation.setAnimationState(AnimState.WALKING);
						ps.setLinearVelocity(-vel.m_pushlimit, ps.getLinearVelocity().y);
					}
					if (ladderComp.m_rightClimb && !ps.isDynamic()) {
						animation.setAnimationState(AnimState.WALKING);
						ps.makeDynamic();
						ps.setLinearVelocity(-vel.m_walkLimit, ps.getLinearVelocity().y);
						touch.m_ladderTouch = false;
						ladderComp.m_rightClimb = false;
					}
				}
			}
		}
		player.setFacingLeft(true);
		player.setState(State.WALKING);
	}

	private void moveRight(Entity e) {

		AnimationComponent animation = m_animComps.get(e);
		VelocityLimitComponent vel = m_velComps.get(e);
		PhysicsComponent ps = m_physComps.get(e);
		TouchComponent touch = m_touchComps.get(e);
		LadderClimbComponent l = m_ladderComps.get(e);
		HangComponent h = m_hangComps.get(e);
		PlayerComponent player = m_playerComps.get(e);

		if (vel.m_velocity < 0)
			vel.m_velocity = VELOCITYINR;
		if (touch.m_groundTouch) {
			if (m_hangComps.has(e)) {
				PushComponent push = m_pushComps.get(e);
				if (m_ladderComps.has(e) && !h.m_isHanging) {
					if (!l.m_rightClimb && !touch.m_boxTouch) {
						vel.m_velocity += VELOCITY * world.delta;
						ps.setLinearVelocity(vel.m_velocity, ps.getLinearVelocity().y);
						if (ps.getLinearVelocity().x > vel.m_walkLimit) {
							ps.setLinearVelocity(vel.m_walkLimit, ps.getLinearVelocity().y);
							animation.setAnimationState(AnimState.RUNNING);
							vel.m_velocity = vel.m_walkLimit;
						} else {
							animation.setAnimationState(AnimState.JOGGING);
						}
					}
					if (touch.m_boxTouch && push.m_pushRight) {
						animation.setAnimationState(AnimState.PUSHING);
						ps.setLinearVelocity(vel.m_pushlimit, ps.getLinearVelocity().y);
					} else if (touch.m_boxTouch && !push.m_pushRight) {
						animation.setAnimationState(AnimState.WALKING);
						ps.setLinearVelocity(vel.m_pushlimit, ps.getLinearVelocity().y);
					}
					if (l.m_leftClimb && !ps.isDynamic()) {
						animation.setAnimationState(AnimState.WALKING);
						ps.makeDynamic();
						ps.setLinearVelocity(vel.m_walkLimit, ps.getLinearVelocity().y);
						touch.m_ladderTouch = false;
						l.m_leftClimb = false;
					}
				}
			}
		}
		player.setFacingLeft(false);
		player.setState(State.WALKING);
	}

	private void climbLadder(Entity e) {

		MovementComponent m = m_movComps.get(e);
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
			animation.setAnimationState(AnimState.LADDERHANG);
		}
		if (vel.m_ladderClimbVelocity > 0)
			animation.setAnimationState(AnimState.LADDERCLIMBUP);
		if (vel.m_ladderClimbVelocity < -1)
			animation.setAnimationState(AnimState.LADDERCLIMBDOWN);
		if (vel.m_ladderClimbVelocity == 0) {
			animation.setAnimationState(AnimState.LADDERHANG);
		}

	}

	private boolean isDead(PhysicsComponent ps) {

		if (ps.getPosition().y < world.getSystem(LevelSystem.class).getCurrentLevel().getLevelBoundaries().minY) {
			return true;
		}
		return false;
	}

	private void animateBody(PhysicsComponent ps, PlayerComponent player,
			AnimationComponent animation) {

		int rot = player.isFacingLeft() ? -1 : 1;
		for (Slot slot : animation.getSkeleton().getSlots()) {
			if (!(slot.getAttachment() instanceof RegionAttachment))
				continue;
			String attachment = slot.getBone().getData().getName();
			if (ps.getBody(attachment) != null) {
				float x = (Converters.ToBox(slot.getBone().getWorldX()));
				float x2 = (ps.getBody().getPosition().x + x);
				float y = Converters.ToBox(slot.getBone().getWorldY());
				float y2 = (ps.getBody().getPosition().y + y);
				ps.getBody(attachment).setTransform(x2, animation.getcenter().y + y2, 0);
			}
		}
	}

	public void restartSystem() {
		m_inputMgr.reset();
	}

	public void clearSystem() {
	}

	public void toggleProcessing(boolean process) {
		m_process = process;
	}

	@Override
	protected boolean checkProcessing() {
		return m_process;
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
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
