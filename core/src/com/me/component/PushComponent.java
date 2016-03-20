package com.me.component;

public class PushComponent extends BaseComponent {

	public boolean pushLeft = false;
	public boolean pushRight = false;
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		pushLeft = false;
		pushRight = false;
	}
}
