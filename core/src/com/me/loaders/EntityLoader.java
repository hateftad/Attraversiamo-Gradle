package com.me.loaders;

import java.util.WeakHashMap;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
import com.me.events.states.PlayerState;
import com.me.level.Player;
import com.me.component.*;
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
import com.me.utils.GameUtils;

public class EntityLoader {

    private RubeScene scene;
    private RubeSceneLoader loader;
    private WeakHashMap<String, Texture> textureMap;
    private static final String LVLPATH = "data/level/";
    private static final String CHARPATH = "data/character/";

    public EntityLoader() {
        textureMap = new WeakHashMap<>();
        loader = new RubeSceneLoader();
    }

    private void clearLoader() {

        if (scene != null) {
            scene.clear();
        }
        if (textureMap.size() > 0) {
            textureMap.clear();
        }
    }

    public void dispose() {
        scene.dispose();
        scene.clear();
        for (Texture t : textureMap.values()) {
            t.dispose();
        }
        textureMap.clear();

    }

    public void loadLevel(Level level, GameEntityWorld entityWorld,
                          World physicsWorld, RayHandler rh) {
        String levelDirectory = level.getLevelName();
        clearLoader();
        FileHandle fileHandle = Gdx.files.internal("data/level/" + levelDirectory + "/" + levelDirectory + ".json");
        scene = loader.loadScene(fileHandle);
        Array<Body> bodies = scene.getBodies();

        GameEventFactory factory = new GameEventFactory();
        Vector2 bodyPos = new Vector2();
        Vector2 tmp = new Vector2();
        PhysicsComponent pComp = null;
        SpriteComponent sComp;
        Entity entity = null;
        RubeImage image = null;
        Array<Body> tempList = new Array<>();

        for (int i = 0; i < bodies.size; i++) {

            Body body = bodies.get(i);

            Array<RubeImage> images = scene.getMappedImage(body);
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
                    Texture texture = textureMap.get(textureFileName);
                    if (texture == null) {
                        texture = new Texture(textureFileName);
                        texture.setFilter(Texture.TextureFilter.Linear,
                                Texture.TextureFilter.Nearest);
                        textureMap.put(textureFileName, texture);
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
                        sComp.shouldDraw = scene.getCustom(image, "shouldDraw", true);
                        entity.addComponent(sComp);
                    }
                }

            } else {
                pComp = new PhysicsComponent(physicsWorld, body,
                        ((BodyUserData) body.getUserData()).mName);
                entity = entityWorld.createEntity();
                entity.addComponent(pComp);
            }

            loadFixtures(pComp, body);

            BodyUserData ud = (BodyUserData) body.getUserData();

            if (ud.mName.equalsIgnoreCase("sky_light")) {
                CameraComponent camComp = entityWorld.getSystem(CameraSystem.class).getCameraComponent();
                entity.addComponent(camComp);
                String color = scene.getCustom(body, "lightColor", "");
                if(!color.equalsIgnoreCase("none") && !color.isEmpty()) {
                    PointLight light = new PointLight(rh, 50, GameUtils.getColor(color), 5000, camComp.getCamera().position.x, camComp.getCamera().position.y);
                    entity.addComponent(new LightComponent(light, "cameraLight"));
                    entityWorld.getManager(GroupManager.class).add(entity, "lights");
                }
            }

            if (ud.mName.equalsIgnoreCase("light")) {
                String color = scene.getCustom(body, "light", "");
                ConeLight light = new ConeLight(rh, 50, GameUtils.getColor(color), 500, Converters.ToWorld(body.getPosition().x), Converters.ToWorld(body.getPosition().y), 180, 180);
                entity.addComponent(new LightComponent(light, ((BodyUserData) body.getUserData()).mName));
                entityWorld.getManager(GroupManager.class).add(entity, "lights");
            }
            if (ud.mName.equalsIgnoreCase("box")) {
                pComp.setMass(20f, ud.mName);
                entity.addComponent(new RestartComponent())
                        .addComponent(new QueueComponent());
            }

