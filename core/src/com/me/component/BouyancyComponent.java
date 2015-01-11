package com.me.component;

import com.badlogic.gdx.math.Vector2;

public class BouyancyComponent extends BaseComponent {

	//com.badlogic.gdx.math.Vector2 surfaceNormal, Vector2 fluidVelocity, Vector2 gravity,
	//float surfaceHeight, float fluidDensity, float linearDrag, float angularDrag

	private float m_angularDrag;
	private float m_linearDrag;
	private Vector2 m_fluidVelocity;

	public BouyancyComponent(Vector2 fluidVelocity, float linearDrag, float angularDrag){
		m_fluidVelocity = fluidVelocity;
		m_linearDrag = linearDrag;
		m_angularDrag = angularDrag;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub

	}

	public float getAngularDrag() {
		return m_angularDrag;
	}

	public float getLinearDrag() {
		return m_linearDrag;
	}

	public Vector2 getFluidVelocity(){
		return m_fluidVelocity;
	}
}
