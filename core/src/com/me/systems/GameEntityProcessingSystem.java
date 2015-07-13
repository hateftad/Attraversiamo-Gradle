package com.me.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.me.event.ButtonEvent;
import com.me.event.GameEvent;
import com.me.event.MultiStateEvent;
import com.me.event.TaskEvent;

/**
 * Created by hateftadayon on 7/9/15.
 */
public abstract class GameEntityProcessingSystem extends EntityProcessingSystem {


    public GameEntityProcessingSystem(Aspect aspect) {
        super(aspect);
    }

    public void notifyObservers(Entity entity, GameEvent event){
        ((GameEntityWorld)world).onNotify(entity, event);
    }

    public void notifyObservers(Entity entity, TaskEvent taskEvent){
        ((GameEntityWorld)world).onNotify(entity, taskEvent);
    }

    public void notifyObservers(Entity entity, MultiStateEvent taskEvent){
        ((GameEntityWorld)world).onNotify(entity, taskEvent);
    }

    public void notifyObservers(Entity entity, ButtonEvent taskEvent){
        ((GameEntityWorld)world).onNotify(entity, taskEvent);
    }

}