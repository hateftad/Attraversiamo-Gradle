package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.InputProcessor;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.me.component.AnimationComponent;
import com.me.component.GrabComponent;
import com.me.component.JumpComponent;
import com.me.component.MovementComponent;
import com.me.component.PhysicsComponent;
import com.me.component.PlayerComponent;
import com.me.component.PlayerTwoComponent;
import com.me.component.TouchComponent;
import com.me.component.VelocityLimitComponent;
import com.me.component.AnimationComponent.State;
import com.me.ui.InputManager;
import com.me.ui.InputManager.PlayerSelection;
import com.me.utils.Converters;
import com.me.utils.PlayerConfig;

public class PlayerTwoSystem extends EntityProcessingSystem implements InputProcessor {

	//GET ALL OF THESE FROM ONE SUPER CLASS

	@Mapper ComponentMapper<PlayerComponent> m_playerComps;

	@Mapper ComponentMapper<AnimationComponent> m_animComps;

	@Mapper ComponentMapper<MovementComponent> m_movComps;

	@Mapper ComponentMapper<TouchComponent> m_touchComps;

	@Mapper ComponentMapper<JumpComponent> m_jumpComps;

	@Mapper ComponentMapper<GrabComponent> m_grabComps;

	@Mapper ComponentMapper<PhysicsComponent> m_physComps;

	@Mapper ComponentMapper<VelocityLimitComponent> m_velComps;

	private InputManager m_inputMgr;

	private PlayerConfig m_playerConfig;

	private int left = 0, right = 1, up = 2, down = 3, jump = 4, rag = 5, changePlayer = 6, action = 7; 

	@SuppressWarnings("unchecked")
	public PlayerTwoSystem() {
		super(Aspect.getAspectForOne(PlayerTwoComponent.class));

		m_inputMgr = InputManager.getInstance();
	}

	public void setPlayerConfig(PlayerConfig playerCfg){
		m_playerConfig = playerCfg;
	}

	@Override
	protected void process(Entity e) {

		PhysicsComponent ps = m_physComps.get(e);
		PlayerComponent player = m_playerComps.get(e);
		AnimationComponent animation = m_animComps.get(e);
		GrabComponent g = m_grabComps.get(e);
		TouchComponent touch = m_touchComps.get(e);
		boolean finish = world.getSystem(LevelSystem.class).getLevelComponent().m_finished;

		if(m_inputMgr.m_playerSelected == PlayerSelection.TWO){
			player.setActive(true);
		}else if(player.canDeActivate()){
			player.setActive(false);
		}

		if(!player.isActive() && !finish){
			if(!g.m_gettingLifted)
				animation.setAnimationState(State.IDLE);
		}

		if(m_movComps.has(e)){
			MovementComponent m = m_movComps.get(e);
			m.set(m_inputMgr.isDown(left), m_inputMgr.isDown(right), m_inputMgr.isDown(up), m_inputMgr.isDown(down), m_inputMgr.isDown(jump));
			if(player.isActive() && !m.m_lockControls && !g.m_gettingLifted && !finish){
				VelocityLimitComponent vel = m_velComps.get(e);

				if(touch.m_groundTouch&&!touch.m_boxTouch){
					animation.setupPose();
				}
				if(!m.m_left && !m.m_right && touch.m_groundTouch) {
					if(!g.m_gonnaGrab && !m_jumpComps.get(e).m_jumped){
						if(!animation.getAnimationState().equals(State.PULLUP)){
							animation.setAnimationState(State.IDLE);
						}
						else{
							if(animation.isCompleted()){
								animation.setAnimationState(State.IDLE);
							}
						}
					}
					vel.m_velocity = 0;
				}


				if(m.m_left){					
					if(vel.m_velocity > 0)
						vel.m_velocity=0;
					if(touch.m_groundTouch){
						vel.m_velocity -= 5.5f * world.delta;
						ps.setLinearVelocity(vel.m_velocity, ps.getLinearVelocity().y);
						if(ps.getLinearVelocity().x < -vel.m_walkLimit){
							ps.setLinearVelocity(-vel.m_walkLimit, ps.getLinearVelocity().y);
							animation.setAnimationState(State.RUNNING);
							vel.m_velocity=-vel.m_walkLimit;
						}else{
							animation.setAnimationState(State.WALKING);
						}
					}
					player.setFacingLeft(true);
				}
				if(m.m_right){
					if(vel.m_velocity < 0)
						vel.m_velocity = 0;
					if(touch.m_groundTouch){
						vel.m_velocity += 5.5f * world.delta;
						ps.setLinearVelocity(vel.m_velocity, ps.getLinearVelocity().y);
						if(ps.getLinearVelocity().x > vel.m_walkLimit){
							ps.setLinearVelocity(vel.m_walkLimit, ps.getLinearVelocity().y);
							animation.setAnimationState(State.RUNNING);
							vel.m_velocity = vel.m_walkLimit;
						}else{
							animation.setAnimationState(State.WALKING);
						}
					}
					player.setFacingLeft(false);
				}
				if(m.m_jump && touch.m_groundTouch && (!m.m_left && !m.m_right)){
					player.setOnGround(false);
					m_jumpComps.get(e).m_jumped = true;
					animation.setAnimationState(State.UPJUMP);
				}
				if(m_jumpComps.get(e).m_jumped){
					if(animation.getTime() > 0.2f){
						ps.setLinearVelocity(ps.getLinearVelocity().x, 8);
						m_jumpComps.get(e).m_jumped = m_playerComps.get(e).isOnGround();
					} 
				}

			}
			if(finish){
				animation.setAnimationState(m_playerConfig.m_finishAnimation);
			}

			if(isDead(ps)){
				world.getSystem(PhysicsSystem.class).onRestartLevel();
			}

			int rot = player.isFacingLeft() ? -1 : 1;
			for (Slot slot : animation.getSkeleton().getSlots()) {
				if (!(slot.getAttachment() instanceof RegionAttachment)) continue;
				String attachment = slot.getBone().getData().getName();
				//System.out.println(attachment.getName());
				if(ps.getBody(attachment) != null){
					float x = (Converters.ToBox(slot.getBone().getWorldX()));
					float x2 = (ps.getBody().getPosition().x +  x);
					float y = Converters.ToBox(slot.getBone().getWorldY());
					float y2 = (ps.getBody().getPosition().y + y);
					//if(!touch.m_handTouch){
					ps.getBody(attachment).setTransform(
							x2, 
							animation.getcenter().y + y2, 
							0
							);
					//}else{
					//	ps.getBody(attachment.getName()).setType(BodyType.StaticBody);
					/*
						ps.getBody(attachment.getName()).setTransform(
								x2, 
								animation.getcenter().y + y2, 
								rot * (33f + slot.getBone().getWorldRotation() * MathUtils.degreesToRadians)
								);
					 */

				}
			}

		}


		animation.setFacing(player.isFacingLeft());


	}

	private boolean isDead(PhysicsComponent ps) {

		if(ps.getPosition().y < -40){
			return true;
		}
		return false;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}



}
