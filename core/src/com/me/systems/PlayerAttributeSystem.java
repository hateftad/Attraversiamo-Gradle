package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.me.component.AnimationComponent;
import com.me.component.AnimationComponent.AnimState;
import com.me.component.GrabComponent;
import com.me.component.HangComponent;
import com.me.component.JointComponent;
import com.me.component.MovementComponent;
import com.me.component.PhysicsComponent;
import com.me.component.PlayerComponent;
import com.me.component.PlayerTwoComponent;
import com.me.component.TouchComponent;
import com.me.physics.JointFactory;

public class PlayerAttributeSystem extends EntityProcessingSystem {


	@Mapper ComponentMapper< PlayerComponent> m_playerComp;
	@Mapper ComponentMapper<PhysicsComponent> m_physComp;
	@Mapper ComponentMapper<TouchComponent> m_touchComp;
	@Mapper ComponentMapper<HangComponent> m_hangComp;
	@Mapper ComponentMapper<JointComponent> m_jointComp;
	@Mapper ComponentMapper<MovementComponent> m_movComp;
	@Mapper ComponentMapper<GrabComponent> m_grabComps;
	@Mapper ComponentMapper<PlayerTwoComponent> m_playerTwo;
	@Mapper ComponentMapper<AnimationComponent> m_animComps;
	
	private boolean breakBond = false;
	private boolean boost = false;
	
	@SuppressWarnings("unchecked")
	public PlayerAttributeSystem() {
		super(Aspect.getAspectForAll(PlayerComponent.class));
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(Entity e) {

		GrabComponent g = m_grabComps.get(e);
		TouchComponent touch = m_touchComp.get(e);
		PhysicsComponent p = m_physComp.get(e);
		if(m_playerComp.get(e).isActive()){
			if(m_hangComp.has(e)){
				HangComponent h = m_hangComp.get(e);
				JointComponent j = m_jointComp.get(e);
				if(!h.m_isHanging && touch.m_edgeTouch){
					h.m_isHanging = true;
					j.setPrismJoint(JointFactory.getInstance().createJoint(j.getPJointDef()));
				}
				MovementComponent m = m_movComp.get(e);
				if(!touch.m_edgeTouch && !j.m_destroyed){
					if(h.m_isHanging){
						if(m.m_left && h.m_hangingRight){
							h.m_release = true;
							h.m_isHanging=false;
						}
						if(m.m_right && h.m_hangingLeft){
							h.m_release = true;
							h.m_isHanging = false;
						}
					}
					if(h.m_release){
						release(e);
					}
					if(j.isFullLength()){
						if(h.m_hangingLeft){
							p.setLinearVelocity(-4, 0);
						}
						if(h.m_hangingRight){
							p.setLinearVelocity(4, 0);
						}
						release(e);
					}
				}
				if(touch.m_ladderTouch && p.isDynamic()){
					p.setLinearVelocity(0,0);
					p.makeKinematic();
				}
				if(!touch.m_ladderTouch && !p.isDynamic()){
					p.makeDynamic();
				}
			}

		}
		if(!m_playerTwo.has(e)){
			JointComponent j = m_jointComp.get(e);
			if(!g.m_grabbed && touch.m_handTouch && touch.m_footEdge){
				g.m_grabbed = true;
				//j.setWeldJoint(JointFactory.getInstance().createJoint(j.getWJointDef()));
			}
			if(g.m_lifting){
				AnimationComponent anim = m_animComps.get(e);
				//if(j.getWeldJoint() != null){
				anim.setAnimationState(AnimState.PULLUP);
				//	if(anim.getTime() > 1.72f){
				//		breakBond = true;
				//	}
				//}
				if(anim.isCompleted(AnimState.PULLUP)){
					g.m_lifting = false;
				}
			}
			if(breakBond){
				if(j.getWeldJoint()!= null){
					JointFactory.getInstance().destroyJoint(j.getWeldJoint());
					p.setLinearVelocity(5.5f, 0);
					j.setWeldJoint(null);
					breakBond = false;
					boost = true;
				}
			}
		}
		if(m_playerTwo.has(e)){
			if(g.m_gettingLifted){
				AnimationComponent anim = m_animComps.get(e);
				anim.setAnimationState(AnimState.PULLUP);
				PhysicsComponent pComp = m_physComp.get(e);
				pComp.setBodyActive(false);
				if(anim.isCompleted(AnimState.PULLUP)){
					pComp.setBodyActive(true);
					System.out.println("lifted done");
					pComp.setAllBodiesPosition(anim.getPosition(pComp.getPosition()));
					anim.setAnimationState(AnimState.IDLE);
					g.m_gettingLifted = false;
				}
				/*
				if(boost){
					if(m_playerComp.get(e).isFacingLeft()){
						p.setLinearVelocity(-2f, 7f);
					}else{
						p.setLinearVelocity(2f, 7f);
					}
					boost = false;
					g.m_gettingLifted = false;
				}
				*/
			}
		}
	}

	private void release(Entity e)
	{

		HangComponent h = m_hangComp.get(e);
		JointComponent j = m_jointComp.get(e);
		JointFactory.getInstance().destroyJoint(j.getPrismJoint());
		j.m_destroyed = true;
		j.setPrismJoint(null);
		h.m_isHanging = false;
		h.m_release = false;
		h.m_climbingUp = false;
		if(h.m_hangingRight)
			h.m_hangingRight = false;
		if(h.m_hangingLeft)
			h.m_hangingLeft = false;
	}

}
