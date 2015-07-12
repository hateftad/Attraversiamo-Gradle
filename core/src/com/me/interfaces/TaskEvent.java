package com.me.interfaces;

import com.artemis.Entity;
import com.me.level.tasks.BodyInfo;
import com.me.systems.GameEntityProcessingSystem;

/**
 * Created by hateftadayon on 7/12/15.
 */
public class TaskEvent extends GameEvent {

    public int getTaskId() {
        return m_taskId;
    }

    private int m_taskId;

    public TaskEvent(BodyInfo bodyInfo){
        super(bodyInfo.getEventType());
        m_taskId = bodyInfo.getTaskId();
    }

    public void notify(Entity e, GameEntityProcessingSystem entityProcessingSystem){
        entityProcessingSystem.notifyObservers(e, this);
    }
}
