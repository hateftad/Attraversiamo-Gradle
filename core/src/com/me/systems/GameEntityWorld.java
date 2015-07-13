package com.me.systems;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import com.me.component.ButtonStateObserverComponent;
import com.me.component.MultiStateObserverComponent;
import com.me.component.TaskEventObserverComponent;
import com.me.event.*;
import com.me.component.GameEventObserverComponent;

/**
 * Created by hateftadayon on 7/9/15.
 */
public class GameEntityWorld extends World {

    private Array<GameEventObserverComponent> m_gameEventObservers;
    private Array<TaskEventObserverComponent> m_taskEventObservers;
    private Array<ButtonStateObserverComponent> m_buttonStateEventObservers;

    public GameEntityWorld(){
        super();
        m_gameEventObservers = new Array<GameEventObserverComponent>();
        m_taskEventObservers = new Array<TaskEventObserverComponent>();
        m_buttonStateEventObservers = new Array<ButtonStateObserverComponent>();
    }

    public void addObserver(GameEventObserverComponent observerComponent){
        m_gameEventObservers.add(observerComponent);
    }

    public void addObserver(TaskEventObserverComponent observerComponent){
        m_taskEventObservers.add(observerComponent);
    }

    public void addObserver(ButtonStateObserverComponent observerComponent){
        m_buttonStateEventObservers.add(observerComponent);
    }

    public void removeObserver(GameEventObserverComponent observerComponent){
        m_gameEventObservers.removeValue(observerComponent, false);
    }

    public void onNotify(GameEventType eventType){
        for(GameEventObserverComponent observerComponent : m_gameEventObservers){
            observerComponent.onNotify(eventType);
        }
    }

    public void onNotify(Entity entity, TaskEvent event){
        for(TaskEventObserverComponent observerComponent : m_taskEventObservers){
            observerComponent.onNotify(entity, event);
        }
    }

    public void onNotify(Entity entity, ButtonEvent event){
        for(ButtonStateObserverComponent observerComponent : m_buttonStateEventObservers){
            observerComponent.onNotify(entity, event);
        }
    }

}
