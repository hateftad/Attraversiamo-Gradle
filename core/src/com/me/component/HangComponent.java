package com.me.component;

public class HangComponent extends BaseComponent {

	public boolean m_hangingLeft = false;
	public boolean m_hangingRight = false;
	public boolean m_isHanging = false;
	public boolean m_release = false;
	public boolean m_climbingUp = false;
	
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void restart() {
		// TODO Auto-generated method stub
		m_hangingLeft = false;
		m_hangingRight = false;
		m_isHanging = false;
		m_release = false;
		m_climbingUp = false;
	}
}
