package com.me.physics;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.me.component.*;
import com.me.controllers.B2BuoyancyController;
import com.me.controllers.B2Controller;
import com.me.component.QueueComponent.QueueType;
import com.me.component.PhysicsComponent.ImmediateModePhysicsListener;
import com.me.physics.RBUserData.Type;

public class PhysicsListenerSetup {

	public PhysicsListenerSetup(){

	}

	public void setPlayerPhysics(PhysicsComponent ps)
	{

		ps.setPhysicsListener(new ImmediateModePhysicsListener() {

			public boolean onGround = false;
			public boolean onBox = false;
			@Override
			public void preSolve(Entity e, Contact contact, boolean fixtureA) {

			}

			@Override
			public void beginContact(Entity e, Contact contact, boolean fixtureA) {
				Fixture fA = contact.getFixtureA();
				Fixture fB = contact.getFixtureB();

				if(fA.isSensor()){

					Entity e2 = (Entity) fA.getBody().getUserData();
					PhysicsComponent player = e.getComponent(PhysicsComponent.class);
					PhysicsComponent other = e2.getComponent(PhysicsComponent.class);
					RBUserData otherUd = other.getRBUserData(fA.getBody());
					RBUserData playerUd = player.getRBUserData(fB.getBody());
					if(playerUd == null)
						return;

					if(player.getRBUserData(fB.getBody()).getCollisionGroup() == otherUd.getCollisionGroup()){
						if(e.getComponent(PlayerComponent.class) != null){
							PlayerComponent pl = e.getComponent(PlayerComponent.class);
							boolean created = false;
							if(pl.isFacingLeft() && otherUd.getType() == Type.LEFTEDGE ){
								JointComponent j = e.getComponent(JointComponent.class);
								j.createEdgeHang(other.getBody(), player.getBody("leftH"),3, 11, 0);
								player.setLinearVelocity(player.getLinearVelocity().x, 0);
								created = true;
								e.getComponent(HangComponent.class).m_hangingLeft = true;
							}
							if(!pl.isFacingLeft() && otherUd.getType() == Type.RIGHTEDGE){
								JointComponent j = e.getComponent(JointComponent.class);
								j.createEdgeHang(other.getBody(), player.getBody("rightH"),3, 11, 0);
								player.setLinearVelocity(player.getLinearVelocity().x, 0);
								created = true;
								e.getComponent(HangComponent.class).m_hangingRight = true;
							}
							if(otherUd.getType() == Type.RIGHTLADDER){
								if(!pl.isFacingLeft()){
									e.getComponent(TouchComponent.class).m_ladderTouch = true;
									e.getComponent(LadderClimbComponent.class).m_rightClimb = true;
								}
							}
							if(otherUd.getType() == Type.LEFTLADDER){
								if(pl.isFacingLeft()){
									e.getComponent(TouchComponent.class).m_ladderTouch = true;
									e.getComponent(LadderClimbComponent.class).m_leftClimb = true;
								}
							}
							if(otherUd.getType() == Type.BOTTOMLADDER){
								e.getComponent(LadderClimbComponent.class).m_bottomLadder = true;
							}
							if(otherUd.getType() == Type.TOPLADDER){
								e.getComponent(LadderClimbComponent.class).m_topLadder = true;
								e.getComponent(VelocityLimitComponent.class).m_ladderClimbVelocity = 0;
							}

							if(otherUd.getType() == Type.LEFTPULLUP && fB.isSensor()){
								e.getComponent(TouchComponent.class).m_footEdgeL = true;
								e.getComponent(TouchComponent.class).m_footEdge = true;
								e.getComponent(TouchComponent.class).m_touchCenter = fA.getBody().getPosition().sub(0, 0.5f);
							}
							if(otherUd.getType() == Type.RIGHTPULLUP && fB.isSensor()){
								e.getComponent(TouchComponent.class).m_footEdgeR = true;
								e.getComponent(TouchComponent.class).m_footEdge = true;
								e.getComponent(TouchComponent.class).m_touchCenter = fA.getBody().getPosition().sub(0, 0.5f);
							}
							
							if(otherUd.getType() == Type.LPUSHAREA){
								System.out.println("left push area");
								e.getComponent(TouchComponent.class).m_pushArea = true;
								e.getComponent(TouchComponent.class).m_leftPushArea = true;
							}
							if(otherUd.getType() == Type.RPUSHAREA){
								System.out.println("right push area");
								e.getComponent(TouchComponent.class).m_pushArea = true;
								e.getComponent(TouchComponent.class).m_rightPushArea = true;
							}

							if(otherUd.getType() == Type.HAND){
								if(e2.getComponent(JointComponent.class) != null){
									if(e2.getComponent(GrabComponent.class).m_gonnaGrab){
										if(e2.getComponent(PlayerComponent.class).isFacingLeft() && e.getComponent(PlayerComponent.class).isFacingLeft())
											return;
										if(!e2.getComponent(PlayerComponent.class).isFacingLeft() && !e.getComponent(PlayerComponent.class).isFacingLeft())
											return;
										JointComponent j = e2.getComponent(JointComponent.class);
										j.createHandHang(fA.getBody(), player.getBody(), e2.getComponent(TouchComponent.class).m_footEdgeR);
										e2.getComponent(TouchComponent.class).m_handTouch = true;
										e.getComponent(TouchComponent.class).m_handTouch = true;
										e.getComponent(GrabComponent.class).m_grabbed = true;
										e.getComponent(GrabComponent.class).m_gettingLifted = true;
										e.getComponent(GrabComponent.class).handPositionX = fA.getBody().getPosition().x;
										e2.getComponent(GrabComponent.class).m_gonnaGrab = false;
										e2.getComponent(GrabComponent.class).m_lifting = true;
									}
								}
							}
							if(otherUd.getType() == Type.LEFTCRAWL){
								e.getComponent(CrawlComponent.class).canCrawl = true;
							}
							if(otherUd.getType() == Type.PORTAL){
								if(!e2.getComponent(ParticleComponent.class).isStarted())
									e2.getComponent(ParticleComponent.class).setToStart(true);
								e.getComponent(TouchComponent.class).m_endReach = 1;
							}
							if(otherUd.getType() == Type.FINISH){
								e.getComponent(TouchComponent.class).m_endReach = 1;
							}
							if(created){
								e.getComponent(TouchComponent.class).m_edgeTouch = true;
								e.getComponent(MovementComponent.class).m_lockControls = false;
							}
						}

					}
				} else if(fB.isSensor()){

				}

				if(contact.isTouching())
				{
					Entity e1 = (Entity) fA.getBody().getUserData();

					PhysicsComponent other = e1.getComponent(PhysicsComponent.class);
					PhysicsComponent player = e.getComponent(PhysicsComponent.class);

					RBUserData otherUd = other.getRBUserData(fA.getBody());
					RBUserData playerUd = player.getRBUserData(fB.getBody());
					if(playerUd == null || otherUd == null)
						return;

					if (playerUd.getCollisionGroup() == otherUd.getCollisionGroup()) {

						if(e.getComponent(PlayerComponent.class) != null){
							if(playerUd.getType() == Type.FEET && otherUd.getType() == Type.GROUND ){
								e.getComponent(TouchComponent.class).m_groundTouch = true;
								e.getComponent(MovementComponent.class).m_lockControls = false;
								e.getComponent(GrabComponent.class).m_grabbed = false;
								onGround = true;
							}

							if(playerUd.getType() == Type.FEET && otherUd.getType() == Type.BOX){
								e.getComponent(TouchComponent.class).m_groundTouch = true;
								e.getComponent(TouchComponent.class).m_feetToBox = true;
								e.getComponent(MovementComponent.class).m_lockControls = false;
								e.getComponent(GrabComponent.class).m_grabbed = false;
								onBox = true;
							}

							if(playerUd.getType() == Type.TORSO && otherUd.getType() == Type.GROUND){
								if(!e.getComponent(TouchComponent.class).m_groundTouch){
									if(e.getComponent(HangComponent.class) != null){
										if(!e.getComponent(HangComponent.class).m_isHanging)
											e.getComponent(MovementComponent.class).m_lockControls=true;
									}else{
										e.getComponent(MovementComponent.class).m_lockControls=true;
									}
								}
							}

							if(playerUd.getType() == Type.TORSO && otherUd.getType() == Type.BOX){
								if(e.getComponent(TouchComponent.class).m_groundTouch){
									QueueComponent queueComp = e1.getComponent(QueueComponent.class);
									queueComp.mass = 5f;
									queueComp.type = QueueType.MASS;
									queueComp.bodyName = "box";
									e.getComponent(TouchComponent.class).m_boxTouch = true;
									if(e.getComponent(PushComponent.class) != null){
										if(e.getComponent(PlayerComponent.class).isFacingLeft()){
											e.getComponent(PushComponent.class).m_pushLeft = true;
											e.getComponent(PushComponent.class).m_pushRight = false;
										} else {
											e.getComponent(PushComponent.class).m_pushLeft = false;
											e.getComponent(PushComponent.class).m_pushRight = true;
										}
									}
								}
							}
						}
					}
				}
			}

			@Override
			public void endContact(Entity e, Contact contact, boolean fixtureA) {

				Fixture fA = contact.getFixtureA();
				Fixture fB = contact.getFixtureB();

				if(fA.isSensor() || fB.isSensor() && !contact.isTouching()){
					Entity e1 = (Entity) fA.getBody().getUserData();
					Entity e2 = (Entity) fB.getBody().getUserData();
					PhysicsComponent other = e1.getComponent(PhysicsComponent.class);
					PhysicsComponent player = e2.getComponent(PhysicsComponent.class);

					RBUserData otherUd = other.getRBUserData(fA.getBody());
					RBUserData playerUd = player.getRBUserData(fB.getBody());
					if (playerUd.getCollisionGroup() == otherUd.getCollisionGroup()) {
						e.getComponent(TouchComponent.class).m_edgeTouch = false;
						if(otherUd.getType() == Type.RIGHTLADDER){
							e.getComponent(TouchComponent.class).m_ladderTouch = false;
							e.getComponent(LadderClimbComponent.class).m_rightClimb = false;
						}
						if(otherUd.getType() == Type.LEFTLADDER){
							e.getComponent(TouchComponent.class).m_ladderTouch = false;
							e.getComponent(LadderClimbComponent.class).m_leftClimb = false;
						}
						if(otherUd.getType() == Type.BOTTOMLADDER){
							e.getComponent(LadderClimbComponent.class).m_bottomLadder = false;
						}
						if(otherUd.getType() == Type.TOPLADDER){
							e.getComponent(LadderClimbComponent.class).m_topLadder = false;
						}
						if(otherUd.getType() == Type.LEFTPULLUP && fB.isSensor()){
							e.getComponent(TouchComponent.class).m_footEdgeL = false;
							e.getComponent(TouchComponent.class).m_footEdge = false;
						}
						if(otherUd.getType() == Type.RIGHTPULLUP && fB.isSensor()){
							e.getComponent(TouchComponent.class).m_footEdgeR = false;
							e.getComponent(TouchComponent.class).m_footEdge = false;
						}
						if(otherUd.getType() == Type.HAND){
							if(e.getComponent(JointComponent.class) != null){
								if(e.getComponent(GrabComponent.class).m_grabbed){
									e.getComponent(TouchComponent.class).m_handTouch = false;
									e2.getComponent(TouchComponent.class).m_handTouch = false;
									e.getComponent(GrabComponent.class).m_grabbed = false;
								}
							}
						}
						if(otherUd.getType() == Type.LEFTCRAWL){
							System.out.println("outOfBox");
							e.getComponent(CrawlComponent.class).canCrawl = false;
						}
						if(otherUd.getType() == Type.PORTAL){
							e.getComponent(TouchComponent.class).m_endReach = 0;
						}
						if(otherUd.getType() == Type.LPUSHAREA){
							e.getComponent(TouchComponent.class).m_pushArea = false;
							e.getComponent(TouchComponent.class).m_leftPushArea = false;
						}
						if(otherUd.getType() == Type.RPUSHAREA){
							e.getComponent(TouchComponent.class).m_pushArea = false;
							e.getComponent(TouchComponent.class).m_rightPushArea = false;
						}
					}
				}

				if(!contact.isTouching()){
					Entity e1 = (Entity) fA.getBody().getUserData();
					Entity e2 = (Entity) fB.getBody().getUserData();
					PhysicsComponent other = e1.getComponent(PhysicsComponent.class);
					PhysicsComponent player = e2.getComponent(PhysicsComponent.class);

					RBUserData otherUd = other.getRBUserData(fA.getBody());
					RBUserData playerUd = player.getRBUserData(fB.getBody());
					if(e.getComponent(PlayerComponent.class) != null){
						if (playerUd.getCollisionGroup() == otherUd.getCollisionGroup()) {

							if(playerUd.getType() == Type.FEET && otherUd.getType() == Type.GROUND){
								onGround = false;
								e.getComponent(MovementComponent.class).m_lockControls = false;
							}
							if(playerUd.getType() == Type.FEET && otherUd.getType() == Type.BOX){
								onBox = false;	
								e.getComponent(MovementComponent.class).m_lockControls = false;
								e.getComponent(TouchComponent.class).m_feetToBox = false;
							}
							if(!onGround && !onBox){
								e.getComponent(TouchComponent.class).m_groundTouch = false;
							}
						}
						if(playerUd.getType() == Type.TORSO && otherUd.getType() == Type.BOX){
							Body b = other.getBody("box");
							b.getFixtureList().get(0).setFriction(PhysicsComponent.HIGH_FRICTION);
							e1.getComponent(QueueComponent.class).mass = 20f;
							e.getComponent(TouchComponent.class).m_boxTouch = false;
							if(e.getComponent(PushComponent.class) != null){
								e.getComponent(PushComponent.class).m_pushLeft = false;
								e.getComponent(PushComponent.class).m_pushRight = false;
							}
						}
					}
				}
			}

			@Override
			public void onRestart() {
				onBox = false;
				onGround = false;				
			}
		});
	}

