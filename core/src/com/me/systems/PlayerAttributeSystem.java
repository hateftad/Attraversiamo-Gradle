package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.me.component.*;
import com.me.events.states.PlayerState;
import com.me.physics.JointFactory;

public class PlayerAttributeSystem extends EntityProcessingSystem {

	@Mapper
	ComponentMapper<PlayerComponent> m_playerComp;
	@Mapper
	ComponentMapper<PhysicsComponent> m_physComp;
	@Mapper
	ComponentMapper<TouchComponent> m_touchComp;
	@Mapper
	ComponentMapper<HangComponent> m_hangComp;
	@Mapper
	ComponentMapper<JointComponent> m_jointComp;
	@Mapper
	ComponentMapper<KeyInputComponent> m_movComp;
	@Mapper
	ComponentMapper<GrabComponent> m_grabComps;
	@Mapper
	ComponentMapper<PlayerTwoComponent> m_playerTwo;
	@Mapper
	ComponentMapper<PlayerAnimationComponent> m_animComps;

	@SuppressWarnings("unchecked")
	public PlayerAttributeSystem() {
		super(Aspect.getAspectForAll(PlayerComponent.class));
	}

	@Override
	protected void process(Entity e) {

		GrabComponent g = m_grabComps.get(e);
		TouchComponent touch = m_touchComp.get(e);
		PhysicsComponent p = m_physComp.get(e);
		if (m_playerComp.get(e).isActive()) {
			if (m_hangComp.has(e)) {
				HangComponent h = m_hangComp.get(e);
				JointComponent j = m_jointComp.get(e);

				if (!h.m_isHanging && touch.m_edgeTouch) {
					h.m_isHanging = true;
					j.setPrismJoint(JointFactory.getInstance().createJoint(
							j.getJointDef()));
				}
				KeyInputComponent m = m_movComp.get(e);
				if (!touch.m_edgeTouch && !j.m_destroyed) {
					if (h.m_isHanging) {
						if (m.m_left && h.m_hangingRight && !h.m_climbingUp) {
							h.m_release = true;
							h.m_isHanging = false;
						}
						if (m.m_right && h.m_hangingLeft && !h.m_climbingUp) {
							h.m_release = true;
							h.m_isHanging = false;
						}
					}
					if (h.m_release) {
						AnimationComponent anim = m_animComps.get(e);
						anim.setAnimationState(PlayerState.Falling);
						release(e);
					}
					if (h.m_climbingUp) {
						PhysicsComponent pComp = m_physComp.get(e);
						pComp.setBodyActive(false);
						AnimationComponent anim = m_animComps.get(e);
						if (anim.isCompleted(PlayerState.ClimbingLedge)) {
							pComp.setAllBodiesPosition(anim.getPositionRelative("left upper leg"));
							pComp.setBodyActive(true);
							anim.setAnimationState(PlayerState.Idle);
							release(e);
						}
					}
				}
				if (touch.m_ladderTouch && p.isDynamic()) {
					p.setLinearVelocity(0, 0);
					p.makeKinematic();
				}
				if (!touch.m_ladderTouch && !p.isDynamic()) {
					p.makeDynamic();
				}
			}
		}
//		if (!m_playerTwo.has(e)) {
//			if (!g.m_grabbed && touch.m_handTouch && touch.m_footEdge) {
//				g.m_grabbed = true;
//			}
//			if (g.m_lifting) {
//				AnimationComponent anim = m_animComps.get(e);
//				anim.setAnimationState(PlayerState.PullUp);
//
//				if (anim.isCompleted(PlayerState.PullUp)) {
//					g.m_lifting = false;
//				}
//			}
//		}
//		if (m_playerTwo.has(e)) {
//			if (g.m_gettingLifted) {
//
//				AnimationComponent anim = m_animComps.get(e);
//				anim.setAnimationState(PlayerState.PullUp);
//				PhysicsComponent pComp = m_physComp.get(e);
//				if (!g.aligned) {
//					pComp.setPosition(g.handPositionX, pComp.getPosition().y);
//					g.aligned = true;
//				}
//				pComp.setBodyActive(false);
//                if (anim.isCompleted(PlayerState.PullUp)) {
//					pComp.setAllBodiesPosition(anim.getPositionRelative("left foot"));
//					pComp.setBodyActive(true);
//					anim.setAnimationState(PlayerState.Idle);
//					g.m_gettingLifted = false;
//					g.aligned = false;
//				}
//			}
//		}
	}

	private void release(Entity e) {

		HangComponent h = m_hangComp.get(e);
		JointComponent j = m_jointComp.get(e);
		JointFactory.getInstance().destroyJoint(j.getJoint());
		j.m_destroyed = true;
		j.setPrismJoint(null);
		h.m_isHanging = false;
		h.m_release = false;
		h.m_climbingUp = false;
		if (h.m_hangingRight)
			h.m_hangingRight = false;
		if (h.m_hangingLeft)
			h.m_hangingLeft = false;
	}

}
