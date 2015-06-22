package com.me.component;

public class QueueComponent extends BaseComponent {

	public enum QueueType{
		Mass,
		TempMass,
		Joint,
        BodyState
	}
	
	public QueueType type;
	public float mass;
	public String bodyName;
    public boolean active;


	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}

}
