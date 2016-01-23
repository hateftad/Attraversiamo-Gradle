package com.me.physics;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.ObjectMap;
import com.me.component.*;
import com.me.controllers.B2BuoyancyController;
import com.me.component.QueueComponent.QueueType;
import com.me.component.PhysicsComponent.ImmediateModePhysicsListener;
import com.me.events.GameEvent;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;
import com.me.level.Player;
import com.me.physics.RBUserData.Type;
import com.me.systems.GameEntityWorld;
import com.me.utils.Converters;

public class PhysicsListenerSetup {

    private GameEntityWorld m_gameEntityWorld;

    public PhysicsListenerSetup(GameEntityWorld world) {
        m_gameEntityWorld = world;
    }

    public void setPlayerPhysics(PhysicsComponent ps) {

        ps.setPhysicsListener(new ImmediateModePhysicsListener() {

            @Override
            public void preSolve(Entity e, Contact contact, boolean fixtureA) {

                Fixture fA = contact.getFixtureA();
                Fixture fB = contact.getFixtureB();
                Entity e2 = (Entity) fA.getBody().getUserData();
                PhysicsComponent player = e.getComponent(PhysicsComponent.class);
                PhysicsComponent other = e2.getComponent(PhysicsComponent.class);
                RBUserData otherUd = other.getRBUserData(fA.getBody());
                RBUserData playerUd = player.getRBUserData(fB.getBody());
                if (playerUd == null || otherUd == null)
                    return;

                if (contact.isTouching()) {
                    if (playerUd.getCollisionGroup() == otherUd.getCollisionGroup()) {
                        if (e.getComponent(PlayerComponent.class) != null) {
                            if (playerUd.getType() == Type.Feet && otherUd.getType() == Type.Box) {
                                if (!e.getComponent(KeyInputComponent.class).isMoving()) {
                                    other.setFriction(1f);
                                    contact.resetFriction();
                                } else {
                                    other.setFriction(0.001f);
                                    contact.resetFriction();
                                }
                            }
                        }
                    }
                }

            }

            @Override
            public void postSolve(Entity e, Contact contact, boolean fixtureA) {

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
                if (playerUd == null || otherUd == null)
                    return;

                if (fA.isSensor()) {

                    if (player.getRBUserData(fB.getBody()).getCollisionGroup() == otherUd.getCollisionGroup()) {
                        if (e.getComponent(PlayerComponent.class) != null) {
                            PlayerComponent pl = e.getComponent(PlayerComponent.class);
                            if (pl.isFacingLeft() && otherUd.getType() == Type.LeftEdge && playerUd.getType() == Type.HangHands) {
                                if (!pl.isHanging()) {
                                    JointComponent j = e.getComponent(JointComponent.class);
                                    j.createEdgeHang(other.getBody(), player.getBody("leftH"));
                                    player.setLinearVelocity(player.getLinearVelocity().x, 0);
                                    e.getComponent(TouchComponent.class).m_edgeTouch = true;
                                    e.getComponent(HangComponent.class).m_hangingLeft = true;
                                }
                            }
                            if (!pl.isFacingLeft() && otherUd.getType() == Type.RightEdge && playerUd.getType() == Type.HangHands) {
                                if (!pl.isHanging()) {
                                    JointComponent j = e.getComponent(JointComponent.class);
                                    j.createEdgeHang(other.getBody(), player.getBody("rightH"));
                                    player.setLinearVelocity(player.getLinearVelocity().x, 0);
                                    e.getComponent(TouchComponent.class).m_edgeTouch = true;
                                    e.getComponent(HangComponent.class).m_hangingRight = true;
                                }
                            }

                            if (pl.isFacingLeft() && otherUd.getType() == Type.PullLedge && playerUd.getType() == Type.HangHands) {
                                if (!pl.isHanging()) {
                                    JointComponent j = e.getComponent(JointComponent.class);
                                    j.createEdgeHang(other.getBody(), player.getBody("leftH"));
                                    player.setLinearVelocity(player.getLinearVelocity().x, 0);
                                    e.getComponent(TouchComponent.class).m_edgeTouch = true;
                                    e.getComponent(TouchComponent.class).m_pullEdgeTouch = true;
                                    e.getComponent(HangComponent.class).m_hangingLeft = true;
                                }
                            }
                            if (!pl.isFacingLeft() && otherUd.getType() == Type.PullLedge && playerUd.getType() == Type.HangHands) {
                                if (!pl.isHanging()) {
                                    JointComponent j = e.getComponent(JointComponent.class);
                                    j.createEdgeHang(other.getBody(), player.getBody("rightH"));
                                    player.setLinearVelocity(player.getLinearVelocity().x, 0);
                                    e.getComponent(TouchComponent.class).m_edgeTouch = true;
                                    e.getComponent(TouchComponent.class).m_pullEdgeTouch = true;
                                    e.getComponent(HangComponent.class).m_hangingRight = true;
                                }
                            }
                            if (otherUd.getType() == Type.RightLadder) {
                                if (!pl.isFacingLeft()) {
                                    e.getComponent(TouchComponent.class).m_ladderTouch = true;
                                    e.getComponent(LadderClimbComponent.class).m_rightClimb = true;
                                }
                            }
                            if (otherUd.getType() == Type.LeftLadder) {
                                if (pl.isFacingLeft()) {
                                    e.getComponent(TouchComponent.class).m_ladderTouch = true;
                                    e.getComponent(LadderClimbComponent.class).m_leftClimb = true;
                                }
                            }
                            if (otherUd.getType() == Type.BottomLadder) {
                                e.getComponent(LadderClimbComponent.class).m_bottomLadder = true;
                            }
                            if(otherUd.getType() == Type.ColorChangeSensor){
                                m_gameEntityWorld.onNotify(new TaskEvent(GameEventType.ColorSkin, e.getComponent(PlayerComponent.class).getPlayerNr()));
                            }
                            if (otherUd.getType() == Type.TopLadder) {
                                e.getComponent(LadderClimbComponent.class).m_topLadder = true;
                                e.getComponent(VelocityLimitComponent.class).m_ladderClimbVelocity = 0;
                            }

                            if (otherUd.getType() == Type.LeftPullup && fB.isSensor()) {
                                e.getComponent(TouchComponent.class).m_footEdgeL = true;
                                e.getComponent(TouchComponent.class).m_footEdge = true;
                                e.getComponent(TouchComponent.class).m_touchCenter = fA.getBody().getPosition().sub(0, 0.5f);
                            }
                            if (otherUd.getType() == Type.RightPullup && fB.isSensor()) {
                                e.getComponent(TouchComponent.class).m_footEdgeR = true;
                                e.getComponent(TouchComponent.class).m_footEdge = true;
                                e.getComponent(TouchComponent.class).m_touchCenter = fA.getBody().getPosition().sub(0, 0.5f);
                            }

                            if (otherUd.getType() == Type.LeftPushButton) {
                                e.getComponent(TouchComponent.class).m_pushArea = true;
                                e.getComponent(TouchComponent.class).m_leftPushArea = true;
                                e.getComponent(EventComponent.class).setEventInfo(other.getEventInfo());

                            }
                            if (otherUd.getType() == Type.RightPushButton) {
                                e.getComponent(TouchComponent.class).m_pushArea = true;
                                e.getComponent(TouchComponent.class).m_rightPushArea = true;
                                e.getComponent(EventComponent.class).setEventInfo(other.getEventInfo());
                            }

                            if (otherUd.getType() == Type.Hand && playerUd.getType() == Type.Hand) {

                                PlayerComponent girl = e.getComponent(PlayerComponent.class);
                                PlayerComponent man = e2.getComponent(PlayerComponent.class);
                                if (man.isFacingLeft() && girl.isFacingLeft() || !man.isFacingLeft() && !girl.isFacingLeft()) {

                                } else {
                                    if (e.getComponent(PlayerTwoComponent.class) != null) {
                                        if (girl.isJumping() && man.lyingDown()) {
                                            e.getComponent(TouchComponent.class).m_handTouch = true;
                                        }
                                    }
                                    if (e2.getComponent(PlayerOneComponent.class) != null) {
                                        if (man.lyingDown() && girl.isJumping()) {
                                            e2.getComponent(TouchComponent.class).m_handTouch = true;
                                        }
                                    }
                                    e.getComponent(GrabComponent.class).handPositionX = fA.getBody().getPosition().x;
                                }
                            }

                            if (otherUd.getType() == Type.RightHandHold) {
                                e2.getComponent(TouchComponent.class).m_handHoldArea = true;
                                e2.getComponent(TouchComponent.class).m_rightHoldArea = true;
                            }
                            if (playerUd.getType() == Type.RightHandHold) {
                                e.getComponent(TouchComponent.class).m_handHoldArea = true;
                                e.getComponent(TouchComponent.class).m_rightHoldArea = true;
                            }
                            if (otherUd.getType() == Type.LeftHandHold) {
                                e2.getComponent(TouchComponent.class).m_handHoldArea = true;
                                e2.getComponent(TouchComponent.class).m_leftHoldArea = true;
                            }
                            if (playerUd.getType() == Type.LeftHandHold) {
                                e.getComponent(TouchComponent.class).m_handHoldArea = true;
                                e.getComponent(TouchComponent.class).m_leftHoldArea = true;
                            }

                            if(otherUd.getType() == Type.InsideCage){
                                e.getComponent(PhysicsComponent.class).setFixedRotation("center", false);
                            }

                            if (otherUd.getType() == Type.LeftCrawl) {
                                e.getComponent(TouchComponent.class).m_canCrawl = true;
                            }
                            if (otherUd.getType() == Type.Portal) {
                                m_gameEntityWorld.onNotify(new TaskEvent(GameEventType.InsideFinishArea, e.getComponent(PlayerComponent.class).getPlayerNr()));
                            }
                            if (otherUd.getType() == Type.Finish) {
                                m_gameEntityWorld.onNotify(new TaskEvent(GameEventType.InsideFinishArea, e.getComponent(PlayerComponent.class).getPlayerNr()));
                            }
                            if(otherUd.getType() == Type.InsideCage){
                                e.getComponent(PhysicsComponent.class).setFixedRotation("center", true);
                            }
                            if(otherUd.getType() == Type.InsideCage){
                                e.getComponent(TouchComponent.class).m_cageTouch = true;
                            }
                        }
                    }
                }

                if (contact.isTouching()) {
                    Entity e1 = (Entity) fA.getBody().getUserData();

                    if (playerUd.getCollisionGroup() == otherUd.getCollisionGroup()) {

                        if (e.getComponent(PlayerComponent.class) != null) {
                            if (playerUd.getType() == Type.Feet && otherUd.getType() == Type.Ground) {
                                e.getComponent(SingleParticleComponent.class).setPosition(Converters.ToWorld(e.getComponent(PhysicsComponent.class).getBody("feet").getPosition()));
                                GameEvent event = e.getComponent(PhysicsComponent.class).getEventInfo();
                                event.notify(m_gameEntityWorld);
                            }

                            if (playerUd.getType() == Type.Torso && otherUd.getType() == Type.Box) {
                                if (e.getComponent(FeetComponent.class).hasCollided()) {
                                    QueueComponent queueComp = e1.getComponent(QueueComponent.class);
                                    queueComp.mass = 5f;
                                    queueComp.type = QueueType.Mass;
                                    queueComp.bodyName = "box";
                                    e.getComponent(TouchComponent.class).m_boxTouch = true;
                                    if (e.getComponent(PushComponent.class) != null) {
                                        if (e.getComponent(PlayerComponent.class).isFacingLeft()) {
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

                if (fA.isSensor() || fB.isSensor() && !contact.isTouching()) {
                    Entity e1 = (Entity) fA.getBody().getUserData();
                    Entity e2 = (Entity) fB.getBody().getUserData();
                    PhysicsComponent other = e1.getComponent(PhysicsComponent.class);
                    PhysicsComponent player = e2.getComponent(PhysicsComponent.class);

                    RBUserData otherUd = other.getRBUserData(fA.getBody());
                    RBUserData playerUd = player.getRBUserData(fB.getBody());
                    if (playerUd.getCollisionGroup() == otherUd.getCollisionGroup()) {
                        if (otherUd.getType() == Type.RightLadder) {
                            e.getComponent(TouchComponent.class).m_ladderTouch = false;
                            e.getComponent(LadderClimbComponent.class).m_rightClimb = false;
                        }
                        if (otherUd.getType() == Type.LeftLadder) {
                            e.getComponent(TouchComponent.class).m_ladderTouch = false;
                            e.getComponent(LadderClimbComponent.class).m_leftClimb = false;
                        }
                        if (otherUd.getType() == Type.BottomLadder) {
                            e.getComponent(LadderClimbComponent.class).m_bottomLadder = false;
                        }
                        if (otherUd.getType() == Type.TopLadder) {
                            e.getComponent(LadderClimbComponent.class).m_topLadder = false;
                        }
                        if (otherUd.getType() == Type.LeftPullup && fB.isSensor()) {
                            e.getComponent(TouchComponent.class).m_footEdgeL = false;
                            e.getComponent(TouchComponent.class).m_footEdge = false;
                        }
                        if (otherUd.getType() == Type.RightPullup && fB.isSensor()) {
                            e.getComponent(TouchComponent.class).m_footEdgeR = false;
                            e.getComponent(TouchComponent.class).m_footEdge = false;
                        }
                        if (otherUd.getType() == Type.Hand && playerUd.getType() == Type.Hand) {

                        }
                        if(otherUd.getType() == Type.ColorChangeSensor){
                            m_gameEntityWorld.onNotify(new TaskEvent(GameEventType.BlackSkin, e.getComponent(PlayerComponent.class).getPlayerNr()));
                        }
                        if (otherUd.getType() == Type.LeftCrawl) {
                            e.getComponent(TouchComponent.class).m_canCrawl = false;
                        }
                        if (otherUd.getType() == Type.Portal) {
                            m_gameEntityWorld.onNotify(new TaskEvent(GameEventType.OutsideFinishArea, e.getComponent(PlayerComponent.class).getPlayerNr()));
                        }
                        if (otherUd.getType() == Type.Finish) {
                            m_gameEntityWorld.onNotify(new TaskEvent(GameEventType.OutsideFinishArea, e.getComponent(PlayerComponent.class).getPlayerNr()));
                        }
                        if (otherUd.getType() == Type.LeftPushButton) {
                            e.getComponent(TouchComponent.class).m_pushArea = false;
                            e.getComponent(TouchComponent.class).m_leftPushArea = false;
                        }
                        if (otherUd.getType() == Type.RightPushButton) {
                            e.getComponent(TouchComponent.class).m_pushArea = false;
                            e.getComponent(TouchComponent.class).m_rightPushArea = false;
                        }

                        if (playerUd.getType() == Type.LeftHandHold) {
                            e.getComponent(TouchComponent.class).m_handHoldArea = false;
                            e.getComponent(TouchComponent.class).m_leftHoldArea = false;
                            e.getComponent(HandHoldComponent.class).setHoldingHands(false);
                            m_gameEntityWorld.onNotify(new TaskEvent(GameEventType.HandHoldingEnded));
                        }
                        if (playerUd.getType() == Type.RightHandHold) {
                            e.getComponent(TouchComponent.class).m_handHoldArea = false;
                            e.getComponent(TouchComponent.class).m_rightHoldArea = false;
                            e.getComponent(HandHoldComponent.class).setHoldingHands(false);
                            m_gameEntityWorld.onNotify(new TaskEvent(GameEventType.HandHoldingEnded));
                        }

                        if (otherUd.getType() == Type.LeftHandHold) {
                            e2.getComponent(TouchComponent.class).m_handHoldArea = false;
                            e2.getComponent(TouchComponent.class).m_leftHoldArea = false;
                            e2.getComponent(HandHoldComponent.class).setHoldingHands(false);
                            m_gameEntityWorld.onNotify(new TaskEvent(GameEventType.HandHoldingEnded));
                        }
                        if (otherUd.getType() == Type.RightHandHold) {
                            e2.getComponent(TouchComponent.class).m_handHoldArea = false;
                            e2.getComponent(TouchComponent.class).m_rightHoldArea = false;
                            e2.getComponent(HandHoldComponent.class).setHoldingHands(false);
                            m_gameEntityWorld.onNotify(new TaskEvent(GameEventType.HandHoldingEnded));
                        }
                        if (otherUd.getType() == Type.RightEdge && playerUd.getType() == Type.HangHands) {
                            e.getComponent(TouchComponent.class).m_edgeTouch = false;
                        }
                        if (otherUd.getType() == Type.LeftEdge && playerUd.getType() == Type.HangHands) {
                            e.getComponent(TouchComponent.class).m_edgeTouch = false;
                        }
                        if (otherUd.getType() == Type.PullLedge && playerUd.getType() == Type.HangHands) {
                            e.getComponent(TouchComponent.class).m_edgeTouch = false;
                            e.getComponent(TouchComponent.class).m_pullEdgeTouch = false;
                        }
                        if (otherUd.getType() == Type.PullLedge && playerUd.getType() == Type.HangHands) {
                            e.getComponent(TouchComponent.class).m_edgeTouch = false;
                            e.getComponent(TouchComponent.class).m_pullEdgeTouch = false;
                        }
                        if(otherUd.getType() == Type.InsideCage){
                            e.getComponent(PhysicsComponent.class).setFixedRotation("center", false);
                        }
                        if(otherUd.getType() == Type.InsideCage){
                            e.getComponent(TouchComponent.class).m_cageTouch = false;
                        }
                    }
                }

                if (!contact.isTouching()) {
                    Entity e1 = (Entity) fA.getBody().getUserData();
                    Entity e2 = (Entity) fB.getBody().getUserData();
                    PhysicsComponent other = e1.getComponent(PhysicsComponent.class);
                    PhysicsComponent player = e2.getComponent(PhysicsComponent.class);

                    RBUserData otherUd = other.getRBUserData(fA.getBody());
                    RBUserData playerUd = player.getRBUserData(fB.getBody());
                    if (e.getComponent(PlayerComponent.class) != null) {
                        if (playerUd.getType() == Type.Torso && otherUd.getType() == Type.Box) {

                            Body b = other.getBody("box");
                            b.getFixtureList().get(0).setFriction(PhysicsComponent.HIGH_FRICTION);
                            e1.getComponent(QueueComponent.class).mass = 20f;
                            e.getComponent(TouchComponent.class).m_boxTouch = false;
                            if (e.getComponent(PushComponent.class) != null) {
                                e.getComponent(PushComponent.class).m_pushLeft = false;
                                e.getComponent(PushComponent.class).m_pushRight = false;
                            }
                        }
                    }
                }
            }

            @Override
            public void onRestart() {
            }
        });
    }

    public void setLevelPhysics(PhysicsComponent pComp) {

        pComp.setPhysicsListener(new ImmediateModePhysicsListener() {

            @Override
            public void preSolve(Entity e, Contact contact, boolean fixtureA) {

            }

            @Override
            public void postSolve(Entity e, Contact contact, boolean fixtureA) {

            }

            @Override
            public void onRestart() {

            }

            @Override
            public void endContact(Entity e, Contact contact, boolean fixtureA) {

                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();

                if ((fixA.isSensor()) && (fixA.getUserData() != null)) {
                    determineController((ObjectMap) fixA.getUserData(), fixB.getBody(), false);
                    //treatBouyancy(fixB.getBody(), false);
                } else if ((fixB.isSensor()) && (fixB.getUserData() != null)) {
                    determineController((ObjectMap) fixB.getUserData(), fixA.getBody(), false);
                    //treatBouyancy(fixA.getBody(), false);
                }
            }

            @Override
            public void beginContact(Entity e, Contact contact, boolean fixtureA) {
                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();
                //printBodies(fixA, fixB);
                if ((fixA.isSensor()) && (fixA.getUserData() != null)) {
                    determineController((ObjectMap) fixA.getUserData(), fixB.getBody(), true);
                    treatBouyancy(fixB.getBody(), true);
                } else if ((fixB.isSensor()) && (fixB.getUserData() != null)) {
                    determineController((ObjectMap) fixB.getUserData(), fixA.getBody(), true);
                    treatBouyancy(fixB.getBody(), true);
                }
            }

            private void determineController(ObjectMap<String, B2BuoyancyController> controllerMap, Body body, boolean add) {
                Entity entity = (Entity) body.getUserData();
                B2BuoyancyController b2c;
                if (entity.getComponent(PlayerOneComponent.class) != null) {
                    b2c = controllerMap.get(PlayerOneComponent.PlayerOne);
                } else if (entity.getComponent(PlayerTwoComponent.class) != null) {
                    b2c = controllerMap.get(PlayerTwoComponent.PlayerTwo);
                } else {
                    b2c = controllerMap.get(WorldObjectComponent.WorldObject);
                }
                if (add) {
                    b2c.addBody(body);
                } else {
                    b2c.removeBody(body);
                }
            }

            private void treatBouyancy(Body body, boolean submerged) {
                Entity entity = (Entity) body.getUserData();
                PhysicsComponent ps = entity.getComponent(PhysicsComponent.class);
                RBUserData otherUd = ps.getRBUserData(body);

                if (otherUd.getType() == Type.Feet && entity.getComponent(PlayerTwoComponent.class) != null) {
                    QueueComponent queueComp = new QueueComponent();
                    if (submerged) {
                        queueComp.mass = 1.6f;
                        queueComp.type = QueueType.TempMass;
                        queueComp.bodyName = "feet";
                    } else {
                        queueComp.mass = 1f;
                        queueComp.type = QueueType.TempMass;
                        queueComp.bodyName = "feet";
                    }
                    entity.addComponent(queueComp);
                }
                if (otherUd.getType() == Type.Pelvis) {
                    System.out.println("Under Water");
                }
            }

            private void printBodies(Fixture fixA, Fixture fixB) {
                Body b1 = fixA.getBody();
                if (b1.getUserData() instanceof Entity) {
                    Entity entity1 = (Entity) b1.getUserData();
                    PhysicsComponent pc1 = entity1.getComponent(PhysicsComponent.class);
                    RBUserData ud1 = pc1.getRBUserData(b1);
                    if (ud1 != null && ud1.getType() != null) {
                        System.out.println("Type Collision b1 " + ud1.getType().toString());
                    }
                }

                Body b2 = fixB.getBody();
                if (b2.getUserData() instanceof Entity) {
                    Entity entity2 = (Entity) b2.getUserData();
                    PhysicsComponent pc2 = entity2.getComponent(PhysicsComponent.class);
                    RBUserData ud2 = pc2.getRBUserData(b1);
                    if (ud2 != null && ud2.getType() != null) {
                        System.out.println("Type Collision b2" + ud2.getType().toString());
                    }
                }
            }
        });
    }
}
