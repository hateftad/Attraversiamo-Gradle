package com.me.listeners;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.me.component.PhysicsComponent;
import com.me.component.PhysicsComponent.ImmediateModePhysicsListener;

public class PhysicsContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		if (!contact.isTouching())
			return;

		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();

		Entity entityA = (Entity) bodyA.getUserData();
		Entity entityB = (Entity) bodyB.getUserData();

		addBodyToContacts(entityA, contact, true);
		addBodyToContacts(entityB, contact, false);
	}

	private void addBodyToContacts(Entity e, Contact contact, boolean fixtureA) {
		if (e == null)
			return;	
		PhysicsComponent physicsComponent = e.getComponent(PhysicsComponent.class);
		if (physicsComponent == null)
			return;

		ImmediateModePhysicsListener physicsListener = physicsComponent.m_physicsListener;
		if (physicsListener != null)
			physicsListener.beginContact(e, contact, fixtureA);
	}

	@Override
	public void endContact(Contact contact) {
		if(contact.getFixtureA() != null && contact.getFixtureB()!=null){
			Body bodyA = contact.getFixtureA().getBody();
			Body bodyB = contact.getFixtureB().getBody();
		
			Entity entityA = (Entity) bodyA.getUserData();
			Entity entityB = (Entity) bodyB.getUserData();

			removeBodyFromContacts(entityA, contact, true);
			removeBodyFromContacts(entityB, contact, false);
		}
	}
	
	private void removeBodyFromContacts(Entity e, Contact contact, boolean fixtureA) {
		if (e == null)
			return;
		PhysicsComponent physicsComponent = e.getComponent(PhysicsComponent.class);
		if (physicsComponent == null)
			return;

		ImmediateModePhysicsListener physicsListener = physicsComponent.m_physicsListener;
		if (physicsListener != null)
			physicsListener.endContact(e, contact, fixtureA);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		if (!contact.isTouching())
			return;

		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();

		Entity entityA = (Entity) bodyA.getUserData();
		Entity entityB = (Entity) bodyB.getUserData();

		executePreSolveListener(entityA, contact, true);
		executePreSolveListener(entityB, contact, false);
	}

	private void executePreSolveListener(Entity e, Contact contact, boolean fixtureA) {
		if (e == null)
			return;
		PhysicsComponent physicsComponent = e.getComponent(PhysicsComponent.class);
		if (physicsComponent == null)
			return;

		ImmediateModePhysicsListener physicsListener = physicsComponent.m_physicsListener;
		if (physicsListener != null)
			physicsListener.preSolve(e, contact, fixtureA);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated function stub

	}


}
