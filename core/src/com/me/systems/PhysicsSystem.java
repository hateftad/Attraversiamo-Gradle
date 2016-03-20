package com.me.systems;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.me.component.*;
import com.me.controllers.B2BuoyancyController;
import com.me.controllers.B2Controller;
import com.me.component.QueueComponent.QueueType;
import com.me.events.states.PlayerState;
import com.me.listeners.LevelEventListener;
import com.me.listeners.PhysicsContactListener;
import com.me.physics.JointFactory;
import com.me.physics.PhysicsListenerSetup;
import com.me.config.GlobalConfig;

public class PhysicsSystem extends EntitySystem implements Disposable, LevelEventListener {

    @Mapper
    ComponentMapper<PhysicsComponent> physicsComponents;

    @Mapper
    ComponentMapper<PlayerAnimationComponent> animComponents;

    @Mapper
    ComponentMapper<QueueComponent> queueComps;

    @Mapper
    ComponentMapper<RestartComponent> restartComps;

    @Mapper
    ComponentMapper<JointComponent> jointComps;

    @Mapper
    ComponentMapper<BuoyancyComponent> bouyComps;

    @Mapper
    ComponentMapper<FeetComponent> feetComponents;

    private World physicsWorld;

    private int velocityItr;

    private int positionItr;

    private float timeStep;

    private PhysicsContactListener physicsContactListener;

    private boolean restart;

    private boolean processPhysics = true;

    private double currentTime = System.currentTimeMillis();

    private float fixedAccumulator = 0;

    private float fixedAccumulatorRatio = 0;

    private static final int MAXSTEPS = 2;

    private ObjectMap<String, B2Controller> b2Controllers;

    public PhysicsSystem(World physicsWorld) {
        this(physicsWorld, 50, 30);
        timeStep = GlobalConfig.getInstance().config.timeStep;
        b2Controllers = new ObjectMap<>();
    }

    public void toggleProcessing(boolean process) {
        processPhysics = process;
    }

    @SuppressWarnings("unchecked")
    public PhysicsSystem(World physicsWorld, int velocityIterations,
                         int positionIterations) {
        super(Aspect.getAspectForAll(PhysicsComponent.class));
        this.physicsWorld = physicsWorld;
        this.physicsWorld.setAutoClearForces(true);
        this.physicsWorld.setContinuousPhysics(true);
        this.physicsWorld.setWarmStarting(true);
        velocityItr = velocityIterations;
        positionItr = positionIterations;
        physicsContactListener = new PhysicsContactListener();
    }

    @Override
    protected void begin() {

        double now = System.currentTimeMillis();
        double dt = now - currentTime;
        currentTime = now;

        fixedAccumulator += dt;

        final int nSteps = (int) Math.floor(fixedAccumulator / timeStep);

        if (nSteps > 0) {
            fixedAccumulator -= nSteps * timeStep;
        }
        fixedAccumulatorRatio = fixedAccumulator / timeStep;

        int nStepsClamped = Math.min(nSteps, MAXSTEPS);

        for (int i = 0; i < nStepsClamped; i++) {
            resetSmoothStates();
            for (B2Controller controller : b2Controllers.values()) {
                controller.step(timeStep);
            }
            singleStep();
        }

        physicsWorld.clearForces();
        smoothStates();


    }

    private void smoothStates() {

        final double oneMinusRatio = 1.0 - fixedAccumulatorRatio;
        Array<Body> bodies = new Array<>();
        physicsWorld.getBodies(bodies);

        for (Body b : bodies) {
            Entity e = (Entity) b.getUserData();
            if (e != null) {
                if (physicsComponents.has(e)) {
                    physicsComponents.get(e).updateSmoothStates(fixedAccumulatorRatio, oneMinusRatio);
                }
            }
        }
    }

    private void resetSmoothStates() {

        Array<Body> bodies = new Array<>();
        physicsWorld.getBodies(bodies);
        for (Body b : bodies) {
            Entity e = (Entity) b.getUserData();
            if (e != null) {
                if (physicsComponents.has(e)) {
                    PhysicsComponent physicsComponent = physicsComponents.get(e);
                    physicsComponent.updatePreviousPosition();
                    if (feetComponents.has(e)) {
                        FeetComponent feetComponent = feetComponents.get(e);
                        feetComponent.reset();
                        feetComponent.update(physicsComponent.getBody(feetComponent.getBodyName()));
                        for (int i = 0; i < feetComponent.getStartPoints().size(); i++) {
                            physicsWorld.rayCast(feetComponent.getRaycastCallback(), feetComponent.getStartPoints().get(i), feetComponent.getEndPoints().get(i));
                        }
                    }
                }
            }
        }
    }

    private void singleStep() {
        if (processPhysics) {
            physicsWorld.step(world.delta, velocityItr, positionItr);
        }
    }

