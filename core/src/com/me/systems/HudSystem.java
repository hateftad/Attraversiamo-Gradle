package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.me.component.HudComponent;
import com.me.events.LevelEvent;
import com.me.events.LevelEventType;

public class HudSystem extends GameEntityProcessingSystem{

	@Mapper
    ComponentMapper<HudComponent> hudComponentComponentMapper;
	
	public HudSystem() {
		super(Aspect.getAspectForOne(HudComponent.class));
	}

	@Override
	protected void process(Entity entity) {

        HudComponent hudComponent = hudComponentComponentMapper.get(entity);
        hudComponent.update(world.delta);
        if (hudComponent.restartPressed()){
            notifyObservers(new LevelEvent(LevelEventType.OnRestart));
            hudComponent.setRestartPRessed(false);
        }
        if(hudComponent.isPaused()){
            notifyObservers(new LevelEvent(LevelEventType.OnPaused));
        }
    }
}
