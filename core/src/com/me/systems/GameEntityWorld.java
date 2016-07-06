package com.me.systems;

import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import com.me.component.EventParticleComponent;
import com.me.component.interfaces.ParticleEventObserverComponent;
import com.me.component.interfaces.TelegramEventObserverComponent;
import com.me.component.interfaces.ButtonStateObserverComponent;
import com.me.component.interfaces.TaskEventObserverComponent;
import com.me.events.*;

/**
 * Created by hateftadayon on 7/9/15.
 */
public class GameEntityWorld extends World {

    private Array<TaskEventObserverComponent> taskEventObservers;
    private Array<ButtonStateObserverComponent> buttonStateEventObservers;
    private Array<TelegramEventObserverComponent> binaryEventObservers;
    private Array<ParticleEventObserverComponent> particleEventObservers;

    public GameEntityWorld(){
        super();
        taskEventObservers = new Array<>();
        buttonStateEventObservers = new Array<>();
        binaryEventObservers = new Array<>();
        particleEventObservers = new Array<>();
    }

    public void addObserver(TaskEventObserverComponent observerComponent){
        taskEventObservers.add(observerComponent);
    }

    public void addObserver(ButtonStateObserverComponent observerComponent){
        buttonStateEventObservers.add(observerComponent);
        taskEventObservers.add(observerComponent);
    }

    public void addObserver(ParticleEventObserverComponent observerComponent){
        taskEventObservers.add(observerComponent);
        particleEventObservers.add(observerComponent);
    }

    public void addObserver(TelegramEventObserverComponent observerComponent){
        taskEventObservers.add(observerComponent);
        binaryEventObservers.add(observerComponent);
    }

    public void onNotify(TaskEvent event){
        for(TaskEventObserverComponent observerComponent : taskEventObservers){
            observerComponent.onNotify(event);
        }
    }

    public void onNotify(ButtonEvent event){
        for(ButtonStateObserverComponent observerComponent : buttonStateEventObservers){
            observerComponent.onNotify(event);
        }
    }

    public void onNotify(TelegramEvent event) {
        for(TelegramEventObserverComponent observerComponent : binaryEventObservers){
            observerComponent.onNotify(event);
        }
    }

    public void onNotify(ParticleEvent particleEvent) {
        for (ParticleEventObserverComponent particleEventObserver : particleEventObservers) {
            particleEventObserver.onNotify(particleEvent);
        }
    }
}
