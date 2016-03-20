package com.me.component;

import java.util.*;
import java.util.Map.Entry;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;
import com.me.component.interfaces.TaskEventObserverComponent;
import com.me.events.GameEvent;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;
import com.me.physics.FixtureData;
import com.me.physics.RBUserData;
import com.me.utils.Converters;

public class PhysicsComponent extends BaseComponent implements TaskEventObserverComponent{

	public static float LOW_FRICTION = 0.1f;
	public static float LOW_MASS = 0.0001f;
	public static float HIGH_FRICTION = 20f;

	protected ObjectMap<String, Body> body = new ObjectMap<>();
	private ObjectMap<Body, RBUserData> userData = new ObjectMap<>();
	private Vector2 worldPosition;
	private ObjectMap<Body, Vector2> previousPositions;
	private float previousAngle = 0;
	private Vector2 startPosition;
	private boolean isActive;
	protected String name;
	private boolean isPlayer;
	private boolean isDynamic = true;
    private FixtureData fixtureData;
	public ImmediateModePhysicsListener physicsListener;
	private boolean submerged;
    private GameEvent eventInfo;

	public PhysicsComponent(World world, Body b, String name) {
		this.name = name;
		this.worldPosition = new Vector2();
		this.body.put(name, createBody(world, b));
		this.setActive(true);
		this.body.get(name).setBullet(b.isBullet());
		this.body.get(name).setFixedRotation(b.isFixedRotation());
		this.body.get(name).setSleepingAllowed(b.isSleepingAllowed());
		this.body.get(name).setGravityScale(b.getGravityScale());
		this.body.get(name).setAwake(b.isAwake());
		this.previousPositions = new ObjectMap<>();
		this.previousPositions.put(body.get(name), body.get(name).getPosition());
		this.startPosition = new Vector2(body.get(name).getPosition());
	}

	public Body createBody(World world, Body b) {

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = b.getType();
		bodyDef.position.set(b.getPosition());
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
		body.get(name).createFixture(fixtureDef);

	}

	public void addBody(World ps, Body b, String name) {
		body.put(name, createBody(ps, b));
		body.get(name).setBullet(b.isBullet());
		body.get(name).setFixedRotation(b.isFixedRotation());
		body.get(name).setSleepingAllowed(b.isSleepingAllowed());
		body.get(name).setActive(b.isActive());
		previousPositions.put(body.get(name), body.get(name)
				.getPosition());
	}

	public void makeKinematic() {
		body.get(name).setType(BodyType.KinematicBody);
		body.get("feet").setType(BodyType.KinematicBody);

		setIsDynamic(false);
	}

	public void makeDynamic() {
		body.get(name).setType(BodyType.DynamicBody);
		body.get("feet").setType(BodyType.DynamicBody);
		setIsDynamic(true);
	}

	public void makeStatic() {
		for (Body b : body.values()) {
			b.setType(BodyType.StaticBody);
		}
	}

	public void makeStatic(String name) {
		body.get(name).setType(BodyType.StaticBody);
	}

	public void makeDynamic(String name, float mass) {
		body.get(name).setType(BodyType.DynamicBody);
		setMass(mass, name);
	}

	public boolean isFalling() {
		if (body.get(name).getLinearVelocity().y < -4) {
			return true;
		}
		return false;
	}

	public void disableBody(String name) {

		if (fixtureData == null) {
            Filter currentFilter = body.get(name).getFixtureList().get(0).getFilterData();
            float friction = body.get(name).getFixtureList().get(0).getFriction();
            fixtureData = new FixtureData(currentFilter, friction);
		}

		Filter t1 = fixtureData.getCurrentFilter();
		t1.categoryBits = RBUserData.None;
        t1.maskBits = RBUserData.None;
        for (Fixture fixture : body.get(name).getFixtureList()) {
            fixture.setFilterData(t1);
        }
	}

	public void enableBody(String name) {

		if (fixtureData != null) {
			fixtureData.restoreCategoryBits();
            for (Fixture fixture : body.get(name).getFixtureList()) {
                fixture.setFilterData(fixtureData.getCurrentFilter());
                fixture.setFriction(fixtureData.getCurrentFriction());
            }
		}
	}

	private HashMap<String, Short> filterData = new HashMap<>();

	private void disableAllFilters() {

        for (ObjectMap.Entry<String, Body> pairs : body.entries()) {
            Body b = pairs.value;
            short bits;
            Filter filter = b.getFixtureList().get(0).getFilterData();
            bits = filter.categoryBits;
            filterData.put(pairs.key, bits);
            filter.categoryBits = 1;
            b.getFixtureList().get(0).setFilterData(filter);
        }
	}

    private void enableAllFilters() {
        for (Entry<String, Short> stringShortEntry : filterData.entrySet()) {
            Entry pairs = (Entry) stringShortEntry;
            Filter filter = body.get((String) pairs.getKey()).getFixtureList().get(0).getFilterData();
            short bits = (Short) pairs.getValue();
            filter.categoryBits = bits;
            body.get((String) pairs.getKey()).getFixtureList().get(0).setFilterData(filter);
        }

	}

	public boolean isMovingForward() {
		if (Math.abs(body.get(name).getLinearVelocity().x) > 1) {
			return true;
		}
		return false;
	}

	public Body getBody(String name) {
		return body.get(name);
	}

	public Body getBody() {
		return body.get(name);
	}

	public void setMass(float mass, String name) {
		MassData m = new MassData();
		m.mass = mass;
		body.get(name).setMassData(m);
	}

	public void setFriction(float friction) {
		body.get(name).getFixtureList().get(0).setFriction(friction);
	}

	public void setFrictionToBody(String name, float friction){
		body.get(name).getFixtureList().get(0).setFriction(friction);
	}

