package com.me.systems;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import com.me.component.TaskEventObserverComponent;
import com.me.interfaces.GameEvent;
import com.me.interfaces.GameEventObserverComponent;
import com.me.interfaces.TaskEvent;

/**
 * Created by hateftadayon on 7/9/15.
 */
public class GameEntityWorld extends World {

    private Array<GameEventObserverComponent> m_gameEventObservers;
    private Array<TaskEventObserverComponent> m_taskEventObservers;

    public GameEntityWorld(){
        super();
        m_gameEventObservers = new Array<GameEventObserverComponent>();
        m_taskEventObservers = new Array<TaskEventObserverComponent>();
    }

    public void addObserver(GameEventObserverComponent observerComponent){
        m_gameEventObservers.add(observerComponent);
    }

    public void addObserver(TaskEventObserverComponent observerComponent){
        m_taskEventObservers.add(observerComponent);
    }

    public void removeObserver(GameEventObserverComponent observerComponent){
        m_gameEventObservers.removeValue(observerComponent, false);
    }

    public void onNotify(Entity entity, GameEvent event){
        for(GameEventObserverComponent observerComponent : m_gameEventObservers){
            observerComponent.onNotify(entity, event);
        }
    }

    public void onNotify(Entity entity, TaskEvent event){
        for(TaskEventObserverComponent observerComponent : m_taskEventObservers){
            observerComponent.onNotify(entity, event);
        }
    }

}
