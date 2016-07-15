package com.me.component;

import com.badlogic.gdx.math.Vector2;

public class GrabComponent extends BaseComponent {

	public Vector2 positionToSet = new Vector2();

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		positionToSet = Vector2.Zero;
	}
}
