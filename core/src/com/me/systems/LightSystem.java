package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.graphics.Color;
import com.me.component.LightComponent;

/**
 * Created by hateftadayon on 1/7/16.
 */
public class LightSystem extends GameEntityProcessingSystem {

    @Mapper
    ComponentMapper<LightComponent> lightComps;

    @SuppressWarnings("unchecked")
    public LightSystem() {
        super(Aspect.getAspectForOne(LightComponent.class));
    }

    @Override
    protected void process(Entity entity) {
        updateLights(lightComps.get(entity));
    }

    private void updateLights(LightComponent light){
        if(light.getName().equals("portalLight")){
            float a = light.getAlpha();

//            if(!currentLevel.isFinished()){
//                if(a >= 1){
//                    light.setColor(Color.RED);
//                    inc = -0.01f;
//                } else if(a < 0.1f){
//                    inc = 0.01f;
//                }
//                light.setAlpha(a + inc);
//
//            } else {
//                light.setAlpha(1);
//                light.setColor(Color.GREEN);
//                world.getSystem(CameraSystem.class).getRayHandler().setAmbientLight(1f);
//            }
        }
    }

}
