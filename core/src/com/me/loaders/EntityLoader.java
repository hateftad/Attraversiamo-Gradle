package com.me.loaders;

import java.util.WeakHashMap;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.*;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationStateData;
import com.me.level.Player;
import com.me.component.*;
import com.me.component.ParticleComponent.ParticleType;
import com.me.component.AnimationComponent.AnimState;
import com.me.factory.GameEventFactory;
import com.me.level.Level;
import com.me.loaders.BodySerializer.BodyUserData;
import com.me.loaders.RubeScene.Indexes;
import com.me.physics.JointFactory;
import com.me.physics.PhysicsListenerSetup;
import com.me.physics.RBUserData;
import com.me.systems.CameraSystem;
import com.me.systems.GameEntityWorld;
import com.me.utils.Converters;

public class EntityLoader {

    private RubeScene m_scene;
    private RubeSceneLoader m_loader;
    private WeakHashMap<String, Texture> m_textureMap;
    private static final String LVLPATH = "data/level/";
    private static final String CHARPATH = "data/character/";

    public EntityLoader() {
        m_textureMap = new WeakHashMap<String, Texture>();
        m_loader = new RubeSceneLoader();
    }

    private void clearLoader() {

        if (m_scene != null) {
            m_scene.clear();
        }
        if (m_textureMap.size() > 0) {
            m_textureMap.clear();
        }
    }

    public void dispose() {
        m_scene.dispose();
        m_scene.clear();
        for (Texture t : m_textureMap.values()) {
            t.dispose();
        }
        m_textureMap.clear();

    }