            if (ud.mName.equalsIgnoreCase("minX")) {
                level.getLevelBoundaries().minX = Converters.ToWorld(body.getPosition().x);
            }
            if (ud.mName.equalsIgnoreCase("maxX")) {
                level.getLevelBoundaries().maxX = Converters.ToWorld(body.getPosition().x);
            }
            if (ud.mName.equalsIgnoreCase("minY")) {
                level.getLevelBoundaries().minY = Converters.ToWorld(body.getPosition().y);
            }
            if (ud.mName.equalsIgnoreCase("maxY")) {
                level.getLevelBoundaries().maxY = Converters.ToWorld(body.getPosition().y);
            }
            if (ud.mName.equalsIgnoreCase("cage")) {
                entityWorld.addObserver(pComp);
            }

            if (ud.mName.equalsIgnoreCase("water")) {
                int eventId = scene.getCustom(body, "taskId", 0);
                //pass in fluid velocity
                BuoyancyComponent buoyancyComponent = new BuoyancyComponent(eventId);
                buoyancyComponent.addControllerInfo(PlayerOneComponent.PlayerOne, new Vector2(0, 1), 4, 4);
                buoyancyComponent.addControllerInfo(PlayerTwoComponent.PlayerTwo, new Vector2(0, 1), 0.2f, 0);
                buoyancyComponent.addControllerInfo(WorldObjectComponent.WorldObject, new Vector2(-1, 4), 5, 2);
                entityWorld.addObserver(buoyancyComponent);
                entity.addComponent(buoyancyComponent);
            }

            if (ud.mName.equalsIgnoreCase("portal") || ud.mName.equalsIgnoreCase("finish")) {
                ReachEndComponent reachEndComponent = new ReachEndComponent(level.getNumberOfFinishers());
                reachEndComponent.setEndEvent(factory.createFromBodyInfo(scene, body));
                entity.addComponent(reachEndComponent);
                entityWorld.addObserver(reachEndComponent);
                int taskId = scene.getCustom(body, "taskId", 0);
                if(ud.mName.equalsIgnoreCase("finish")) {
                    LevelComponent levelComponent = new LevelComponent(level.getNumberOfFinishers(), LevelComponent.RUNOUT, taskId);
                    entityWorld.addObserver(levelComponent);
                    entity.addComponent(levelComponent);
                } else if (ud.mName.equalsIgnoreCase("portal")){
                    LevelComponent levelComponent = new LevelComponent(level.getNumberOfFinishers(), LevelComponent.PORTAL, taskId);
                    entityWorld.addObserver(levelComponent);
                    entity.addComponent(levelComponent);
                }

            }

            if (ud.mName.equalsIgnoreCase("particleEmitter")) {
                String particleName = scene.getCustom(body, "particlename", "");
                ContinuousParticles particleComponent = new ContinuousParticles(particleName, pComp.getPosition());
                entity.addComponent(particleComponent);
                entityWorld.addObserver(particleComponent);
            }

            if (ud.mName.equalsIgnoreCase("singleParticleEmitter")) {
                String particleName = scene.getCustom(body, "particlename", "");
                int particleId = scene.getCustom(body, "taskId", 0);
                EventParticleComponent particleComponent = new EventParticleComponent(particleName, particleId, body.getPosition());
                particleComponent.setEvent(factory.createParticleEventFromBodyInfo(scene, body));
                entity.addComponent(particleComponent);
                entityWorld.addObserver(particleComponent);
            }

            if (ud.mName.equalsIgnoreCase("bodyInfo")) {
                pComp.setTaskInfo(factory.createFromBodyInfo(scene, body));
            }

            if (ud.mName.equalsIgnoreCase("player_position")) {
                int player = scene.getCustom(body, "playerNr", -1);
                level.addPlayerPosition(player, body.getPosition());
            }

            if (ud.mName.equalsIgnoreCase("animating_body")) {
                String atlas = scene.getCustom(body, "atlas", "");
                String skeleton = scene.getCustom(body, "skeleton", "");
                boolean flip = scene.getCustom(body, "flip_animation", false);
                int eventId = scene.getCustom(body, "taskId", 0);
                atlas = LVLPATH + "animation/" + atlas;
                skeleton = LVLPATH + "animation/" + skeleton;
                LevelAnimationComponent levelAnimationComponent = new LevelAnimationComponent(atlas, skeleton, 5f, eventId);
                levelAnimationComponent.setUp(bodyPos, "idle");
                levelAnimationComponent.setFacing(flip);
                levelAnimationComponent.setAnimationState(PlayerState.Idle);
                entity.addComponent(levelAnimationComponent);
                entityWorld.addObserver(levelAnimationComponent);

            }

