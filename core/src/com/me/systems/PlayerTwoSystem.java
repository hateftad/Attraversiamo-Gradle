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
import com.me.component.CrawlComponent;
import com.me.component.GrabComponent;
import com.me.component.JumpComponent;
import com.me.component.MovementComponent;
import com.me.component.PhysicsComponent;
import com.me.component.PlayerComponent;
import com.me.component.PlayerComponent.State;
import com.me.component.PlayerTwoComponent;
import com.me.component.TouchComponent;
import com.me.component.VelocityLimitComponent;
import com.me.component.AnimationComponent.AnimState;
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

	@Mapper ComponentMapper<CrawlComponent> m_crawlComps;

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
		CrawlComponent crawlComp = m_crawlComps.get(e);
		boolean finish = world.getSystem(LevelSystem.class).getLevelComponent().m_finished;

		if(m_inputMgr.m_playerSelected == PlayerSelection.TWO){
			player.setActive(true);
		}else if(player.canDeActivate()){
			player.setActive(false);
		}

		if(!player.isActive() && !finish){
			if(!g.m_gettingLifted && touch.m_groundTouch && !crawlComp.isCrawling){
				animation.setAnimationState(AnimState.IDLE);
			}
		}
		
		if(crawlComp.isStanding && animation.isCompleted(AnimState.STANDUP)){
			crawlComp.isStanding = false;
			player.setState(State.IDLE); 
		}

		MovementComponent m = m_movComps.get(e);
		m.set(m_inputMgr.isDown(left), m_inputMgr.isDown(right), m_inputMgr.isDown(up), m_inputMgr.isDown(down), m_inputMgr.isDown(jump));
		if(player.isActive() && !m.m_lockControls && !g.m_gettingLifted && !finish && !crawlComp.isStanding){
			VelocityLimitComponent vel = m_velComps.get(e);
			//animation.printStateChange();
			if(touch.m_groundTouch && !crawlComp.isCrawling){
				animation.setupPose();
			}
			
			if(isIdle(e)) {
				animation.setAnimationState(AnimState.IDLE);
				vel.m_velocity = 0;
			}

			if(m.m_left && touch.m_groundTouch){
				if(crawlComp.isCrawling){
					crawlLeft(e);
				} else{
					walkLeft(e);
				}
				player.setFacingLeft(true);
			}

			if(m.m_right && touch.m_groundTouch){
				if(crawlComp.isCrawling){
					crawlRight(e);
				} else{
					walkRight(e);
				}
				player.setFacingLeft(false);
			}

			if(m.m_jump && touch.m_groundTouch && (!m.m_left && !m.m_right)){
				player.setOnGround(false);
				m_jumpComps.get(e).m_jumped = true;
				animation.setAnimationState(AnimState.UPJUMP);
				player.setState(State.JUMPING);
			}

			if(m_jumpComps.get(e).m_jumped){
				if(animation.getTime() > 0.2f){
					ps.setLinearVelocity(ps.getLinearVelocity().x, vel.m_jumpLimit);
					m_jumpComps.get(e).m_jumped = m_playerComps.get(e).isOnGround();
					player.setState(State.JUMPED);
				} 
			}

			if(m_inputMgr.isDown(action)){
				if(crawlComp.canCrawl && !crawlComp.isCrawling){
					animation.setAnimationState(AnimState.LIEDOWN);
					player.setState(State.LYINGDOWN);
				}
			} 
			
			if(animation.isCompleted(AnimState.LIEDOWN)){
				animation.setAnimationState(AnimState.LYINGDOWN);
				crawlComp.isCrawling = true;
				ps.disableBody("center");
			}

			if(player.getState().equals(State.CRAWLING) && !ps.movingForward()){
				animation.setAnimationState(AnimState.LYINGDOWN);
			}
			
			if(!crawlComp.canCrawl && crawlComp.isCrawling){
				animation.setAnimationState(AnimState.STANDUP);
				crawlComp.isCrawling = false;
				crawlComp.isStanding = true;
				ps.enableBody("center");
			}
			
		}

		if(finish){
			animation.setAnimationState(m_playerConfig.m_finishAnimation);
		}

		if(isDead(ps)){
			world.getSystem(PhysicsSystem.class).onRestartLevel();
		}

		animateBody(ps, player, animation);

		animation.setFacing(player.isFacingLeft());

	}

	private boolean isIdle(Entity e){

		PlayerComponent player = m_playerComps.get(e);
		AnimationComponent animation = m_animComps.get(e);
		TouchComponent touch = m_touchComps.get(e);
		MovementComponent m = m_movComps.get(e);

		if(!m.m_left && !m.m_right && 
				touch.m_groundTouch && 
				!player.getState().equals(State.JUMPING) && 
				!animation.getAnimationState().equals(AnimState.PULLUP) && 
				!player.getState().equals(State.LYINGDOWN)  && 
				!player.getState().equals(State.CRAWLING)) {
			player.setState(State.IDLE);
			return true;
		}

		return false;
	}

	private void crawlLeft(Entity e){
		PhysicsComponent ps = m_physComps.get(e);
		PlayerComponent player = m_playerComps.get(e);
		AnimationComponent animation = m_animComps.get(e);
		VelocityLimitComponent vel = m_velComps.get(e);

		vel.m_velocity = -vel.m_crawlLimit;
		ps.setLinearVelocity(vel.m_velocity, ps.getLinearVelocity().y);
		animation.setAnimationState(AnimState.CRAWL);
		player.setState(State.CRAWLING);
	}

	private void crawlRight(Entity e){
		PhysicsComponent ps = m_physComps.get(e);
		PlayerComponent player = m_playerComps.get(e);
		AnimationComponent animation = m_animComps.get(e);
		VelocityLimitComponent vel = m_velComps.get(e);

		vel.m_velocity = vel.m_crawlLimit;
		ps.setLinearVelocity(vel.m_velocity, ps.getLinearVelocity().y);
		animation.setAnimationState(AnimState.CRAWL);
		player.setState(State.CRAWLING);
	}

	private void walkLeft(Entity e){

		PhysicsComponent ps = m_physComps.get(e);
		PlayerComponent player = m_playerComps.get(e);
		AnimationComponent animation = m_animComps.get(e);
		VelocityLimitComponent vel = m_velComps.get(e);

		if(vel.m_velocity > 0){
			vel.m_velocity=0;
		}
		vel.m_velocity -= 5.5f * world.delta;
		ps.setLinearVelocity(vel.m_velocity, ps.getLinearVelocity().y);
		if(ps.getLinearVelocity().x < -vel.m_walkLimit){
			ps.setLinearVelocity(-vel.m_walkLimit, ps.getLinearVelocity().y);
			animation.setAnimationState(AnimState.RUNNING);
			vel.m_velocity=-vel.m_walkLimit;
		}else{
			animation.setAnimationState(AnimState.WALKING);
		}
		player.setState(State.WALKING);

	}

	private void walkRight(Entity e){

		PhysicsComponent ps = m_physComps.get(e);
		PlayerComponent player = m_playerComps.get(e);
		AnimationComponent animation = m_animComps.get(e);
		VelocityLimitComponent vel = m_velComps.get(e);

		if(vel.m_velocity < 0){
			vel.m_velocity = 0;
		}

		vel.m_velocity += 5.5f * world.delta;
		ps.setLinearVelocity(vel.m_velocity, ps.getLinearVelocity().y);
		if(ps.getLinearVelocity().x > vel.m_walkLimit){
			ps.setLinearVelocity(vel.m_walkLimit, ps.getLinearVelocity().y);
			animation.setAnimationState(AnimState.RUNNING);
			vel.m_velocity = vel.m_walkLimit;
		}else{
			animation.setAnimationState(AnimState.WALKING);
		}
		player.setState(State.WALKING);

	}

	private void animateBody(PhysicsComponent ps, PlayerComponent player, AnimationComponent animation){

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
