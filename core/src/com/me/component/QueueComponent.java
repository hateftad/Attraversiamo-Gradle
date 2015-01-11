package com.me.component;

public class QueueComponent extends BaseComponent {

	public enum QueueType{
		MASS,
		MASSTEMP,
		JOINT
	}
	
	public QueueType type;
	public float mass;
	public String bodyName;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}

}
