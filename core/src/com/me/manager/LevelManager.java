package com.me.manager;

import com.badlogic.gdx.utils.ObjectMap;
import com.me.component.PlayerComponent.PlayerNumber;
import com.me.tasks.LevelTask;
import com.me.tasks.LevelTask.TaskType;
import com.me.utils.LevelConfig;

/**
 * Created by hateftadayon on 6/1/15.
 */
public class LevelManager {

    public boolean m_hasPortal;
    public boolean m_finishFacingLeft;
    private boolean m_levelFinished;
    private ObjectMap<TaskType, LevelTask> taskObjectMap = new ObjectMap<TaskType, LevelTask>();
    private boolean m_taskChanged;

    public LevelManager(LevelConfig lvlConf) {
        m_hasPortal = lvlConf.hasPortal();
        m_finishFacingLeft = lvlConf.finishLeft();
    }

    public boolean isTaskDoneForAll(TaskType task) {
        if(taskObjectMap.containsKey(task)) {
            return taskObjectMap.get(task).isFinished();
        }
        return false;
    }

    public void addTask(TaskType taskType, LevelTask levelTask){
        taskObjectMap.put(taskType, levelTask);
    }

    public void doneTask(PlayerNumber playerNr, TaskType taskType){
        taskObjectMap.get(taskType).playerFinished(playerNr);
    }

    public void unDoneTask(PlayerNumber playerNumber, TaskType taskType){
        taskObjectMap.get(taskType).playerUnfinished(playerNumber);
    }

    public void setFinishedLevel(){
        m_levelFinished = true;
    }

    public boolean isLevelFinished(){
        return m_levelFinished;
    }

    public void restart() {
        for (LevelTask task: taskObjectMap.values()) {
            task.resetTask();
        }
    }

    public void setTaskChanged(boolean taskChanged){
        m_taskChanged = taskChanged;
    }

}
