package com.me.level.tasks;

import com.me.interfaces.GameEventType;


/**
 * Created by hateftadayon on 6/9/15.
 */
public class BodyInfo {

    private int m_taskFinishers;

    public int getTaskId() {
        return m_taskId;
    }

    private int m_taskId;
    private GameEventType m_taskType;

    public BodyInfo(int taskFinishers, int taskId,  String taskType){
        m_taskFinishers = taskFinishers;
        m_taskId = taskId;
        setTaskType(taskType);
    }

    private void setTaskType(String taskType){
        if("Door".equalsIgnoreCase(taskType)){
            m_taskType = GameEventType.Door;
        } else if("WaterEngine".equalsIgnoreCase(taskType)){
            m_taskType = GameEventType.WaterEngine;
        }
    }


    public int getTaskFinishers() {
        return m_taskFinishers;
    }

    public GameEventType getEventType() {
        return m_taskType;
    }

}
