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
import com.me.component.AnimationComponent.AnimState;
import com.me.component.QueueComponent.QueueType;
import com.me.listeners.LevelEventListener;
import com.me.listeners.PhysicsContactListener;
import com.me.physics.JointFactory;
import com.me.physics.PhysicsListenerSetup;
import com.me.tasks.CharacterTask.TaskType;
import com.me.utils.GlobalConfig;

import java.util.Iterator;

public class PhysicsSystem extends EntitySystem implements Disposable,
		LevelEventListener {

	@Mapper
	ComponentMapper<PhysicsComponent> m_physicsComponents;

	@Mapper
	ComponentMapper<AnimationComponent> m_animComponents;

	@Mapper
	ComponentMapper<QueueComponent> m_queueComps;

	@Mapper
	ComponentMapper<RestartComponent> m_restartComps;

	@Mapper
	ComponentMapper<JointComponent> m_jointComps;

	@Mapper
	ComponentMapper<BuoyancyComponent> m_bouyComps;

	private World m_world;

	private int m_velocityItr;

	private int m_positionItr;

	private float m_timeStep;

	private PhysicsContactListener m_physicsContactListener;

	private boolean m_restart;

	private boolean m_processPhysics = true;

	private double m_currentTime = System.currentTimeMillis();

	private float m_fixedAccumulator = 0;

	private float m_fixedAccumulatorRatio = 0;

	private Array<B2Controller> m_b2Controllers;

	public PhysicsSystem(World physicsWorld) {
		this(physicsWorld, 6, 6);
		m_timeStep = GlobalConfig.getInstance().config.timeStep;
		m_b2Controllers = new Array<B2Controller>();
	}

	public void toggleProcessing(boolean process) {
		m_processPhysics = process;
	}

	@SuppressWarnings("unchecked")
	public PhysicsSystem(World physicsWorld, int velocityIterations,
			int positionIterations) {
		super(Aspect.getAspectForAll(PhysicsComponent.class));
		m_world = physicsWorld;
		m_world.setAutoClearForces(true);
		m_world.setContinuousPhysics(true);
		m_world.setWarmStarting(true);
		m_velocityItr = velocityIterations;
		m_positionItr = positionIterations;
		m_physicsContactListener = new PhysicsContactListener();
	}

	private static final int MAXSTEPS = 2;

	@Override
	protected void begin() {

		double now = System.currentTimeMillis();
		double dt = now - m_currentTime;
		m_currentTime = now;

		m_fixedAccumulator += dt;

		final int nSteps = (int) Math.floor(m_fixedAccumulator / m_timeStep);

		if (nSteps > 0) {
			m_fixedAccumulator -= nSteps * m_timeStep;
		}
		m_fixedAccumulatorRatio = m_fixedAccumulator / m_timeStep;

		int nStepsClamped = Math.min(nSteps, MAXSTEPS);

		for (int i = 0; i < nStepsClamped; i++) {
			resetSmoothStates();
			for (int x = 0; x < m_b2Controllers.size; x++) {
				m_b2Controllers.get(x).step(m_timeStep);
			}
			singleStep(m_timeStep);
		}

		m_world.clearForces();
		smoothStates();

	}

	private void smoothStates() {

		final double oneMinusRatio = 1.0 - m_fixedAccumulatorRatio;
		Array<Body> bodies = new Array<Body>();
		m_world.getBodies(bodies);

		for (Body b : bodies) {
			Entity e = (Entity) b.getUserData();
			if(e != null){
				if (m_physicsComponents.has(e)) {
					m_physicsComponents.get(e).updateSmoothStates(
							m_fixedAccumulatorRatio, oneMinusRatio);
				}
			}
		}
	}

	private void resetSmoothStates() {

		Array<Body> bodies = new Array<Body>();
		m_world.getBodies(bodies);
		for (Body b : bodies) {
			Entity e = (Entity) b.getUserData();
			if(e != null){
				if (m_physicsComponents.has(e)) {
					m_physicsComponents.get(e).updatePreviousPosition();
				}
			}
		}
	}

	private void singleStep(float timeStep) {
		if (m_processPhysics) {
			m_world.step(world.delta, m_velocityItr, m_positionItr);
		}
	}

	@Override
	protected boolean checkProcessing() {
		return m_processPhysics;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		if (m_restart) {
			if (!world.getSystem(LevelSystem.class).getLevelManager().isTaskDoneForAll(TaskType.ReachedEnd)) {
				for (int i = 0; i < entities.size(); i++) {
					Entity e = entities.get(i);
					if (m_restartComps.has(e)) {
						PhysicsComponent comp = m_physicsComponents.get(e);
						comp.setToStart();
						if (m_animComponents.has(e)) {
							m_animComponents.get(e).setAnimationState(
									AnimState.IDLE);
							if (e.getComponent(PlayerTwoComponent.class) != null) {
								comp.makeDynamic("center", 0.001f);
							}
						}
					}
					Bag<Component> fillBag = new Bag<Component>();
					e.getComponents(fillBag);
					for (int x = 0; x < fillBag.size(); x++) {
						BaseComponent comp = (BaseComponent) fillBag.get(x);
						comp.restart();
					}
				}
				OnStartLevel();
			}
		}

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (m_queueComps.has(e)) {
				QueueComponent comp = m_queueComps.get(e);
				if (comp.type == QueueType.Mass) {
					m_physicsComponents.get(e).setMass(comp.mass, comp.bodyName);
				} else if(comp.type == QueueType.TempMass) {
					m_physicsComponents.get(e).setMass(comp.mass, comp.bodyName);
					e.removeComponent(comp);
				}else if (comp.type == QueueType.Joint) {
					JointComponent joint = m_jointComps.get(e);
					JointFactory.getInstance().destroyJoint(joint.getDJoint());
					e.removeComponent(comp);
				}
			}
		}
	}

	@Override
	protected void removed(Entity e) {
		PhysicsComponent physicsComponent = m_physicsComponents.get(e);

		if (physicsComponent == null) {
			return;
		}
		world.deleteEntity(e);
	}

	public void clearSystem() {
		m_b2Controllers.clear();
		Array<Body> bodies = new Array<Body>();
		m_world.getBodies(bodies);

		for (Body b : bodies) {
			m_world.destroyBody(b);
		}
		m_world.setContactListener(null);
		dispose();
	}

	@Override
	protected void inserted(Entity e) {

		if (m_bouyComps.has(e)) {

			PhysicsComponent pComp = m_physicsComponents.get(e);
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

			BuoyancyComponent buoyancyComponent = m_bouyComps.get(e);
			ObjectMap<String, B2BuoyancyController> controllers = new ObjectMap<String, B2BuoyancyController>();
			Iterator it = buoyancyComponent.getControllerInfo().entries().iterator();
			while (it.hasNext()) {
				ObjectMap.Entry pairs = (ObjectMap.Entry) it.next();
				BuoyancyComponent.BuoyancyControllerInfo controllerInfo = (BuoyancyComponent.BuoyancyControllerInfo) pairs.value;
				B2BuoyancyController b2c = new B2BuoyancyController(B2BuoyancyController.DEFAULT_SURFACE_NORMAL,
						controllerInfo.getFluidVelocity(),
						m_world.getGravity(),
						maxHeight,
						fixture.getDensity(),
						controllerInfo.getLinearDrag(),
						controllerInfo.getAngularDrag());
				controllers.put((String)pairs.key, b2c);
				m_b2Controllers.add(b2c);
				it.remove();
			}
			fixture.setUserData(controllers);
			PhysicsListenerSetup setup = new PhysicsListenerSetup();
			setup.setLevelPhysics(pComp);
		}
	}

	public void printInfo() {
		System.out.println(" Bodies in the world: " + m_world.getBodyCount());
		System.out.println(" Fixtures in the world: " + m_world.getFixtureCount());
		System.out.println(" Number of Contacts: " + m_world.getContactCount());
		System.out.println(" Joints in the world: " + m_world.getJointCount());
	}

	@Override
	public void initialize() {
		m_world.setContactListener(m_physicsContactListener);
	}

	@Override
	public void dispose() {
		m_world.dispose();
	}

	public World getWorld() {
		return m_world;
	}

	@Override
	public void onRestartLevel() {
		m_restart = true;
	}

	@Override
	public void OnStartLevel() {
		m_restart = false;
	}

	@Override
	public void onFinishedLevel(int nr) {

	}

}
