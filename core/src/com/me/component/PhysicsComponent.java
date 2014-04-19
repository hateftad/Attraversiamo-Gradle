package com.me.component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import com.me.physics.RBUserData;
import com.me.utils.Converters;

public class PhysicsComponent extends BaseComponent {

	private Map<String, Body> m_body = new HashMap<String, Body>();

	private Map<Body, RBUserData> m_userData = new HashMap<Body, RBUserData>();

	private Vector2 m_worldPosition;

	private Map<Body, Vector2> m_previousPositions;

	private Vector2 m_startPosition;

	private boolean m_isActive;

	private String m_name;
	
	private boolean m_isPlayer;

	private boolean m_isDynamic = true;
	
	private	Filter currentFilter;

	public ImmediateModePhysicsListener m_physicsListener;

	public PhysicsComponent(World world, Body b, String name)
	{
		m_name = name;
		m_worldPosition = new Vector2();
		m_body.put(m_name, createBody(world, b));
		setActive(true);
		m_body.get(m_name).setBullet(b.isBullet());
		m_body.get(m_name).setFixedRotation(b.isFixedRotation());
		m_body.get(m_name).setSleepingAllowed(b.isSleepingAllowed());
		m_previousPositions = new HashMap<Body, Vector2>();
		m_previousPositions.put(m_body.get(name), m_body.get(name).getPosition());
		m_startPosition = new Vector2(m_body.get(name).getPosition());
	}

	public Body createBody(World world, Body b){

		BodyDef bodyDef = new BodyDef(); 
		bodyDef.type = b.getType();
		bodyDef.position.set(b.getPosition().x, b.getPosition().y);
		bodyDef.angle = b.getAngle();
		return world.createBody(bodyDef);
	}

	public void createBody(World world, BodyDef bDef, String name)
	{
		m_body.put(name, world.createBody(bDef));
	}

	public void createFixture(PolygonShape pShape, String name)
	{
		m_body.get(name).createFixture(pShape, 1);
	}

