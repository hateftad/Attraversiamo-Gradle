package com.me.component;

public class LadderClimbComponent extends BaseComponent {

	public boolean rightClimb = false;
	public boolean leftClimb = false;
	public boolean topLadder = false;
	public boolean bottomLadder = false;
	public boolean goingUp = false;

	@Override
	public void dispose() {

	}



	@Override
	public void restart() {
		rightClimb = false;
		leftClimb = false;
		topLadder = false;
		bottomLadder = false;
		goingUp = false;
	}
}