    public void loadLevel(Level level, GameEntityWorld entityWorld,
                          World physicsWorld, RayHandler rh) {
        String levelDirectory = level.getLevelName();
        clearLoader();

        m_scene = m_loader.loadScene(Gdx.files.internal("data/level/"
                + levelDirectory + "/" + levelDirectory + ".json"));
        Array<Body> bodies = m_scene.getBodies();

        Vector2 bodyPos = new Vector2();
        Vector2 tmp = new Vector2();
        PhysicsComponent pComp = null;
        SpriteComponent sComp;
        Entity entity = null;
        RubeImage image;
        Array<Body> tempList = new Array<Body>();

        for (int i = 0; i < bodies.size; i++) {

            Body body = bodies.get(i);

            Array<RubeImage> images = m_scene.getMappedImage(body);
            if (images != null) {

                bodyPos.set(body.getPosition());
                image = images.get(0);
                String tName = image.file;

                if (tName != null) {
                    tmp.set(image.width, image.height);
                    String textureFileName;
                    if (!tName.contains("common")) {
                        textureFileName = LVLPATH + levelDirectory + "/"
                                + tName;
                    } else {
                        tName = tName.substring(tName.indexOf("/") + 1);
                        textureFileName = LVLPATH + tName;
                    }
                    System.out.println(textureFileName);
                    Texture texture = m_textureMap.get(textureFileName);
                    if (texture == null) {
                        texture = new Texture(textureFileName);
                        texture.setFilter(Texture.TextureFilter.Linear,
                                Texture.TextureFilter.Nearest);
                        m_textureMap.put(textureFileName, texture);
                    }
                    if (image.body != null) {
                        sComp = new SpriteComponent(texture, image.flip,
                                image.body, image.color, tmp, image.center,
                                image.angleInRads * MathUtils.radiansToDegrees,
                                image.renderOrder);

                        pComp = new PhysicsComponent(physicsWorld, body,
                                ((BodyUserData) body.getUserData()).mName);
                        entity = entityWorld.createEntity();
                        entity.addComponent(pComp);
                        sComp.m_shouldDraw = m_scene.getCustom(image, "shouldDraw", true);
                        entity.addComponent(sComp);
                    }
                }

            } else {
                pComp = new PhysicsComponent(physicsWorld, body,
                        ((BodyUserData) body.getUserData()).mName);
                entity = entityWorld.createEntity();
                entity.addComponent(pComp);
            }
            if (m_scene.getCustom(body, "bodyType", "").equalsIgnoreCase("light")) {
                ConeLight light = new ConeLight(rh, 50, Color.GREEN, 500, Converters.ToWorld(body.getPosition().x), Converters.ToWorld(body.getPosition().y), 180, 180);
                entity.addComponent(new LightComponent(light, ((BodyUserData) body.getUserData()).mName));
                entity.addComponent(new TriggerComponent());
                entityWorld.getManager(GroupManager.class).add(entity, "lights");
            }

            if (m_scene.getCustom(body, "bodyType", "").equalsIgnoreCase("skyLight")) {
                CameraComponent camComp = entityWorld.getSystem(CameraSystem.class).getCameraComponent();
                entity.addComponent(camComp);
                PointLight light = new PointLight(rh, 50, level.getLevelConfig().getLightColor(), 5000, camComp.getCamera().position.x, camComp.getCamera().position.y);
                entity.addComponent(new LightComponent(light, "cameraLight"));
                entityWorld.getManager(GroupManager.class).add(entity, "lights");
            }
            if (m_scene.getCustom(body, "bodyType", "").equalsIgnoreCase("behindLight")) {

                // entity.addComponent(component);
            }

            loadFixtures(pComp, body);

            BodyUserData ud = (BodyUserData) body.getUserData();
            if (ud.mName.equalsIgnoreCase("box")) {
                pComp.setMass(20f, ud.mName);
                pComp.setFriction(20.0f);
                entity.addComponent(new RestartComponent())
                        .addComponent(new QueueComponent());
            }
            if (ud.mName.equalsIgnoreCase("portal")) {
                SingleParticleComponent particleComponent = new SingleParticleComponent("fire", ParticleType.PORTAL);
                entityWorld.addObserver(particleComponent);
                entity.addComponent(particleComponent);
                entity.addComponent(new TriggerComponent());
                ReachEndComponent reachEndComponent = new ReachEndComponent(level.getNumberOfFinishers());
                LevelComponent levelComponent = new LevelComponent();
                entityWorld.addObserver(reachEndComponent);
                entityWorld.addObserver(levelComponent);
                entity.addComponent(levelComponent);
                entity.addComponent(reachEndComponent);
            }
            if (ud.mName.equalsIgnoreCase("finish")) {
                entity.addComponent(new TriggerComponent());
                ReachEndComponent reachEndComponent = new ReachEndComponent(level.getNumberOfFinishers());
                LevelComponent levelComponent = new LevelComponent();
                entityWorld.addObserver(reachEndComponent);
                entityWorld.addObserver(levelComponent);
                entity.addComponent(levelComponent);
                entity.addComponent(reachEndComponent);
            }
            if (ud.mName.equalsIgnoreCase("point")) {
                //entity.addComponent(new ParticleComponent("point", ParticleType.PICKUP, 1));
                //entity.addComponent(new TriggerComponent());
            }
            if (ud.mName.equalsIgnoreCase("minX")) {
                level.getLevelBoundaries().minX = Converters.ToWorld(body.getPosition().x);
                //System.out.println("Minx "+ Converters.ToWorld(body.getPosition().x));
            }
            if (ud.mName.equalsIgnoreCase("maxX")) {
                level.getLevelBoundaries().maxX = Converters.ToWorld(body.getPosition().x);
                //System.out.println("MaxX "+ Converters.ToWorld(body.getPosition().x));
            }
            if (ud.mName.equalsIgnoreCase("minY")) {
                level.getLevelBoundaries().minY = Converters.ToWorld(body.getPosition().y);
                //System.out.println("MinY " + Converters.ToWorld(body.getPosition().y));
            }

            if (ud.mName.equalsIgnoreCase("water")) {
                int eventId = m_scene.getCustom(body, "taskId", 0);
                //pass in fluid velocity
                BuoyancyComponent buoyancyComponent = new BuoyancyComponent(eventId);
                buoyancyComponent.addControllerInfo(PlayerOneComponent.PlayerOne, new Vector2(0, 3), 1.5f, 2);
                buoyancyComponent.addControllerInfo(PlayerTwoComponent.PlayerTwo, new Vector2(0, 1), 1.5f, 2);
                buoyancyComponent.addControllerInfo(WorldObjectComponent.WorldObject, new Vector2(0, 1), 1.5f, 2);
                entityWorld.addObserver(buoyancyComponent);
                entity.addComponent(buoyancyComponent);
                //entity.addComponent(new ShaderComponent("",body));
                entity.addComponent(new TriggerComponent());

            }

            if (ud.mName.equalsIgnoreCase("particleEmitter")) {
                String particleName = m_scene.getCustom(body, "particlename", "");
                ContinuousParticles particleComponent = new ContinuousParticles(particleName, body.getPosition());
                entity.addComponent(particleComponent);
                entityWorld.addObserver(particleComponent);
            }

            if (ud.mName.equalsIgnoreCase("bodyInfo")) {
                GameEventFactory factory = new GameEventFactory();
                pComp.setTaskInfo(factory.createFromBodyInfo(m_scene, body));
            }

            if(ud.mName.equalsIgnoreCase("player_position")){
                int player = m_scene.getCustom(body, "playerNr", -1);
                level.addPlayerPosition(player, body.getPosition());
            }

            pComp.setRBUserData(pComp.getBody(ud.mName), new RBUserData(ud.mBoxIndex, ud.mCollisionGroup, ud.mtaskId, pComp.getBody(ud.mName)));
            pComp.setUserData(entity, ((BodyUserData) body.getUserData()).mName);
            tempList.add(pComp.getBody(ud.mName));
            entity.addToWorld();
            entityWorld.getManager(GroupManager.class).add(entity, "worldObjects");
        }

        loadBodyJoints(physicsWorld, tempList, entityWorld);
        tempList.clear();
    }