	public void createFixture(Fixture fixture, String name)
	{
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

	public void addBody(World ps, Body b, String name)
	{
		m_body.put(name, createBody(ps, b));
		m_body.get(name).setBullet(b.isBullet());
		m_body.get(name).setFixedRotation(b.isFixedRotation());
		m_body.get(name).setSleepingAllowed(b.isSleepingAllowed());
		m_body.get(name).setActive(b.isActive());
		m_previousPositions.put(m_body.get(name), m_body.get(name).getPosition());
	}

	public void makeKinematic()
	{
		m_body.get(m_name).setType(BodyType.KinematicBody);
		m_body.get("feet").setType(BodyType.KinematicBody);

		setIsDynamic(false);
	}

	public void makeDynamic()
	{

		m_body.get(m_name).setType(BodyType.DynamicBody);
		m_body.get("feet").setType(BodyType.DynamicBody);
		setIsDynamic(true);
	}

	public void makeStatic(){
		for(Body b: m_body.values()){
			b.setType(BodyType.StaticBody);
		}
	}

	public boolean isFalling()
	{
		if(m_body.get(m_name).getLinearVelocity().y < -4){
			return true;
		}
		return false;
	}

	public void disableBody(String name){
		
		currentFilter = m_body.get(name).getFixtureList().get(0).getFilterData();
		
		Filter t1 = currentFilter;
		t1.categoryBits = 1;
		
		m_body.get(name).getFixtureList().get(0).setFilterData(t1);
		
	}
	
	public boolean movingForward(){
		if(Math.abs(m_body.get(m_name).getLinearVelocity().x) > 1){
			return true;
		}
		
		return false;
	}

	public Body getBody(String name)
	{
		return m_body.get(name);
	}

	public Body getBody()
	{
		return m_body.get(m_name);
	}

	public void setMass(float mass)
	{
		MassData m = new MassData();
		m.mass = mass;
		m_body.get(m_name).setMassData(m);
	}


	public Vector2 getWorldPosition()
	{
		return m_worldPosition;
	}

	public Vector2 getPosition()
	{
		return m_body.get(m_name).getPosition();
	}

	public void setPosition(String name, Vector2 pos){
		m_body.get(name).setTransform(pos, 0);
	}

	public void setPosition(Vector2 pos)
	{
		m_body.get(m_name).setTransform(pos, 0.0f);
	}

	public void setPosition(float x, float y)
	{
		m_body.get(m_name).setTransform(x, y, 0.0f);
	}

	public void warp(float x, float y){
		for(Body b: m_body.values()){
			b.setLinearVelocity(0, 0);
			b.setTransform(x, y, 0);
		}
	}

	public void warp(String name, Vector2 pos){

		m_body.get(name).setLinearVelocity(0, 0);
		m_body.get(name).setTransform(pos.x, pos.y, 0);
	}

	public BodyType getBodyType(){
		return m_body.get(m_name).getType();
	}

	public void setToStart(){
		warp(m_startPosition.x, m_startPosition.y);
	}

	public void setLinearVelocity(Vector2 vel)
	{
		for(Body b : m_body.values())
			b.setLinearVelocity(vel);
	}

	public void setLinearVelocity(float x, float y)
	{
		for(Body b : m_body.values())
			b.setLinearVelocity(x, y);
	}

	public void applyLinearImpulse(Vector2 imp)
	{
		m_body.get(m_name).applyLinearImpulse(imp, m_body.get(m_name).getPosition(), true);
	}

	public void applyLinearImpulse(float x, float y){
		m_body.get(m_name).applyLinearImpulse(x, y, m_body.get(m_name).getPosition().x, m_body.get(m_name).getPosition().y, true);
	}

	public Vector2 getLinearVelocity()
	{
		return m_body.get(m_name).getLinearVelocity();
	}

	public void setRBUserData(Body body, RBUserData ud)
	{
		m_userData.put(body, ud);
	}

	public RBUserData getRBUserData(Body body)
	{
		return m_userData.get(body);
	}

	public Collection<Body> getBodyMap(){
		return m_body.values();
	}

	public Object getUserData(String name)
	{
		return m_body.get(name).getUserData();
	}

	public void setUserData(Entity e, String name)
	{
		m_body.get(name).setUserData(e);
	}

	public boolean isActive() {
		return m_isActive;
	}

	public void setActive(boolean m_isActive) {
		this.m_isActive = m_isActive;
	}

	public void setIsPlayer(boolean player){
		m_isPlayer = player;
	}
	
	private float m_previousAngle = 0;
	
	public void updateSmoothStates(float accumulatorRatio, double oneMinusRatio){

		for(Body b: m_body.values()){
			if(b.getType() != BodyType.StaticBody){
				float x = (float) (accumulatorRatio * b.getPosition().x + (oneMinusRatio * m_previousPositions.get(b).x));
				float y = (float) (accumulatorRatio * b.getPosition().y + oneMinusRatio * m_previousPositions.get(b).y);
				float rotation = m_isPlayer ? 0 : (float) ( b.getAngle() * accumulatorRatio  + oneMinusRatio * m_previousAngle); 
				b.setTransform(x, y, rotation);
			}
		}
	}

	public void updatePreviousPosition(){
		for(Body b: m_body.values()){
			if(b.getType() != BodyType.StaticBody){
				m_previousPositions.get(b).set(b.getPosition());
				m_previousAngle = b.getAngle();
			}
		}
	}

	public void updateWorldPosition()
	{
		m_worldPosition.set(Converters.ToWorld(m_body.get(m_name).getPosition().x),Converters.ToWorld(m_body.get(m_name).getPosition().y));
	}

	public void setPhysicsListener(ImmediateModePhysicsListener listener)
	{
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
	
	public void destroy(){
		
		m_body.clear();
		m_previousPositions.clear();
		m_userData.clear();
	}
	
	@Override
	public void dispose() {
		destroy();
	}

	public interface ImmediateModePhysicsListener {

		public void beginContact(Entity e, Contact contact, boolean fixtureA);

		public void endContact(Entity e, Contact contact, boolean fixtureA);

		public void preSolve(Entity e, Contact contact, boolean fixtureA);
	}


}
