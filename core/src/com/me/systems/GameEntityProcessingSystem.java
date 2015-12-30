package com.me.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.me.events.*;

/**
 * Created by hateftadayon on 7/9/15.
 */
public abstract class GameEntityProcessingSystem extends EntityProcessingSystem {


    public GameEntityProcessingSystem(Aspect aspect) {
        super(aspect);
    }

    public void notifyObservers(TaskEvent taskEvent){
        ((GameEntityWorld)world).onNotify(taskEvent);
    }

    public void notifyObservers(ButtonEvent taskEvent){
        ((GameEntityWorld)world).onNotify(taskEvent);
    }

    public void notifyObservers(BinaryEvent event){
        ((GameEntityWorld)world).onNotify(event);
    }

}
