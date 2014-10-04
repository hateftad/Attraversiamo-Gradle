package com.me.component;

public class JumpComponent extends BaseComponent {

	public boolean m_jumped = false;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		m_jumped = false;
		
	} 
	
	
}
