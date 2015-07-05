package com.me.level;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.me.component.PlayerComponent;
import com.me.level.tasks.LevelTask;
import com.me.utils.LevelConfig;
import com.me.utils.PlayerConfig;

/**
 * Created by hateftadayon on 6/23/15.
 */
public class Level {


    public class LevelBoundaries{
        public float minX;
        public float maxX;
        public float minY;
    }

    private LevelConfig m_levelConfig;
    private ObjectMap<LevelTask.TaskType, LevelTask> taskObjectMap = new ObjectMap<LevelTask.TaskType, LevelTask>();
    private LevelBoundaries m_levelBoundaries;

    public Level(LevelConfig levelConfig){
        m_levelConfig = levelConfig;
        m_levelBoundaries = new LevelBoundaries();
    }

    public String getLevelName(){
        return m_levelConfig.getLevelName();
    }

    public int getLevelNumber(){
        return m_levelConfig.getLevelNr();
    }

    public boolean hasPortal() {
        return m_levelConfig.hasPortal();
    }

    public boolean isfinishFacingLeft() {
        return m_levelConfig.finishLeft();
    }

    public LevelConfig getLevelConfig() {
        return m_levelConfig;
    }

    public LevelBoundaries getLevelBoundaries(){
        return m_levelBoundaries;
    }

    public Array<PlayerConfig> getPlayerConfigs(){
        return m_levelConfig.getPlayerConfigs();
    }

    public boolean isTaskDoneForAll(LevelTask.TaskType task) {
        if(taskObjectMap.containsKey(task)) {
            return taskObjectMap.get(task).isFinished();
        }
        return false;
    }

    public void addTask(LevelTask.TaskType taskType, LevelTask levelTask){
        taskObjectMap.put(taskType, levelTask);
    }

    public void doneTask(PlayerComponent.PlayerNumber playerNr, LevelTask task){
        taskObjectMap.get(task.getTaskType()).playerFinished(playerNr);
    }

    public void unDoneTask(PlayerComponent.PlayerNumber playerNumber, LevelTask.TaskType task){
        taskObjectMap.get(task).playerUnfinished(playerNumber);
    }

    public void restart() {
        for (LevelTask task: taskObjectMap.values()) {
            task.resetTask();
        }
    }


}
