package com.me.manager;

import com.badlogic.gdx.utils.ObjectMap;
import com.me.component.PlayerComponent.PlayerNumber;
import com.me.level.Level;
import com.me.level.tasks.LevelTask;
import com.me.level.tasks.LevelTask.TaskType;
import com.me.utils.LevelConfig;

/**
 * Created by hateftadayon on 6/1/15.
 */
public class LevelManager {


    private boolean m_levelFinished;
    private Level m_currentLevel;


    public LevelManager() {

    }

    public void setLevel(Level level){
        m_currentLevel = level;
    }

    public boolean isTaskDoneForAll(TaskType task) {
        return m_currentLevel.isTaskDoneForAll(task);
    }

    public void addTask(TaskType taskType, LevelTask levelTask){
        m_currentLevel.addTask(taskType, levelTask);
    }

    public void doneTask(PlayerNumber playerNumber, LevelTask task){
        m_currentLevel.doneTask(playerNumber, task);
    }

    public void unDoneTask(PlayerNumber playerNumber, TaskType task){
        m_currentLevel.unDoneTask(playerNumber, task);
    }

    public void setFinishedLevel(){
        m_levelFinished = true;
    }

    public boolean isLevelFinished(){
        return m_levelFinished;
    }

    public boolean charactersFinishingLeft(){
        return m_currentLevel.isfinishFacingLeft();
    }

    public boolean levelHasPortal(){
        return m_currentLevel.hasPortal();
    }

    public int getLevelNumber(){
        return m_currentLevel.getLevelNumber();
    }

    public Level.LevelBoundaries getLevelBoundaries(){
        return m_currentLevel.getLevelBoundaries();
    }



    public void restart() {
        m_currentLevel.restart();
    }
}
