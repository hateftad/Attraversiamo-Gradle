package com.me.component;

public class VelocityLimitComponent extends BaseComponent{
	
	public float m_walkLimit;
	public float m_jumpLimit;
	public float m_ladderLimit;
	public float m_velocity = 0;
	public float m_ladderClimbVelocity;
	public float m_crawlLimit;
	public float m_pushlimit;
	
	public VelocityLimitComponent(float walkLimit, float jumpLimit, float ladderLimit, float pushLimit)
	{
		m_walkLimit = walkLimit;
		m_jumpLimit = jumpLimit;
		m_ladderLimit = ladderLimit;
		m_pushlimit = pushLimit;
	}
	
	public VelocityLimitComponent(float walkLimit, float jumpLimit){
		m_walkLimit = walkLimit;
		m_jumpLimit = jumpLimit;		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}

}
