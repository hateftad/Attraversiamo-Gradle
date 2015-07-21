package com.me.component;

import java.util.*;
import java.util.Map.Entry;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;
import com.me.event.GameEvent;
import com.me.physics.FixtureData;
import com.me.physics.RBUserData;
import com.me.utils.Converters;

public class PhysicsComponent extends BaseComponent {

	public static float LOW_FRICTION = 0.1f;
	public static float HIGH_FRICTION = 20f;

	private ObjectMap<String, Body> m_body = new ObjectMap<String, Body>();
	private ObjectMap<Body, RBUserData> m_userData = new ObjectMap<Body, RBUserData>();
	private Vector2 m_worldPosition;
	private ObjectMap<Body, Vector2> m_previousPositions;
	private float m_previousAngle = 0;
	private Vector2 m_startPosition;
	private boolean m_isActive;
	private String m_name;
	private boolean m_isPlayer;
	private boolean m_isDynamic = true;
    private FixtureData m_fixtureData;
	private Filter currentFilter;
    private short currentBits;
	public ImmediateModePhysicsListener m_physicsListener;
	private boolean m_submerged;
    private GameEvent m_eventInfo;

	public PhysicsComponent(World world, Body b, String name) {
		m_name = name;
		m_worldPosition = new Vector2();
		m_body.put(m_name, createBody(world, b));
		setActive(true);
		m_body.get(m_name).setBullet(b.isBullet());
		m_body.get(m_name).setFixedRotation(b.isFixedRotation());
		m_body.get(m_name).setSleepingAllowed(b.isSleepingAllowed());
		m_previousPositions = new ObjectMap<Body, Vector2>();
		m_previousPositions.put(m_body.get(name), m_body.get(name).getPosition());
		m_startPosition = new Vector2(m_body.get(name).getPosition());
	}

	public Body createBody(World world, Body b) {

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = b.getType();
		bodyDef.position.set(b.getPosition().x, b.getPosition().y);
		bodyDef.angle = b.getAngle();
		return world.createBody(bodyDef);
	}

	public void createFixture(Fixture fixture, String name) {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = fixture.getDensity();
		fixtureDef.friction = fixture.getFriction();
		fixtureDef.restitution = fixture.getRestitution();
		fixtureDef.isSensor = fixture.isSensor();
		fixtureDef.filter.categoryBits = fixture.getFilterData().categoryBits;
		fixtureDef.filter.maskBits = fixture.getFilterData().maskBits;
		fixtureDef.filter.groupIndex = fixture.getFilterData().groupIndex;
		fixtureDef.shape = fixture.getShape();
		m_body.get(name).createFixture(fixtureDef);

	}

	public void addBody(World ps, Body b, String name) {
		m_body.put(name, createBody(ps, b));
		m_body.get(name).setBullet(b.isBullet());
		m_body.get(name).setFixedRotation(b.isFixedRotation());
		m_body.get(name).setSleepingAllowed(b.isSleepingAllowed());
		m_body.get(name).setActive(b.isActive());
		m_previousPositions.put(m_body.get(name), m_body.get(name)
				.getPosition());
	}

	public void makeKinematic() {
		m_body.get(m_name).setType(BodyType.KinematicBody);
		m_body.get("feet").setType(BodyType.KinematicBody);

		setIsDynamic(false);
	}

	public void makeDynamic() {
		m_body.get(m_name).setType(BodyType.DynamicBody);
		m_body.get("feet").setType(BodyType.DynamicBody);
		setIsDynamic(true);
	}

	public void makeStatic() {
		for (Body b : m_body.values()) {
			b.setType(BodyType.StaticBody);
		}
	}

	public void makeStatic(String name) {
		m_body.get(name).setType(BodyType.StaticBody);
	}

	public void makeDynamic(String name, float mass) {
		m_body.get(name).setType(BodyType.DynamicBody);
		setMass(mass, name);
	}

	public boolean isFalling() {
		if (m_body.get(m_name).getLinearVelocity().y < -4) {
			return true;
		}
		return false;
	}

