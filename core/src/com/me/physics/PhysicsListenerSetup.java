package com.me.physics;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.ObjectMap;
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
				Entity e2 = (Entity) fA.getBody().getUserData();
				PhysicsComponent player = e.getComponent(PhysicsComponent.class);
				PhysicsComponent other = e2.getComponent(PhysicsComponent.class);
				RBUserData otherUd = other.getRBUserData(fA.getBody());
				RBUserData playerUd = player.getRBUserData(fB.getBody());
				if(playerUd == null || otherUd == null)
					return;

				System.out.println(otherUd.getType());
				System.out.println(playerUd.getType());

				if(fA.isSensor()){

					if(player.getRBUserData(fB.getBody()).getCollisionGroup() == otherUd.getCollisionGroup()){
						if(e.getComponent(PlayerComponent.class) != null){
							PlayerComponent pl = e.getComponent(PlayerComponent.class);
							boolean created = false;
							if(pl.isFacingLeft() && otherUd.getType() == Type.LeftEdge && playerUd.getType() == Type.HangHands){
								JointComponent j = e.getComponent(JointComponent.class);
								j.createEdgeHang(other.getBody(), player.getBody("leftH"),3, 11, 0);
								player.setLinearVelocity(player.getLinearVelocity().x, 0);
								created = true;
								e.getComponent(HangComponent.class).m_hangingLeft = true;
							}
							if(!pl.isFacingLeft() && otherUd.getType() == Type.RightEdge && playerUd.getType() == Type.HangHands){
								JointComponent j = e.getComponent(JointComponent.class);
								j.createEdgeHang(other.getBody(), player.getBody("rightH"),3, 11, 0);
								player.setLinearVelocity(player.getLinearVelocity().x, 0);
								created = true;
								e.getComponent(HangComponent.class).m_hangingRight = true;
							}
							if(otherUd.getType() == Type.RightLadder){
								if(!pl.isFacingLeft()){
									e.getComponent(TouchComponent.class).m_ladderTouch = true;
									e.getComponent(LadderClimbComponent.class).m_rightClimb = true;
								}
							}
							if(otherUd.getType() == Type.LeftLadder){
								if(pl.isFacingLeft()){
									e.getComponent(TouchComponent.class).m_ladderTouch = true;
									e.getComponent(LadderClimbComponent.class).m_leftClimb = true;
								}
							}
							if(otherUd.getType() == Type.BottomLadder){
								e.getComponent(LadderClimbComponent.class).m_bottomLadder = true;
							}
							if(otherUd.getType() == Type.TopLadder){
								e.getComponent(LadderClimbComponent.class).m_topLadder = true;
								e.getComponent(VelocityLimitComponent.class).m_ladderClimbVelocity = 0;
							}

							if(otherUd.getType() == Type.LeftPullup && fB.isSensor()){
								e.getComponent(TouchComponent.class).m_footEdgeL = true;
								e.getComponent(TouchComponent.class).m_footEdge = true;
								e.getComponent(TouchComponent.class).m_touchCenter = fA.getBody().getPosition().sub(0, 0.5f);
							}
							if(otherUd.getType() == Type.RightPullup && fB.isSensor()){
								e.getComponent(TouchComponent.class).m_footEdgeR = true;
								e.getComponent(TouchComponent.class).m_footEdge = true;
								e.getComponent(TouchComponent.class).m_touchCenter = fA.getBody().getPosition().sub(0, 0.5f);
							}
							
							if(otherUd.getType() == Type.LeftPushButton){
								System.out.println("left push area");
								e.getComponent(TouchComponent.class).m_pushArea = true;
								e.getComponent(TouchComponent.class).m_leftPushArea = true;
							}
							if(otherUd.getType() == Type.RightPushButton){
								System.out.println("right push area");
								e.getComponent(TouchComponent.class).m_pushArea = true;
								e.getComponent(TouchComponent.class).m_rightPushArea = true;
							}

							if(otherUd.getType() == Type.Hand){
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
							if(otherUd.getType() == Type.LeftCrawl){
								e.getComponent(CrawlComponent.class).canCrawl = true;
							}
							if(otherUd.getType() == Type.Portal){
								if(!e2.getComponent(ParticleComponent.class).isStarted())
									e2.getComponent(ParticleComponent.class).setToStart(true);
								e.getComponent(TouchComponent.class).m_endReach = 1;
							}
							if(otherUd.getType() == Type.Finish){
								e.getComponent(TouchComponent.class).m_endReach = 1;
							}
							if(created){
								e.getComponent(TouchComponent.class).m_edgeTouch = true;
								e.getComponent(MovementComponent.class).m_lockControls = false;
							}
						}

					}
				} else if(fB.isSensor()){
					System.out.println("other is sensor");
				}

				if(contact.isTouching())
				{
					Entity e1 = (Entity) fA.getBody().getUserData();

					if (playerUd.getCollisionGroup() == otherUd.getCollisionGroup()) {

						if(e.getComponent(PlayerComponent.class) != null){
							if(playerUd.getType() == Type.Feet && otherUd.getType() == Type.Ground){
								e.getComponent(TouchComponent.class).m_groundTouch = true;
								e.getComponent(MovementComponent.class).m_lockControls = false;
								e.getComponent(GrabComponent.class).m_grabbed = false;
								onGround = true;
							}

							if(playerUd.getType() == Type.Feet && otherUd.getType() == Type.Box){
								e.getComponent(TouchComponent.class).m_groundTouch = true;
								e.getComponent(TouchComponent.class).m_feetToBox = true;
								e.getComponent(MovementComponent.class).m_lockControls = false;
								e.getComponent(GrabComponent.class).m_grabbed = false;
								onBox = true;
							}

							if(playerUd.getType() == Type.Torso && otherUd.getType() == Type.Ground){
								if(!e.getComponent(TouchComponent.class).m_groundTouch){
									if(e.getComponent(HangComponent.class) != null){
										if(!e.getComponent(HangComponent.class).m_isHanging)
											e.getComponent(MovementComponent.class).m_lockControls=true;
									}else{
										e.getComponent(MovementComponent.class).m_lockControls=true;
									}
								}
							}

							if(playerUd.getType() == Type.Torso && otherUd.getType() == Type.Box){
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
						if(otherUd.getType() == Type.RightLadder){
							e.getComponent(TouchComponent.class).m_ladderTouch = false;
							e.getComponent(LadderClimbComponent.class).m_rightClimb = false;
						}
						if(otherUd.getType() == Type.LeftLadder){
							e.getComponent(TouchComponent.class).m_ladderTouch = false;
							e.getComponent(LadderClimbComponent.class).m_leftClimb = false;
						}
						if(otherUd.getType() == Type.BottomLadder){
							e.getComponent(LadderClimbComponent.class).m_bottomLadder = false;
						}
						if(otherUd.getType() == Type.TopLadder){
							e.getComponent(LadderClimbComponent.class).m_topLadder = false;
						}
						if(otherUd.getType() == Type.LeftPullup && fB.isSensor()){
							e.getComponent(TouchComponent.class).m_footEdgeL = false;
							e.getComponent(TouchComponent.class).m_footEdge = false;
						}
						if(otherUd.getType() == Type.RightPullup && fB.isSensor()){
							e.getComponent(TouchComponent.class).m_footEdgeR = false;
							e.getComponent(TouchComponent.class).m_footEdge = false;
						}
						if(otherUd.getType() == Type.Hand){
							if(e.getComponent(JointComponent.class) != null){
								if(e.getComponent(GrabComponent.class).m_grabbed){
									e.getComponent(TouchComponent.class).m_handTouch = false;
									e2.getComponent(TouchComponent.class).m_handTouch = false;
									e.getComponent(GrabComponent.class).m_grabbed = false;
								}
							}
						}
						if(otherUd.getType() == Type.LeftCrawl){
							System.out.println("outOfBox");
							e.getComponent(CrawlComponent.class).canCrawl = false;
						}
						if(otherUd.getType() == Type.Portal){
							e.getComponent(TouchComponent.class).m_endReach = 0;
						}
						if(otherUd.getType() == Type.LeftPushButton){
							e.getComponent(TouchComponent.class).m_pushArea = false;
							e.getComponent(TouchComponent.class).m_leftPushArea = false;
						}
						if(otherUd.getType() == Type.RightPushButton){
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

							if(playerUd.getType() == Type.Feet && otherUd.getType() == Type.Ground){
								onGround = false;
								e.getComponent(MovementComponent.class).m_lockControls = false;
							}
							if(playerUd.getType() == Type.Feet && otherUd.getType() == Type.Box){
								onBox = false;	
								e.getComponent(MovementComponent.class).m_lockControls = false;
								e.getComponent(TouchComponent.class).m_feetToBox = false;
							}
							if(!onGround && !onBox){
								e.getComponent(TouchComponent.class).m_groundTouch = false;
							}
						}
						if(playerUd.getType() == Type.Torso && otherUd.getType() == Type.Box){
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
					determineController((ObjectMap)fixA.getUserData(), fixB.getBody(), true);
					treatBouyancy(fixB.getBody(), false);
				}
				else if ((fixB.isSensor()) && (fixB.getUserData() != null))
				{
					determineController((ObjectMap) fixB.getUserData(), fixA.getBody(), false);
					treatBouyancy(fixA.getBody(), false);
				}
			}

			@Override
			public void beginContact(Entity e, Contact contact, boolean fixtureA) {
				Fixture fixA = contact.getFixtureA();
				Fixture fixB = contact.getFixtureB();
				printBodies(fixA, fixB);
				if ((fixA.isSensor()) && (fixA.getUserData() != null))
				{
					determineController((ObjectMap)fixA.getUserData(), fixB.getBody(), true);
					treatBouyancy(fixB.getBody(), true);
				}
				else if ((fixB.isSensor()) && (fixB.getUserData() != null))
				{
					determineController((ObjectMap) fixB.getUserData(), fixA.getBody(), true);
					treatBouyancy(fixB.getBody(), true);
				}
			}

			private void determineController(ObjectMap<String, B2BuoyancyController> controllerMap, Body body, boolean add){
				Entity entity = (Entity) body.getUserData();
				B2BuoyancyController b2c;
				if(entity.getComponent(PlayerOneComponent.class) != null){
					b2c = controllerMap.get("playerOne");
				}else if(entity.getComponent(PlayerTwoComponent.class) != null){
					b2c = controllerMap.get("playerTwo");
				} else {
					b2c = controllerMap.get("worldObjects");
				}
				if(add) {
					b2c.addBody(body);
				}else {
					b2c.removeBody(body);
				}
			}

			private void treatBouyancy(Body body, boolean submerged){
				Entity entity = (Entity) body.getUserData();
				PhysicsComponent ps = entity.getComponent(PhysicsComponent.class);
				RBUserData otherUd = ps.getRBUserData(body);
				if (otherUd.getType() == Type.Box && submerged) {
					ps.setFriction(PhysicsComponent.LOW_FRICTION);
				} else if(otherUd.getType() == Type.Box && !submerged){
					ps.setFriction(PhysicsComponent.HIGH_FRICTION);
				}
				if(otherUd.getType()==Type.Feet && entity.getComponent(PlayerTwoComponent.class) != null){
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
				if(otherUd.getType() == Type.Pelvis){
					System.out.println("Under Water");
				}
			}

			private void printBodies(Fixture fixA, Fixture fixB){
				Body b1 = fixA.getBody();
				if(b1.getUserData() instanceof Entity) {
					Entity entity1 = (Entity) b1.getUserData();
					PhysicsComponent pc1 = entity1.getComponent(PhysicsComponent.class);
					RBUserData ud1 = pc1.getRBUserData(b1);
					if(ud1 != null && ud1.getType() != null) {
						System.out.println("Type Collision b1 " + ud1.getType().toString());
					}
				}

				Body b2 = fixB.getBody();
				if(b2.getUserData() instanceof Entity) {
					Entity entity2 = (Entity) b2.getUserData();
					PhysicsComponent pc2 = entity2.getComponent(PhysicsComponent.class);
					RBUserData ud2 = pc2.getRBUserData(b1);
					if(ud2 != null && ud2.getType() != null) {
						System.out.println("Type Collision b2" + ud2.getType().toString());
					}
				}
			}
		});
	}
}
