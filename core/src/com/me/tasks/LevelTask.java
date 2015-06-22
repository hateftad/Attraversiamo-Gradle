package com.me.tasks;

import com.badlogic.gdx.utils.Array;
import com.me.component.PlayerComponent.PlayerNumber;


/**
 * Created by hateftadayon on 6/9/15.
 */
public class LevelTask {

    public enum TaskType{
        OpenDoor,
        ReachedEnd,
        WaterEngine
    }

    private int m_taskId;
    private int m_taskFinishers;
    private TaskType m_taskType;
    private Array<PlayerNumber> m_finishers;

    public LevelTask(int taskId, int taskFinishers, String taskType){
        m_taskId = taskId;
        m_taskFinishers = taskFinishers;
        m_finishers = new Array<PlayerNumber>();
        setTaskType(taskType);
    }

    public void playerFinished(PlayerNumber playerNr){
        if(!m_finishers.contains(playerNr, true)) {
            m_finishers.add(playerNr);
        }
    }

    public void playerUnfinished(PlayerNumber playerNr){
        m_finishers.removeValue(playerNr, true);
    }

    private void setTaskType(String taskType){
        if("Door".equalsIgnoreCase(taskType)){
            m_taskType = TaskType.OpenDoor;
        } else if("ReachEnd".equalsIgnoreCase(taskType)){
            m_taskType = TaskType.ReachedEnd;
        } else if("WaterEngine".equalsIgnoreCase(taskType)){
            m_taskType = TaskType.WaterEngine;
        }
    }

    public int getTaskId() {
        return m_taskId;
    }

    public int getTaskFinishers() {
        return m_taskFinishers;
    }

    public TaskType getTaskType() {
        return m_taskType;
    }

    public boolean isFinished(){
        return m_finishers.size == m_taskFinishers;
    }

    public void resetTask(){
        m_finishers.clear();
    }
}
