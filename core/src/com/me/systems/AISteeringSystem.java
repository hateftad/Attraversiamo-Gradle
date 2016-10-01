package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.me.component.AIComponent;
import com.me.component.PhysicsComponent;
import com.me.component.RayCastComponent;
import com.me.component.SteeringEntity;

import java.util.concurrent.TimeUnit;

/**
 * Created by hateftadayon on 9/28/16.
 */
public class AISteeringSystem extends GameEntityProcessingSystem {

    @Mapper
    private ComponentMapper<PhysicsComponent> physicsComponentMapper;
    @Mapper
    private ComponentMapper<RayCastComponent> rayCastComponentMapper;
    @Mapper
    private ComponentMapper<AIComponent> aiComponentMapper;

    public AISteeringSystem() {
        super(Aspect.getAspectForOne(AIComponent.class));
    }

    @Override
    protected void process(Entity e) {

        AIComponent aiComponent = aiComponentMapper.get(e);
        aiComponent.update(world.delta);
        RayCastComponent rayCastComponent = rayCastComponentMapper.get(e);
        if(aiComponent.getSteeringEntity() != null) {
            SteeringEntity steeringComponent = aiComponent.getSteeringEntity();
            steeringComponent.update(world.delta);
            if(rayCastComponent.hasCollided() && aiComponent.getTarget() == null){
                aiComponent.setTarget(rayCastComponent.getTarget());
            }
            PhysicsComponent physicsComponent = physicsComponentMapper.get(e);
            if (physicsComponent.getBody() != null) {
                physicsComponent.setLinearVelocityDefault(steeringComponent.getLinearVelocity().x, 0);
//                System.out.println(steeringComponent.getLinearVelocity().x);
            }
        }

        if(rayCastComponent.hasCollided() &&
                rayCastComponent.getCollisionTime() + TimeUnit.SECONDS.toMillis(5) < System.currentTimeMillis()){
            rayCastComponent.clearTarget();
            aiComponent.setTarget(null);
            aiComponent.getSteeringEntity().reset();
            System.out.println("clearing target");
        }

    }

}