            pComp.setRBUserData(pComp.getBody(ud.mName), new RBUserData(ud.mBoxIndex, ud.mCollisionGroup, ud.mtaskId, pComp.getBody(ud.mName)));
            pComp.setUserData(entity, ((BodyUserData) body.getUserData()).mName);
            tempList.add(pComp.getBody(ud.mName));
            entity.addToWorld();
        }

        loadBodyJoints(physicsWorld, tempList, entityWorld);
        tempList.clear();
    }

    public Entity loadCharacter(Player playerConfig, GameEntityWorld entityWorld, World physicsWorld) {
        String characterPath = playerConfig.getName() + "/";
        String characterName = playerConfig.getName();

        clearLoader();

        scene = loader.loadScene(Gdx.files.internal(CHARPATH
                + characterPath + "/" + characterName + ".json"));
        Array<Body> bodies = scene.getBodies();

        Vector2 bodyPos = new Vector2();
        Vector2 tmp = new Vector2();
        PhysicsComponent pComp = null;
        SpriteComponent sComp = null;
        Entity entity = entityWorld.createEntity();
        RubeImage image = null;
        Array<Body> tempList = new Array<>();

        for (int i = 0; i < bodies.size; i++) {

            Body body = bodies.get(i);

            Array<RubeImage> images = scene.getMappedImage(body);
            if (images != null) {

                bodyPos.set(body.getPosition());
                image = images.get(0);
                String tName = image.file;
                if (tName != null) {
                    tmp.set(image.width, image.height);
                    String textureFileName = CHARPATH + characterPath + tName;
                    Texture texture = textureMap.get(textureFileName);
                    if (texture == null) {
                        texture = new Texture(textureFileName);
                        texture.setFilter(Texture.TextureFilter.Linear,
                                Texture.TextureFilter.Nearest);
                        textureMap.put(textureFileName, texture);
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
                if (scene.getCustom(body, "characterType", "").equalsIgnoreCase("leg")) {
                    String name = ((BodyUserData) body.getUserData()).mName;
                    if (pComp != null) {
                        pComp.addBody(physicsWorld, body, name);
//                        pComp.setMass(20f, name);
                    } else {
                        pComp = new PhysicsComponent(physicsWorld, body, name);
//                        pComp.setMass(20f, name);
                        entity.addComponent(pComp);
                    }

                    FeetComponent feetComponent = new FeetComponent(name);
                    entity.addComponent(feetComponent);

                } else if (scene.getCustom(body, "characterType", "").equalsIgnoreCase(
                        "hand")) {
                    if (pComp != null) {
                        pComp.addBody(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
                    } else {
                        pComp = new PhysicsComponent(physicsWorld, body, ((BodyUserData) body.getUserData()).mName);
                        entity.addComponent(pComp);
                    }
                    pComp.setMass(1f, ((BodyUserData) body.getUserData()).mName);
                } else if (scene.getCustom(body, "characterType", "").equalsIgnoreCase("LHand")) {
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

            String skelName = scene.getCustom(body, "skeleton", "failed");
            String atlasName = scene.getCustom(body, "atlas", "failed");
            PlayerAnimationComponent animationComponent = null;
            AnimationStateData stateData;
            if (!skelName.equalsIgnoreCase("failed") && !atlasName.equalsIgnoreCase("failed")) {
                animationComponent = new PlayerAnimationComponent(CHARPATH + characterPath
                        + atlasName, CHARPATH + characterPath + skelName, 1.3f, playerConfig.getFinishAnimation(), getPlayerNumber(playerConfig.getPlayerNumber()));
                entity.addComponent(animationComponent);
                entityWorld.addObserver(animationComponent);
            }
            if (scene.getCustom(body, "characterType", "").equalsIgnoreCase("player_one")) {

                PlayerComponent playerComponent = new PlayerComponent(getPlayerNumber(playerConfig.getPlayerNumber()), playerConfig.isFinishFacingLeft());
                playerComponent.setActive(playerConfig.isActive());
                playerComponent.setFacingLeft(playerConfig.isFacingLeft());
                playerComponent.setCanBecomeInactive(playerConfig.canDeactivate());
                entityWorld.addObserver(playerComponent);
                // entity.addComponent(new LightComponent(light, ((BodyUserData)
                // body.getUserData()).mName));
                entity.addComponent(playerComponent)
                        .addComponent(new TouchComponent())
                        .addComponent(new KeyInputComponent())
                        .addComponent(new JointComponent())
                        .addComponent(new HangComponent())
                        .addComponent(new RagDollComponent())
                        .addComponent(new LadderClimbComponent())
                        .addComponent(new VelocityLimitComponent(12, 14, 5, 6f))
                        .addComponent(new PushComponent())
                        .addComponent(new JumpComponent())
                        .addComponent(new GrabComponent())
                        .addComponent(new PlayerOneComponent())
                        .addComponent(new TriggerComponent())
                        .addComponent(new RestartComponent())
                        .addComponent(new EventComponent());

                HandHoldComponent handHoldComponent = new HandHoldComponent();
                entity.addComponent(handHoldComponent);
                entityWorld.addObserver(handHoldComponent);


                pComp.setName(((BodyUserData) body.getUserData()).mName);
                pComp.setIsPlayer(true);
                stateData = animationComponent.setUp(image);
                animationComponent.setAnimationState(PlayerState.Idle);
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
                stateData.setMix("runJumping", "falling", 0.2f);
                stateData.setMix("pushing", "idle1", 0.4f);
                stateData.setMix("falling", "drowning", 0.4f);
                stateData.setMix("landing", "jogging", 0.4f);

                animationComponent.setSkin(playerConfig.getSkinName());
                pComp.setAllBodiesPosition(playerConfig.getPosition());

            } else if (scene.getCustom(body, "characterType", "").equalsIgnoreCase("player_two")) {
                PlayerComponent playerComponent = new PlayerComponent(getPlayerNumber(playerConfig.getPlayerNumber()), playerConfig.isFinishFacingLeft());
                playerComponent.setActive(playerConfig.isActive());
                playerComponent.setFacingLeft(playerConfig.isFacingLeft());
                playerComponent.setCanBecomeInactive(playerConfig.canDeactivate());
                entityWorld.addObserver(playerComponent);

                pComp.setName(((BodyUserData) body.getUserData()).mName);
                pComp.setMass(0.001f, ((BodyUserData) body.getUserData()).mName);
                pComp.setIsPlayer(true);
                stateData = animationComponent.setUp(image);
                animationComponent.setAnimationState(PlayerState.Idle);
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
                stateData.setMix("falling", "drowning", 0.4f);
                entity.addComponent(playerComponent);
                animationComponent.setSkin(playerConfig.getSkinName());
                entity.addComponent(new KeyInputComponent())
                        .addComponent(new VelocityLimitComponent(8.5f, 10, 2.5f))
                        .addComponent(new JointComponent())
                        .addComponent(new TouchComponent())
                        .addComponent(new JumpComponent())
                        .addComponent(new GrabComponent())
                        .addComponent(new PlayerTwoComponent())
                        .addComponent(new TriggerComponent())
                        .addComponent(new RestartComponent())
                        .addComponent(new PushComponent())
                        .addComponent(new QueueComponent())
                        .addComponent(new EventComponent());


                HandHoldComponent handHoldComponent = new HandHoldComponent();
                entity.addComponent(handHoldComponent);
                entityWorld.addObserver(handHoldComponent);

                pComp.setAllBodiesPosition(playerConfig.getPosition());


            }

            BodyUserData ud = (BodyUserData) body.getUserData();
            pComp.setRBUserData(pComp.getBody(ud.mName), new RBUserData(ud.mBoxIndex, ud.mCollisionGroup, ud.mtaskId, pComp.getBody(ud.mName)));
            pComp.setUserData(entity, ud.mName);
            tempList.add(pComp.getBody(ud.mName));

            if (ud.mName.equalsIgnoreCase("feet")) {
                GameEventFactory factory = new GameEventFactory();
                pComp.setTaskInfo(factory.createFromBodyInfo(scene, body));
                SingleParticleComponent particleComponent = new SingleParticleComponent("smoke", playerConfig.getPlayerNumber(), pComp.getBody("feet").getPosition());
                entity.addComponent(particleComponent);
                entityWorld.addObserver(particleComponent);
            }

            if (ud.mName.equalsIgnoreCase("right_hand_hold")) {
                pComp.setMass(PhysicsComponent.LOW_MASS, ud.mName);
            }
            if (ud.mName.equalsIgnoreCase("left_hand_hold")) {
                pComp.setMass(PhysicsComponent.LOW_MASS, ud.mName);
            }

        }
        Array<Joint> joints = scene.getJoints();
        if (joints != null && joints.size > 0) {
            for (int i = 0; i < joints.size; i++) {
                Joint joint = joints.get(i);
                Indexes ind = scene.getJointBodyIndex(i);
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
        Array<Joint> joints = scene.getJoints();
        if (joints == null)
            return;
        for (int j = 0; j < joints.size; j++) {

            Joint joint = joints.get(j);
            Indexes ind = scene.getJointBodyIndex(j);
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
                    int taskId = scene.getCustom(joint, "taskId", 0);
                    int taskFinishers = scene.getCustom(joint, "taskFinishers", 0);
                    RevoluteEngineComponent engineComponent = new RevoluteEngineComponent(taskId, JointFactory.getInstance().createJoint(
                            tempList.get(ind.first),
                            tempList.get(ind.second), jDef, physicsWorld));
                    entity.addComponent(engineComponent);
                    gameEntityWorld.addObserver(engineComponent);
                } else if (name.equals("hinge")) {
                    int taskId = scene.getCustom(joint, "taskId", 0);
                    int taskFinishers = scene.getCustom(joint, "taskFinishers", 0);
                    HingeComponent engineComponent = new HingeComponent(taskId, JointFactory.getInstance().createJoint(
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
                    int taskId = scene.getCustom(joint, "taskId", 0);
                    int taskFinishers = scene.getCustom(joint, "taskFinishers", 0);
                    RevoluteEngineComponent engineComponent = new RevoluteEngineComponent(taskId, JointFactory.getInstance().createJoint(
                            tempList.get(ind.first),
                            tempList.get(ind.second), jDef, physicsWorld));
                    entity.addComponent(engineComponent);
                    gameEntityWorld.addObserver(engineComponent);

                } else {
                    JointFactory.getInstance().createJoint(tempList.get(ind.first),
                            tempList.get(ind.second), jDef, physicsWorld);
                }
            }
            // wheels.add((WheelJoint)jD);
        }

        if (joint.getType() == JointType.WeldJoint) {
            WeldJointDef jDef = (WeldJointDef) ind.jointDef;
            JointFactory.getInstance().createJoint(tempList.get(ind.first),
                    tempList.get(ind.second), jDef, physicsWorld);
        }

        if (joint.getType() == JointType.RopeJoint) {
            RopeJointDef jDef = (RopeJointDef) ind.jointDef;
            JointFactory.getInstance().createJoint(tempList.get(ind.first),
                    tempList.get(ind.second), jDef, physicsWorld);
        }

        if (joint.getType() == JointType.PrismaticJoint) {
            String name = (String) joint.getUserData();
            PrismaticJointDef jDef = (PrismaticJointDef) ind.jointDef;
            if (name.equals("doorMotor")) {
                int taskId = scene.getCustom(joint, "taskId", 0);
                int taskFinishers = scene.getCustom(joint, "taskFinishers", 0);
                DoorComponent comp = new DoorComponent(taskFinishers, taskId);
                comp.setPrismJoint(JointFactory.getInstance().createJoint(
                        tempList.get(ind.first), tempList.get(ind.second),
                        jDef, physicsWorld));
                entity.addComponent(comp);
                entity.addToWorld();
                gameEntityWorld.addObserver(comp);
            } else if (name.equals("elevatorMotor")) {
                int taskId = scene.getCustom(joint, "taskId", 0);
                int taskFinishers = scene.getCustom(joint, "taskFinishers", 0);
                PrismaticEngineComponent engineComponent = new PrismaticEngineComponent(taskId, JointFactory.getInstance().createJoint(
                        tempList.get(ind.first),
                        tempList.get(ind.second), jDef, physicsWorld));
                entity.addComponent(engineComponent);
                gameEntityWorld.addObserver(engineComponent);
            } else {
                JointFactory.getInstance().createJoint(
                        tempList.get(ind.first), tempList.get(ind.second),
                        jDef, physicsWorld);
            }
        }
    }

    private PlayerComponent.PlayerNumber getPlayerNumber(int playerNr) {
        if (playerNr == 1) {
            return PlayerComponent.PlayerNumber.ONE;
        }
        if (playerNr == 2) {
            return PlayerComponent.PlayerNumber.TWO;
        }
        return PlayerComponent.PlayerNumber.NONE;
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
     * ps.createBody(pSystem.getPhysicsWorld(),boxBodyDef, attachment.getName());
     * ps.createFixture(boxPoly, attachment.getName());
     *
     * boxPoly.dispose(); } }
     */
