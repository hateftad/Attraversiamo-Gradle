package com.me.component;

import com.badlogic.gdx.math.Vector2;

public class GrabComponent extends BaseComponent {

	public boolean m_gonnaGrab = false;
	public boolean m_grabbed = false;
	public boolean m_lifting = false;
	public boolean m_gettingLifted = false;
	public float handPositionX;
	public boolean aligned;
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {

		m_gonnaGrab = false;
		m_grabbed = false;
		m_lifting = false;
		m_gettingLifted = false;
		handPositionX = 0;
		aligned = false;
	}
}
