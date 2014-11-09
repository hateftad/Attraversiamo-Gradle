package com.me.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.me.component.AIComponent;

public class AISystem extends EntityProcessingSystem {

	@SuppressWarnings("unchecked")
	public AISystem(Aspect aspect) {
		super(Aspect.getAspectForOne(AIComponent.class));
		
	}

	@Override
	protected void process(Entity e) {
		
	}
}