	public float getFriction(String name){
		return body.get(name).getFixtureList().get(0).getFriction();
	}

	public Vector2 getWorldPosition() {
		return worldPosition;
	}

	public Vector2 getPosition() {
		return body.get(name).getPosition();
	}

	public void setPosition(String name, Vector2 pos) {
		body.get(name).setTransform(pos, 0);
        updateWorldPosition();
	}

	public void setPosition(Vector2 pos) {
		body.get(name).setTransform(pos, 0.0f);
        updateWorldPosition();
	}

	public void setAllBodiesPosition(Vector2 pos) {
		for (Body b : body.values()) {
			b.setTransform(pos, 0);
		}
        updateWorldPosition();
	}

	public void warp(Vector2 pos) {
		disableAllFilters();
		Vector2 cent = body.get("center").getPosition();
		body.get("feet").setTransform(pos, 0.0f);
		cent.y = cent.y + pos.y;
		cent.x = pos.x;
		body.get("center").setTransform(cent, 0.0f);
		enableAllFilters();
	}

	public void setPosition(float x, float y) {
		body.get(name).setTransform(x, y, 0.0f);
	}

	public void warp(float x, float y) {
		for (Body b : body.values()) {
			b.setLinearVelocity(0, 0);
			b.setTransform(x, y, 0);
		}
	}

	public void warp(String name, Vector2 pos) {
		body.get(name).setLinearVelocity(0, 0);
		body.get(name).setTransform(pos.x, pos.y, 0);
	}

	public BodyType getBodyType() {
		return body.get(name).getType();
	}

	public void setToStart() {
		warp(startPosition.x, startPosition.y);
	}

	public void setLinearVelocity(Vector2 vel) {
		for (Body b : body.values())
			b.setLinearVelocity(vel);
	}

	public void setLinearVelocity(float x, float y) {
		//for (Body b : body.values())
		//	b.setLinearVelocity(x, y);
        body.get("center").setLinearVelocity(x, y);
	}

	public void applyLinearImpulse(Vector2 imp) {
		body.get(name).applyLinearImpulse(imp,
				body.get(name).getPosition(), true);
	}

	public void applyLinearImpulse(float x, float y) {
		body.get(name).applyLinearImpulse(x, y,
				body.get(name).getPosition().x,
				body.get(name).getPosition().y, true);
	}

    public void setFixedRotation(String name, boolean fixed){
        body.get(name).setFixedRotation(fixed);
    }

	public Vector2 getLinearVelocity() {
		return body.get(name).getLinearVelocity();
	}

	public void setRBUserData(Body body, RBUserData ud) {
		userData.put(body, ud);
	}

	public RBUserData getRBUserData(Body body) {
		return userData.get(body);
	}

    public void setTaskInfo(GameEvent taskInfo){
        eventInfo = taskInfo;
    }

    public GameEvent getEventInfo(){
        return eventInfo;
    }

	public Values<Body> getBodyMap() {
		return body.values();
	}

	public Object getUserData(String name) {
		return body.get(name).getUserData();
	}

	public void setUserData(Entity e, String name) {
		body.get(name).setUserData(e);
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setIsPlayer(boolean player) {
		isPlayer = player;
	}

	public void setBodyActive(boolean active) {
		for (Body b : body.values()) {
			b.setActive(active);
		}
	}

	public void updateSmoothStates(float accumulatorRatio, double oneMinusRatio) {

		for (Body b : body.values()) {
			if (b.getType() != BodyType.StaticBody) {
				float x = (float) (accumulatorRatio * b.getPosition().x + (oneMinusRatio * previousPositions
						.get(b).x));
				float y = (float) (accumulatorRatio * b.getPosition().y + oneMinusRatio
						* previousPositions.get(b).y);
				float rotation = isPlayer ? 0 : (float) (b.getAngle()
						* accumulatorRatio + oneMinusRatio * previousAngle);
				b.setTransform(x, y, rotation);
			}
		}
	}

	public void updatePreviousPosition() {
		for (Body b : body.values()) {
			if (b.getType() != BodyType.StaticBody) {
				previousPositions.get(b).set(b.getPosition());
				previousAngle = b.getAngle();
			}
		}
	}

	public void updateWorldPosition() {
		worldPosition.set(Converters.ToWorld(body.get(name).getPosition()));
	}

	public void setPhysicsListener(ImmediateModePhysicsListener listener) {
		physicsListener = listener;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDynamic() {
		return isDynamic;
	}

	public void setIsDynamic(boolean isDynamic) {
		this.isDynamic = isDynamic;
	}

	public void destroy() {
		body.clear();
		previousPositions.clear();
		userData.clear();
	}

	@Override
	public void dispose() {
		destroy();
	}

	@Override
	public void restart() {
		isDynamic = true;
		isActive = true;
		setBodyActive(true);
		if (physicsListener != null) {
			physicsListener.onRestart();
		}
		enableBody("center");
	}

	public boolean isSubmerged() {
		return submerged;
	}

	public void setSubmerged(boolean submerged) {
		this.submerged = submerged;
	}

    @Override
    public void onNotify(TaskEvent event) {
        if(event.getEventType() == GameEventType.LeftImpulse){
            applyLinearImpulse(-100, 0);
        } else if(event.getEventType() == GameEventType.RightImpulse){
            applyLinearImpulse(100, 0);
        }
    }


    public interface ImmediateModePhysicsListener {

		void beginContact(Entity e, Contact contact, boolean fixtureA);

		void endContact(Entity e, Contact contact, boolean fixtureA);

		void preSolve(Entity e, Contact contact, boolean fixtureA);

		void postSolve(Entity e, Contact contact, boolean fixtureA);

		void onRestart();
	}

}