	public void setLevelPhysics(PhysicsComponent pComp) {

		pComp.setPhysicsListener(new ImmediateModePhysicsListener() {

			@Override
			public void preSolve(Entity e, Contact contact, boolean fixtureA) {

			}

			@Override
			public void onRestart() {

			}

			@Override
			public void endContact(Entity e, Contact contact, boolean fixtureA) {

				Fixture fixA = contact.getFixtureA();
				Fixture fixB = contact.getFixtureB();

				if ((fixA.isSensor()) && (fixA.getUserData() != null))
				{
					B2Controller b2c = (B2Controller) fixA.getUserData();
					b2c.removeBody(fixB.getBody());
					if(b2c instanceof B2BuoyancyController) {
						treatBouyancy(fixB.getBody(), false);
					}
				}
				else if ((fixB.isSensor()) && (fixB.getUserData() != null))
				{
					B2Controller b2c = (B2Controller) fixB.getUserData();
					b2c.removeBody(fixA.getBody());
					if(b2c instanceof B2BuoyancyController){
						treatBouyancy(fixA.getBody(), false);
					}
				}
			}

			@Override
			public void beginContact(Entity e, Contact contact, boolean fixtureA) {
				Fixture fixA = contact.getFixtureA();
				Fixture fixB = contact.getFixtureB();
				if ((fixA.isSensor()) && (fixA.getUserData() != null))
				{
					B2Controller b2c = (B2Controller) fixA.getUserData();
					b2c.addBody(fixB.getBody());
					if(b2c instanceof B2BuoyancyController) {
						treatBouyancy(fixB.getBody(), true);
					}
				}
				else if ((fixB.isSensor()) && (fixB.getUserData() != null))
				{
					B2Controller b2c = (B2Controller) fixB.getUserData();
					b2c.addBody(fixA.getBody());
					if(b2c instanceof B2BuoyancyController) {
						treatBouyancy(fixA.getBody(), true);
					}
				}
			}

			private void treatBouyancy(Body body, boolean submerged){
				Entity entity = (Entity) body.getUserData();
				PhysicsComponent ps = entity.getComponent(PhysicsComponent.class);
				RBUserData otherUd = ps.getRBUserData(body);
				if (otherUd.getType() == Type.BOX && submerged) {
					ps.setFriction(PhysicsComponent.LOW_FRICTION);
				} else if(otherUd.getType() == Type.BOX && !submerged){
					ps.setFriction(PhysicsComponent.HIGH_FRICTION);
				}
				if(otherUd.getType()==Type.FEET && entity.getComponent(PlayerTwoComponent.class) != null){
					QueueComponent queueComp = new QueueComponent();
					if(submerged) {
						queueComp.mass = 1.6f;
						queueComp.type = QueueType.MASSTEMP;
						queueComp.bodyName = "feet";
					}else{
						queueComp.mass = 1f;
						queueComp.type = QueueType.MASSTEMP;
						queueComp.bodyName = "feet";
					}
					entity.addComponent(queueComp);
				}
				if(otherUd.getType() == Type.PELVIS){
					System.out.println("Under Water");
				}
			}
		});
	}
}
