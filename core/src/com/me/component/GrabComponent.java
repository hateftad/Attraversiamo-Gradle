package com.me.component;

public class GrabComponent extends BaseComponent {

	public float handPositionX;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		handPositionX = 0;
	}
}
