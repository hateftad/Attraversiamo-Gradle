package com.me.systems;

import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import com.me.component.interfaces.TelegramEventObserverComponent;
import com.me.component.interfaces.ButtonStateObserverComponent;
import com.me.component.interfaces.TaskEventObserverComponent;
import com.me.events.*;

/**
 * Created by hateftadayon on 7/9/15.
 */
public class GameEntityWorld extends World {

    private Array<TaskEventObserverComponent> m_taskEventObservers;
    private Array<ButtonStateObserverComponent> m_buttonStateEventObservers;
    private Array<TelegramEventObserverComponent> m_binaryEventObservers;

    public GameEntityWorld(){
        super();
        m_taskEventObservers = new Array<TaskEventObserverComponent>();
        m_buttonStateEventObservers = new Array<ButtonStateObserverComponent>();
        m_binaryEventObservers = new Array<TelegramEventObserverComponent>();
    }


    public void addObserver(TaskEventObserverComponent observerComponent){
        m_taskEventObservers.add(observerComponent);
    }

    public void addObserver(ButtonStateObserverComponent observerComponent){
        m_buttonStateEventObservers.add(observerComponent);
    }

    public void addObserver(TelegramEventObserverComponent observerComponent){
        m_binaryEventObservers.add(observerComponent);
    }

    public void onNotify(TaskEvent event){
        for(TaskEventObserverComponent observerComponent : m_taskEventObservers){
            observerComponent.onNotify(event);
        }
    }

    public void onNotify(ButtonEvent event){
        for(ButtonStateObserverComponent observerComponent : m_buttonStateEventObservers){
            observerComponent.onNotify(event);
        }
    }

    public void onNotify(TelegramEvent event) {
        for(TelegramEventObserverComponent observerComponent : m_binaryEventObservers){
            observerComponent.onNotify(event);
        }
    }
}