	public void disableBody(String name) {

		if (m_fixtureData == null) {
            Filter currentFilter = m_body.get(name).getFixtureList().get(0).getFilterData();
            float friction = m_body.get(name).getFixtureList().get(0).getFriction();
            m_fixtureData = new FixtureData(currentFilter, friction);
		}

		Filter t1 = m_fixtureData.getCurrentFilter();
		t1.categoryBits = 0;
		m_body.get(name).getFixtureList().get(0).setFilterData(t1);

	}

	public void enableBody(String name) {

		if (m_fixtureData != null) {
			m_fixtureData.restoreCategoryBits();
			m_body.get(name).getFixtureList().get(0).setFilterData(m_fixtureData.getCurrentFilter());
            m_body.get(name).getFixtureList().get(0).setFriction(m_fixtureData.getCurrentFriction());

		}
	}

	private HashMap<String, Short> filterData = new HashMap<String, Short>();

	public void disableAllFilters() {

		Iterator<ObjectMap.Entry<String, Body>> it = m_body.entries().iterator();

		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			ObjectMap.Entry<String, Body> pairs = (ObjectMap.Entry<String, Body>) it.next();
			Body b = (Body) pairs.value;
			short bits;
			Filter filter = b.getFixtureList().get(0).getFilterData();
			bits = filter.categoryBits;
			filterData.put((String) pairs.key, new Short(bits));
			filter.categoryBits = 1;
			b.getFixtureList().get(0).setFilterData(filter);
		}
	}

	public void enableAllFilters() {
		Iterator<Entry<String, Short>> it = filterData.entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) it.next();
			Filter filter = m_body.get((String) pairs.getKey()).getFixtureList().get(0).getFilterData();
			short bits = (Short) pairs.getValue();
			filter.categoryBits = bits;
			m_body.get((String) pairs.getKey()).getFixtureList().get(0)
					.setFilterData(filter);
		}

	}

	public boolean movingForward() {
		if (Math.abs(m_body.get(m_name).getLinearVelocity().x) > 1) {
			return true;
		}
		return false;
	}

	public Body getBody(String name) {
		return m_body.get(name);
	}

	public Body getBody() {
		return m_body.get(m_name);
	}

	public void setMass(float mass, String name) {
		MassData m = new MassData();
		m.mass = mass;
		m_body.get(name).setMassData(m);
	}

	public void setFriction(float friction) {
		m_body.get(m_name).getFixtureList().get(0).setFriction(friction);
	}

	public void setFrictionToBody(String name, float friction){
		m_body.get(name).getFixtureList().get(0).setFriction(friction);
	}

	public float getFriction(String name){
		return m_body.get(name).getFixtureList().get(0).getFriction();
	}

	public Vector2 getWorldPosition() {
		return m_worldPosition;
	}

	public Vector2 getPosition() {
		return m_body.get(m_name).getPosition();
	}

	public void setPosition(String name, Vector2 pos) {
		m_body.get(name).setTransform(pos, 0);
	}

	public void setPosition(Vector2 pos) {
		m_body.get(m_name).setTransform(pos, 0.0f);
	}

	public void setAllBodiesPosition(Vector2 pos) {
		for (Body b : m_body.values()) {
			b.setTransform(pos, 0);
		}
	}

	public void warp(Vector2 pos) {
		disableAllFilters();
		Vector2 cent = m_body.get("center").getPosition();
		m_body.get("feet").setTransform(pos, 0.0f);
		cent.y = cent.y + pos.y;
		cent.x = pos.x;
		m_body.get("center").setTransform(cent, 0.0f);
		enableAllFilters();
	}

	public void setPosition(float x, float y) {
		m_body.get(m_name).setTransform(x, y, 0.0f);
	}

	public void warp(float x, float y) {
		for (Body b : m_body.values()) {
			b.setLinearVelocity(0, 0);
			b.setTransform(x, y, 0);
		}
	}

	public void warp(String name, Vector2 pos) {
		m_body.get(name).setLinearVelocity(0, 0);
		m_body.get(name).setTransform(pos.x, pos.y, 0);
	}

	public BodyType getBodyType() {
		return m_body.get(m_name).getType();
	}

	public void setToStart() {
		warp(m_startPosition.x, m_startPosition.y);
	}

	public void setLinearVelocity(Vector2 vel) {
		for (Body b : m_body.values())
			b.setLinearVelocity(vel);
	}

	public void setLinearVelocity(float x, float y) {
		//for (Body b : m_body.values())
		//	b.setLinearVelocity(x, y);
        m_body.get("center").setLinearVelocity(x, y);
	}

	public void applyLinearImpulse(Vector2 imp) {
		m_body.get(m_name).applyLinearImpulse(imp,
				m_body.get(m_name).getPosition(), true);
	}

	public void applyLinearImpulse(float x, float y) {
		m_body.get(m_name).applyLinearImpulse(x, y,
				m_body.get(m_name).getPosition().x,
				m_body.get(m_name).getPosition().y, true);
	}

	public Vector2 getLinearVelocity() {
		return m_body.get(m_name).getLinearVelocity();
	}

	public void setRBUserData(Body body, RBUserData ud) {
		m_userData.put(body, ud);
	}

	public RBUserData getRBUserData(Body body) {
		return m_userData.get(body);
	}

    public void setTaskInfo(GameEvent taskInfo){
        m_eventInfo = taskInfo;
    }

    public GameEvent getEventInfo(){
        return m_eventInfo;
    }

	public Values<Body> getBodyMap() {
		return m_body.values();
	}

	public Object getUserData(String name) {
		return m_body.get(name).getUserData();
	}

	public void setUserData(Entity e, String name) {
		m_body.get(name).setUserData(e);
	}

	public boolean isActive() {
		return m_isActive;
	}

	public void setActive(boolean m_isActive) {
		this.m_isActive = m_isActive;
	}

	public void setIsPlayer(boolean player) {
		m_isPlayer = player;
	}

	public void setBodyActive(boolean active) {
		for (Body b : m_body.values()) {
			b.setActive(active);
		}
	}

	public void updateSmoothStates(float accumulatorRatio, double oneMinusRatio) {

		for (Body b : m_body.values()) {
			if (b.getType() != BodyType.StaticBody) {
				float x = (float) (accumulatorRatio * b.getPosition().x + (oneMinusRatio * m_previousPositions
						.get(b).x));
				float y = (float) (accumulatorRatio * b.getPosition().y + oneMinusRatio
						* m_previousPositions.get(b).y);
				float rotation = m_isPlayer ? 0 : (float) (b.getAngle()
						* accumulatorRatio + oneMinusRatio * m_previousAngle);
				b.setTransform(x, y, rotation);
			}
		}
	}

	public void updatePreviousPosition() {
		for (Body b : m_body.values()) {
			if (b.getType() != BodyType.StaticBody) {
				m_previousPositions.get(b).set(b.getPosition());
				m_previousAngle = b.getAngle();
			}
		}
	}

	public void updateWorldPosition() {
		m_worldPosition.set(
				Converters.ToWorld(m_body.get(m_name).getPosition().x),
				Converters.ToWorld(m_body.get(m_name).getPosition().y));
	}

	public void setPhysicsListener(ImmediateModePhysicsListener listener) {
		m_physicsListener = listener;
	}

	public String getName() {
		return m_name;
	}

	public void setName(String m_name) {
		this.m_name = m_name;
	}

	public boolean isDynamic() {
		return m_isDynamic;
	}

	public void setIsDynamic(boolean m_isDynamic) {
		this.m_isDynamic = m_isDynamic;
	}

	public void destroy() {

		m_body.clear();
		m_previousPositions.clear();
		m_userData.clear();
	}

	@Override
	public void dispose() {
		destroy();
	}

	@Override
	public void restart() {
		m_isDynamic = true;
		m_isActive = true;
		setBodyActive(true);
		if (m_physicsListener != null) {
			m_physicsListener.onRestart();
		}
		enableBody("center");
	}

	public boolean isSubmerged() {
		return m_submerged;
	}

	public void setSubmerged(boolean submerged) {
		this.m_submerged = submerged;
	}


	public interface ImmediateModePhysicsListener {

		void beginContact(Entity e, Contact contact, boolean fixtureA);

		void endContact(Entity e, Contact contact, boolean fixtureA);

		void preSolve(Entity e, Contact contact, boolean fixtureA);

		void postSolve(Entity e, Contact contact, boolean fixtureA);

		void onRestart();
	}

}
