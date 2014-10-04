package com.me.component;

public class RagDollComponent extends BaseComponent {

	public boolean m_activated = true;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		m_activated = true;
	}
}
