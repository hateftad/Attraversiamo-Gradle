package com.me.level.tasks;

import com.badlogic.gdx.utils.Array;
import com.me.component.PlayerComponent.PlayerNumber;


/**
 * Created by hateftadayon on 6/9/15.
 */
public class LevelTask {

    public enum TaskType{
        Undefined,
        OpenDoor,
        ReachedEnd,
        WaterEngine
    }

    private static LevelTask m_undefined;

    public static LevelTask noTask(){
        if(m_undefined == null){
            m_undefined = new LevelTask(0, 0, "Undefined");
        }
        return m_undefined;
    }

    private int m_taskId;
    private int m_taskFinishers;
    private TaskType m_taskType;
    private Array<PlayerNumber> m_finishers;

    public LevelTask(int taskId, int taskFinishers, String taskType){
        m_taskId = taskId;
        m_taskFinishers = taskFinishers;
        m_finishers = new Array<PlayerNumber>(taskFinishers);
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
        } else if("Undefined".equalsIgnoreCase(taskType)){
            m_taskType = TaskType.Undefined;
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