    @Override
    protected boolean checkProcessing() {
        return processPhysics;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        if (restart) {
            for (int i = 0; i < entities.size(); i++) {
                Entity e = entities.get(i);
                if (restartComps.has(e)) {
                    PhysicsComponent comp = physicsComponents.get(e);
                    comp.setToStart();
                    if (animComponents.has(e)) {
                        animComponents.get(e).setAnimationState(PlayerState.Idle);
                        if (e.getComponent(PlayerTwoComponent.class) != null) {
                            comp.makeDynamic("center", 0.001f);
                        }
                    }
                }
                Bag<Component> fillBag = new Bag<>();
                e.getComponents(fillBag);
                for (int x = 0; x < fillBag.size(); x++) {
                    BaseComponent comp = (BaseComponent) fillBag.get(x);
                    comp.restart();
                }
            }
            OnStartLevel();
            printInfo();
        }

        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (queueComps.has(e)) {
                QueueComponent comp = queueComps.get(e);
                if (comp.type == QueueType.Mass) {
                    physicsComponents.get(e).setMass(comp.mass, comp.bodyName);
                } else if (comp.type == QueueType.TempMass) {
                    physicsComponents.get(e).setMass(comp.mass, comp.bodyName);
                    e.removeComponent(comp);
                } else if (comp.type == QueueType.Joint) {
                    JointComponent joint = jointComps.get(e);
                    JointFactory.getInstance().destroyJoint(joint.getJoint());
                    e.removeComponent(comp);
                } else if (comp.type == QueueType.BodyState) {
                    System.out.println("removing component");
                    physicsComponents.get(e).setActive(comp.active);
                    e.removeComponent(comp);
                }
            }
        }
    }

    @Override
    protected void removed(Entity e) {
        PhysicsComponent physicsComponent = physicsComponents.get(e);

        if (physicsComponent == null) {
            return;
        }
        world.deleteEntity(e);
    }

    public void clearSystem() {
        Array<Body> bodies = new Array<>();
        physicsWorld.getBodies(bodies);

        for (Body b : bodies) {
            physicsWorld.destroyBody(b);
        }
        physicsWorld.setContactListener(null);

        b2Controllers.clear();
        dispose();
    }

    @Override
    protected void inserted(Entity e) {

        if (bouyComps.has(e)) {

            PhysicsComponent pComp = physicsComponents.get(e);
            Fixture fixture = pComp.getBody().getFixtureList().first();
            final Vector2 mTmp = new Vector2();
            float bodyHeight = fixture.getBody().getPosition().y;
            PolygonShape shape = (PolygonShape) fixture.getShape();
            shape.getVertex(0, mTmp);
            float maxHeight = mTmp.y + bodyHeight;
            for (int j = 1; j < shape.getVertexCount(); j++) {
                shape.getVertex(j, mTmp);
                maxHeight = Math.max(maxHeight, mTmp.y + bodyHeight);
            }

            BuoyancyComponent buoyancyComponent = bouyComps.get(e);
            ObjectMap<String, B2Controller> controllers = new ObjectMap<>();
            for (Object o : buoyancyComponent.getControllerInfo().entries()) {
                ObjectMap.Entry pairs = (ObjectMap.Entry) o;
                BuoyancyComponent.BuoyancyControllerConfig controllerInfo = (BuoyancyComponent.BuoyancyControllerConfig) pairs.value;
                B2BuoyancyController b2c = new B2BuoyancyController(B2BuoyancyController.DEFAULT_SURFACE_NORMAL,
                        controllerInfo.getFluidVelocity(),
                        new Vector2(0, 10),
                        maxHeight,
                        fixture.getDensity(),
                        controllerInfo.getLinearDrag(),
                        controllerInfo.getAngularDrag());
                controllers.put((String) pairs.key, b2c);
                System.out.println(" fixture density "+fixture.getDensity());
            }
            fixture.setUserData(controllers);
            b2Controllers = controllers;
            PhysicsListenerSetup setup = new PhysicsListenerSetup((GameEntityWorld) world);
            setup.setLevelPhysics(pComp);
        }
    }

    public void printInfo() {
        System.out.println(" Bodies in the physicsWorld: " + physicsWorld.getBodyCount());
        System.out.println(" Fixtures in the physicsWorld: " + physicsWorld.getFixtureCount());
        System.out.println(" Number of Contacts: " + physicsWorld.getContactCount());
        System.out.println(" Joints in the physicsWorld: " + physicsWorld.getJointCount());
    }

    @Override
    public void initialize() {
        physicsWorld.setContactListener(physicsContactListener);
    }

    @Override
    public void dispose() {
        physicsWorld.dispose();
    }

    public World getPhysicsWorld() {
        return physicsWorld;
    }

    @Override
    public void onRestartLevel() {
        restart = true;
    }

    @Override
    public void OnStartLevel() {
        restart = false;
    }

    @Override
    public void onFinishedLevel(int nr) {

    }

}
