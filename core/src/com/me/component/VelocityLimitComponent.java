package com.me.component;

public class VelocityLimitComponent extends BaseComponent{
	
	public float walkLimit;
	public float jumpLimit;
	public float ladderLimit;
	public float velocity = 0;
	public float ladderClimbVelocity;
	public float crawlLimit;
	public float pushlimit;
	
	public VelocityLimitComponent(float walkLimit, float jumpLimit, float ladderLimit, float pushLimit)
	{
		this.walkLimit = walkLimit;
		this.jumpLimit = jumpLimit;
		this.ladderLimit = ladderLimit;
		this.pushlimit = pushLimit;
	}
	
	public VelocityLimitComponent(float walkLimit, float jumpLimit, float crawlLimit){
		this.walkLimit = walkLimit;
		this.jumpLimit = jumpLimit;
		this.crawlLimit = crawlLimit;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}

	public void standStill() {
		velocity = 0;
	}
}
