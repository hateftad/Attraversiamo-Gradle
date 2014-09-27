package com.me.component;

public class QueueComponent extends BaseComponent {

	public enum QueueType{
		MASS,
		JOINT
	}
	
	public QueueType type;
	public float mass;
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
