package com.me.component;

public class PushComponent extends BaseComponent {

	public boolean m_pushLeft = false;
	public boolean m_pushRight = false;
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		m_pushLeft = false;
		m_pushRight = false;
	}
}
