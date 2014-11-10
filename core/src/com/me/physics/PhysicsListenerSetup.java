package com.me.physics;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.gushikustudios.box2d.controllers.B2Controller;
import com.me.component.CrawlComponent;
import com.me.component.GrabComponent;
import com.me.component.HangComponent;
import com.me.component.JointComponent;
import com.me.component.LadderClimbComponent;
import com.me.component.MovementComponent;
import com.me.component.ParticleComponent;
import com.me.component.PhysicsComponent;
import com.me.component.PlayerComponent;
import com.me.component.PlayerOneComponent;
import com.me.component.PlayerTwoComponent;
import com.me.component.PushComponent;
import com.me.component.QueueComponent;
import com.me.component.QueueComponent.QueueType;
import com.me.component.TouchComponent;
import com.me.component.VelocityLimitComponent;
import com.me.component.PhysicsComponent.ImmediateModePhysicsListener;
import com.me.physics.RBUserData.Type;
import com.me.utils.Converters;

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

									Body b = other.getBody("box");
									QueueComponent queueComp = e1.getComponent(QueueComponent.class);
									queueComp.mass = 5f;
									queueComp.type = QueueType.MASS;
									//b.getFixtureList().get(0).setFriction(0.001f);
									//other.setMass(0.01f, "box");
									System.out.println("Box touch and friction" +b.getFixtureList().get(0).getFriction());
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
							b.getFixtureList().get(0).setFriction(20f);
							e1.getComponent(QueueComponent.class).mass = 20f;
							System.out.println("Box not touching and friction" +b.getFixtureList().get(0).getFriction());
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
		// TODO Auto-generated method stub
		pComp.setPhysicsListener(new ImmediateModePhysicsListener() {

			@Override
			public void preSolve(Entity e, Contact contact, boolean fixtureA) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onRestart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void endContact(Entity e, Contact contact, boolean fixtureA) {
				// TODO Auto-generated method stub
				Fixture fixA = contact.getFixtureA();
				Fixture fixB = contact.getFixtureB();

				if ((fixA.isSensor()) && (fixA.getUserData() != null))
				{
					B2Controller b2c = (B2Controller) fixA.getUserData();
					b2c.removeBody(fixB.getBody());
				}
				else if ((fixB.isSensor()) && (fixB.getUserData() != null))
				{
					B2Controller b2c = (B2Controller) fixB.getUserData();
					b2c.removeBody(fixA.getBody());
				}
			}

			@Override
			public void beginContact(Entity e, Contact contact, boolean fixtureA) {
				// TODO Auto-generated method stub
				Fixture fixA = contact.getFixtureA();
				Fixture fixB = contact.getFixtureB();

				if ((fixA.isSensor()) && (fixA.getUserData() != null))
				{
					B2Controller b2c = (B2Controller) fixA.getUserData();
					b2c.addBody(fixB.getBody());
				}
				else if ((fixB.isSensor()) && (fixB.getUserData() != null))
				{
					B2Controller b2c = (B2Controller) fixB.getUserData();
					b2c.addBody(fixA.getBody());
				}
			}
		});
	}
}
