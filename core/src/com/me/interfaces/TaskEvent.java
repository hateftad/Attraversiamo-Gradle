package com.me.interfaces;

import com.me.level.tasks.BodyInfo;

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


}
