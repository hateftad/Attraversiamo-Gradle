package com.me.component;

public class JumpComponent extends BaseComponent {

	public boolean jumped = false;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		jumped = false;
		
	} 
	
	
}
