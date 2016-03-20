package com.me.component;

import com.badlogic.gdx.utils.ObjectMap;
import com.me.component.interfaces.TaskEventObserverComponent;

/**
 * Created by hateftadayon on 7/11/15.
 */
public abstract class TaskComponent extends BaseComponent implements TaskEventObserverComponent {

    protected int nrfinishers;
    protected ObjectMap<PlayerComponent.PlayerNumber, Boolean> finishers;

    public TaskComponent(int finishers) {
        this.nrfinishers = finishers;
        this.finishers = new ObjectMap<>(nrfinishers);
    }

    public boolean allFinished() {
        return finishers.size == nrfinishers;
    }
}
