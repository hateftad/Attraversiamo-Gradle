package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.me.component.BackgroundComponent;
import com.me.component.CameraController;
import com.me.component.PhysicsComponent;
import com.me.component.PlayerComponent;
import com.me.ui.InputManager;

public class BackgroundObjectSystem extends EntityProcessingSystem {

    private final CameraController cameraComponent;
    private boolean process = true;

    @Mapper
    ComponentMapper<BackgroundComponent> backgroundComponents;
    @Mapper
    ComponentMapper<PhysicsComponent> physicsComp;

    @SuppressWarnings("unchecked")
	public BackgroundObjectSystem(CameraController cameraComponent) {
		super(Aspect.getAspectForOne(BackgroundComponent.class));
        this.cameraComponent = cameraComponent;
	}

	@Override
	protected void process(Entity e) {
        BackgroundComponent backgroundComponent = backgroundComponents.get(e);
        float v = cameraComponent.getPosition().x * backgroundComponent.getVelocityX() * 0.002f;
        PhysicsComponent physicsComponent = physicsComp.getSafe(e);
        if(InputManager.getInstance().shouldLockCamera()) {
            physicsComponent.setPosition(v, physicsComponent.getPosition().y);
        }
    }

    @Override
    protected boolean checkProcessing() {
        return process;
    }

    public void toggleProcessing(boolean process) {
        this.process = process;
    }
}
