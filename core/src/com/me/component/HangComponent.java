package com.me.component;

public class HangComponent extends BaseComponent {

	public boolean hangingLeft = false;
	public boolean hangingRight = false;
	public boolean isHanging = false;
	public boolean release = false;
	public boolean climbingUp = false;
	
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

    public void notHanging(){
        hangingLeft = false;
        isHanging = false;
        hangingRight = false;
    }

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		hangingLeft = false;
		hangingRight = false;
		isHanging = false;
		release = false;
		climbingUp = false;
	}
}