    public Entity loadCharacter(Player playerConfig, GameEntityWorld entityWorld, World physicsWorld) {
        String characterPath = playerConfig.getName() + "/";
        String characterName = playerConfig.getName();

        clearLoader();

        m_scene = m_loader.loadScene(Gdx.files.internal(CHARPATH
                + characterPath + "/" + characterName + ".json"));
        Array<Body> bodies = m_scene.getBodies();

        Vector2 bodyPos = new Vector2();
        Vector2 tmp = new Vector2();
        PhysicsComponent pComp = null;
        SpriteComponent sComp = null;
        Entity entity = entityWorld.createEntity();
        RubeImage image = null;
        Array<Body> tempList = new Array<Body>();

        for (int i = 0; i < bodies.size; i++) {

            Body body = bodies.get(i);

            Array<RubeImage> images = m_scene.getMappedImage(body);
            if (images != null) {

                bodyPos.set(body.getPosition());
                image = images.get(0);
                String tName = image.file;
                if (tName != null) {
                    tmp.set(image.width, image.height);
                    String textureFileName = CHARPATH + characterPath + tName;
                    Texture texture = m_textureMap.get(textureFileName);
                    if (texture == null) {
                        texture = new Texture(textureFileName);
                        texture.setFilter(Texture.TextureFilter.Linear,
                                Texture.TextureFilter.Nearest);
                        m_textureMap.put(textureFileName, texture);
                    }
                    if (image.body != null) {
                        sComp = new SpriteComponent(texture, image.flip,
                                image.body, image.color, tmp, image.center,
                                image.angleInRads * MathUtils.radiansToDegrees,
                                image.renderOrder);
                        if (pComp != null) {
                            pComp.addBody(physicsWorld, body,
                                    ((BodyUserData) body.getUserData()).mName);
                        } else {
                            pComp = new PhysicsComponent(physicsWorld, body,
                                    ((BodyUserData) body.getUserData()).mName);
                            entity.addComponent(pComp);
                        }
                        entity.addComponent(sComp);
                    }
                }
            } else {
                if (m_scene.getCustom(body, "characterType", "").equalsIgnoreCase("leg")) {
                    String name = ((BodyUserData) body.getUserData()).mName;
                    if (pComp != null) {
                        pComp.addBody(physicsWorld, body, name);
                    } else {
                        pComp = new PhysicsComponent(physicsWorld, body, name);
                        pComp.setMass(0.001f, name);
                        entity.addComponent(pComp);
                    }

                    FeetComponent feetComponent = new FeetComponent(name);
                    entity.addComponent(feetComponent);

                } else if (m_scene.getCustom(body, "characterType", "").equalsIgnoreCase(
                        "hand")) {
                    if (pComp != null)
                        pComp.addBody(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
                    else {
                        pComp = new PhysicsComponent(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
                        entity.addComponent(pComp);
                    }
                } else if (m_scene.getCustom(body, "characterType", "").equalsIgnoreCase("LHand")) {
                    if (pComp != null) {
                        pComp.addBody(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
                        pComp.setMass(0.01f, ((BodyUserData) body.getUserData()).mName);
                    } else {
                        pComp = new PhysicsComponent(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
                        entity.addComponent(pComp);
                    }
                } else {
                    if (pComp != null)
                        pComp.addBody(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
                    else {
                        pComp = new PhysicsComponent(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
                        entity.addComponent(pComp);
                    }
                }
            }

            loadFixtures(pComp, body);

            String skelName = m_scene.getCustom(body, "skeleton", "failed");
            String atlasName = m_scene.getCustom(body, "atlas", "failed");
            PlayerAnimationComponent animationComponent = null;
            AnimationStateData stateData;
            if (!skelName.equalsIgnoreCase("failed") && !atlasName.equalsIgnoreCase("failed")) {
                animationComponent = new PlayerAnimationComponent(CHARPATH + characterPath
                        + atlasName, CHARPATH + characterPath + skelName, 1.3f, playerConfig.getFinishAnimation());
                entity.addComponent(animationComponent);
                entityWorld.addObserver(animationComponent);
            }
            if (m_scene.getCustom(body, "characterType", "").equalsIgnoreCase("player_one")) {

                PlayerComponent playerComponent = new PlayerComponent(m_scene.getCustom(body, "characterType", ""), playerConfig.isFinishFacingLeft());
                playerComponent.setActive(playerConfig.isActive());
                playerComponent.setFacingLeft(playerConfig.isFacingLeft());
                playerComponent.setCanBecomeInactive(playerConfig.canDeactivate());
                entityWorld.addObserver(playerComponent);
                // entity.addComponent(new LightComponent(light, ((BodyUserData)
                // body.getUserData()).mName));
                entity.addComponent(playerComponent);
                entity.addComponent(new TouchComponent());
                entity.addComponent(new MovementComponent());
                entity.addComponent(new JointComponent("noname"));
                entity.addComponent(new HangComponent());
                entity.addComponent(new RagDollComponent());
                entity.addComponent(new LadderClimbComponent());
                entity.addComponent(new VelocityLimitComponent(12, 14, 5, 5));
                entity.addComponent(new PushComponent());
                entity.addComponent(new JumpComponent());
                entity.addComponent(new GrabComponent());
                entity.addComponent(new PlayerOneComponent());
                entity.addComponent(new TriggerComponent());
                entity.addComponent(new RestartComponent());
                entity.addComponent(new EventComponent());

                HandHoldComponent handHoldComponent = new HandHoldComponent();
                entity.addComponent(handHoldComponent);
                entityWorld.addObserver(handHoldComponent);

                SingleParticleComponent particleComponent = new SingleParticleComponent("smoke", 1);
                entity.addComponent(particleComponent);
                entityWorld.addObserver(particleComponent);

                pComp.setName(((BodyUserData) body.getUserData()).mName);
                pComp.setIsPlayer(true);
                stateData = animationComponent.setUp(image);
                animationComponent.setAnimationState(AnimState.IDLE);
                stateData.setMix("idle1", "jogging", 0.4f);
                stateData.setMix("jogging", "idle1", 0.4f);
                stateData.setMix("running", "idle1", 0.4f);
                stateData.setMix("runJumping", "running", 0.6f);
                stateData.setMix("jogging", "running", 0.4f);
                stateData.setMix("runJumping", "running", 0.1f);
                stateData.setMix("upJump", "running", 0.2f);
                stateData.setMix("idle1", "climbUp", 0.6f);
                stateData.setMix("jogging", "pushing", 0.5f);
                stateData.setMix("idle1", "pushing", 0.4f);
                stateData.setMix("running", "pushing", 0.3f);
                stateData.setMix("runJumping", "falling", 0.4f);
                stateData.setMix("pushing", "idle1", 0.4f);
//                stateData.setMix("upJump", "idle1", 0.7f);
                // dstateData.setMix("pushing", "idle", 0.6f);
                // stateData.setMix("ladderHang", "running", 0.1f);
                animationComponent.setSkin(playerConfig.getSkinName());
                //pComp.setPosition();
                // stateData.setMix("lieDown", "running", 0.3f);
                pComp.setAllBodiesPosition(playerConfig.getPosition());

            } else if (m_scene.getCustom(body, "characterType", "").equalsIgnoreCase("player_two")) {
                PlayerComponent playerComponent = new PlayerComponent(m_scene.getCustom(body, "characterType", ""), playerConfig.isFinishFacingLeft());
                playerComponent.setActive(playerConfig.isActive());
                playerComponent.setFacingLeft(playerConfig.isFacingLeft());
                playerComponent.setCanBecomeInactive(playerConfig.canDeactivate());
                entityWorld.addObserver(playerComponent);

                pComp.setName(((BodyUserData) body.getUserData()).mName);
                pComp.setMass(0.001f, ((BodyUserData) body.getUserData()).mName);
                pComp.setIsPlayer(true);
                stateData = animationComponent.setUp(image);
                animationComponent.setAnimationState(AnimState.IDLE);
                stateData.setMix("idle1", "walking", 0.4f);
                stateData.setMix("running", "idle1", 0.4f);
                stateData.setMix("walking", "idle1", 0.4f);
                stateData.setMix("walking", "running", 0.4f);
                stateData.setMix("running", "falling", 0.6f);
                stateData.setMix("idle1", "falling", 0.6f);
                stateData.setMix("running", "falling", 0.6f);
                stateData.setMix("walking", "falling", 0.6f);
                stateData.setMix("idle1", "pushing", 0.6f);
                stateData.setMix("running", "pushing", 0.6f);
                stateData.setMix("lieDown", "lyingDown", 0.2f);
                stateData.setMix("crawling", "lyingDown", 0.2f);
                stateData.setMix("lyingDown", "crawling", 0.2f);
                stateData.setMix("standUp", "idle1", 0.2f);
                stateData.setMix("lyingDown", "standUp", 0.2f);
                entity.addComponent(playerComponent);
                animationComponent.setSkin(playerConfig.getSkinName());
                entity.addComponent(new MovementComponent());
                entity.addComponent(new VelocityLimitComponent(8.5f, 10, 2.5f));
                entity.addComponent(new TouchComponent());
                entity.addComponent(new JumpComponent());
                entity.addComponent(new GrabComponent());
                entity.addComponent(new PlayerTwoComponent());
                entity.addComponent(new TriggerComponent());
                entity.addComponent(new CrawlComponent());
                entity.addComponent(new RestartComponent());
                entity.addComponent(new PushComponent());
                entity.addComponent(new QueueComponent());
                entity.addComponent(new EventComponent());


                HandHoldComponent handHoldComponent = new HandHoldComponent();
                entity.addComponent(handHoldComponent);
                entityWorld.addObserver(handHoldComponent);

                SingleParticleComponent particleComponent = new SingleParticleComponent("smoke", 2);
                entity.addComponent(particleComponent);
                entityWorld.addObserver(particleComponent);

                pComp.setAllBodiesPosition(playerConfig.getPosition());
            }

            BodyUserData ud = (BodyUserData) body.getUserData();
            pComp.setRBUserData(pComp.getBody(ud.mName), new RBUserData(ud.mBoxIndex, ud.mCollisionGroup, ud.mtaskId, pComp.getBody(ud.mName)));
            pComp.setUserData(entity, ud.mName);
            tempList.add(pComp.getBody(ud.mName));

            if (ud.mName.equalsIgnoreCase("feet")) {
                GameEventFactory factory = new GameEventFactory();
                pComp.setTaskInfo(factory.createFromBodyInfo(m_scene, body));
            }

            if (ud.mName.equalsIgnoreCase("right_hand_hold")) {
                pComp.setMass(PhysicsComponent.LOW_MASS, ud.mName);
            }
            if (ud.mName.equalsIgnoreCase("left_hand_hold")) {
                pComp.setMass(PhysicsComponent.LOW_MASS, ud.mName);
            }

        }
        Array<Joint> joints = m_scene.getJoints();
        if (joints != null && joints.size > 0) {
            for (int i = 0; i < joints.size; i++) {
                Joint joint = joints.get(i);
                Indexes ind = m_scene.getJointBodyIndex(i);
                attachToEntity(joint, ind, tempList, entity, physicsWorld, entityWorld);
            }
        }
        // loadBodyParts(entity, ps);
        PhysicsListenerSetup setup = new PhysicsListenerSetup(entityWorld);
        setup.setPlayerPhysics(pComp);
        tempList.clear();
        entity.addToWorld();
        entityWorld.getManager(GroupManager.class).add(entity, "players");
        return entity;
    }

    private void loadBodyJoints(
            World physicsWorld,
            Array<Body> tempList, GameEntityWorld entityWorld) {
        Array<Joint> joints = m_scene.getJoints();
        if (joints == null)
            return;
        for (int j = 0; j < joints.size; j++) {

            Joint joint = joints.get(j);
            Indexes ind = m_scene.getJointBodyIndex(j);
            attachToEntity(joint, ind, tempList, entityWorld.createEntity(), physicsWorld, entityWorld);
        }
    }

    private void loadFixtures(PhysicsComponent pComp, Body body) {
        Array<Fixture> fixtures = body.getFixtureList();

        if ((fixtures != null) && (fixtures.size > 0)) {
            // for each fixture on the body...
            for (int j = 0; j < fixtures.size; j++) {
                Fixture fixture = fixtures.get(j);
                pComp.createFixture(fixture, ((BodyUserData) body.getUserData()).mName);
            }
        }
    }

    private void attachToEntity(Joint joint, Indexes ind, Array<Body> tempList, Entity entity, World physicsWorld, GameEntityWorld gameEntityWorld) {

        if (joint.getType() == JointType.DistanceJoint) {

            DistanceJointDef jDef = (DistanceJointDef) ind.jointDef;
            if (joint.getUserData() != null) {
                JointFactory.getInstance().createJoint(
                        tempList.get(ind.first), tempList.get(ind.second),
                        jDef, physicsWorld);
            }
        }

        if (joint.getType() == JointType.WheelJoint) {
        }

        if (joint.getType() == JointType.RevoluteJoint) {

            RevoluteJointDef jDef = (RevoluteJointDef) ind.jointDef;
            if (joint.getUserData() != null) {
                String name = (String) joint.getUserData();
                if (name.equals("footMotor")) {
                    CharacterMovementComponent comp = new CharacterMovementComponent(JointFactory.getInstance().createJoint(
                            tempList.get(ind.first),
                            tempList.get(ind.second), jDef, physicsWorld));
                    entity.addComponent(comp);
                    gameEntityWorld.addObserver(comp);
                } else if (name.equals("waterEngine")) {
                    int taskId = m_scene.getCustom(joint, "taskId", 0);
                    int taskFinishers = m_scene.getCustom(joint, "taskFinishers", 0);
                    TwoWayEngineComponent engineComponent = new TwoWayEngineComponent(taskId, JointFactory.getInstance().createJoint(
                            tempList.get(ind.first),
                            tempList.get(ind.second), jDef, physicsWorld));
                    entity.addComponent(engineComponent);
                    gameEntityWorld.addObserver(engineComponent);
                } else {
                    JointFactory.getInstance().createJoint(tempList.get(ind.first),
                            tempList.get(ind.second), jDef, physicsWorld);
                }
            } else {
                JointFactory.getInstance().createJoint(tempList.get(ind.first),
                        tempList.get(ind.second), jDef, physicsWorld);
            }
        }

        if (joint.getType() == JointType.WheelJoint) {
            WheelJointDef jDef = (WheelJointDef) ind.jointDef;
            if (joint.getUserData() != null) {
                String name = (String) joint.getUserData();
                if (name.equals("footMotor")) {
                    CharacterMovementComponent comp = new CharacterMovementComponent(JointFactory.getInstance().createJoint(
                            tempList.get(ind.first),
                            tempList.get(ind.second), jDef, physicsWorld));
                    entity.addComponent(comp);
                    gameEntityWorld.addObserver(comp);
                } else if (name.equals("waterEngine")) {
                    int taskId = m_scene.getCustom(joint, "taskId", 0);
                    int taskFinishers = m_scene.getCustom(joint, "taskFinishers", 0);
                    TwoWayEngineComponent engineComponent = new TwoWayEngineComponent(taskId, JointFactory.getInstance().createJoint(
                            tempList.get(ind.first),
                            tempList.get(ind.second), jDef, physicsWorld));
                    entity.addComponent(engineComponent);
                    gameEntityWorld.addObserver(engineComponent);

                } else {
                    JointFactory.getInstance().createJoint(tempList.get(ind.first),
                            tempList.get(ind.second), jDef, physicsWorld);
                }
            }
            // m_wheels.add((WheelJoint)jD);
        }

        if (joint.getType() == JointType.WeldJoint) {
            WeldJointDef jDef = (WeldJointDef) ind.jointDef;
            JointFactory.getInstance().createJoint(tempList.get(ind.first),
                    tempList.get(ind.second), jDef, physicsWorld);
        }

        if (joint.getType() == JointType.PrismaticJoint) {
            String name = (String) joint.getUserData();
            PrismaticJointDef jDef = (PrismaticJointDef) ind.jointDef;
            if (name.equals("doorMotor")) {
                int taskId = m_scene.getCustom(joint, "taskId", 0);
                int taskFinishers = m_scene.getCustom(joint, "taskFinishers", 0);
                DoorComponent comp = new DoorComponent(taskFinishers, taskId);
                comp.setPrismJoint(JointFactory.getInstance().createJoint(
                        tempList.get(ind.first), tempList.get(ind.second),
                        jDef, physicsWorld));
                entity.addComponent(new TriggerComponent());
                entity.addComponent(comp);
                entity.addToWorld();
                gameEntityWorld.addObserver(comp);
            } else {
                JointFactory.getInstance().createJoint(
                        tempList.get(ind.first), tempList.get(ind.second),
                        jDef, physicsWorld);
            }
        }
    }

}

    /*
     * private Vector2 vector = new Vector2(); private void loadBodyParts(Entity
     * e, PhysicsSystem pSystem) { AnimationComponent anim =
     * e.getComponent(AnimationComponent.class); PhysicsComponent ps =
     * e.getComponent(PhysicsComponent.class); final TextureAtlas atlas =
     * anim.getAtlas();
     *
     * AtlasAttachmentLoader atlasLoader = new AtlasAttachmentLoader(atlas);
     * SkeletonJson json = new SkeletonJson(atlasLoader); json.setScale(1.3f);
     * SkeletonData skeletonData =
     * json.readSkeletonData(Gdx.files.internal("skeleton.json"));
     *
     * Skeleton skeleton = new Skeleton(skeletonData);
     *
     * for (Slot slot : skeleton.getSlots()) { if (!(slot.getAttachment()
     * instanceof RegionAttachment)) continue; RegionAttachment attachment =
     * (RegionAttachment)slot.getAttachment();
     *
     * PolygonShape boxPoly = new PolygonShape(); boxPoly.setAsBox(
     * Converters.ToBox(attachment.getWidth() / 2 * attachment.getScaleX()),
     * Converters.ToBox(attachment.getHeight() / 2 * attachment.getScaleY()),
     * Converters.ToBox(vector.set(attachment.getX(), attachment.getY())),
     * attachment.getRotation() * MathUtils.degRad);
     * System.out.println(attachment.getName()); BodyDef boxBodyDef = new
     * BodyDef(); boxBodyDef.type = BodyType.StaticBody;
     * ps.createBody(pSystem.getWorld(),boxBodyDef, attachment.getName());
     * ps.createFixture(boxPoly, attachment.getName());
     *
     * boxPoly.dispose(); } }
     */
