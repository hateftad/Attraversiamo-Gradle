package com.me.component;

public class LadderClimbComponent extends BaseComponent {

	public boolean m_rightClimb = false;
	public boolean m_leftClimb = false;
	public boolean m_topLadder = false;
	public boolean m_bottomLadder = false;
	public boolean m_goingUp = false;
	
	
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void restart() {
		
		m_rightClimb = false;
		m_leftClimb = false;
		m_topLadder = false;
		m_bottomLadder = false;
		m_goingUp = false;
		
		
	}
}
