package com.me.manager;

import com.badlogic.gdx.utils.ObjectMap;
import com.me.component.PlayerComponent;
import com.me.tasks.CharacterTask;
import com.me.utils.LevelConfig;

/**
 * Created by hateftadayon on 6/1/15.
 */
public class LevelManager {

    public enum LevelTaskType {
        LevelFinished
    }

    public ObjectMap<PlayerComponent.PlayerNumber, PlayerComponent> m_finishers = new ObjectMap<PlayerComponent.PlayerNumber, PlayerComponent>();
    public int m_nrOfFinishers;
    public boolean m_hasPortal;
    public boolean m_finishFacingLeft;

    private ObjectMap<LevelTaskType, Boolean> m_levelTasks = new ObjectMap<LevelTaskType, Boolean>();

    public LevelManager(LevelConfig lvlConf) {
        m_nrOfFinishers = lvlConf.getNrOfPlayers();
        m_hasPortal = lvlConf.hasPortal();
        m_finishFacingLeft = lvlConf.finishLeft();
    }

    public void addFinisher(PlayerComponent player) {
        m_finishers.put(player.getPlayerNr(), player);
    }

    public boolean isTaskDoneForAll(CharacterTask.TaskType task) {
        for (PlayerComponent player : m_finishers.values()) {
            if (!player.isTaskDone(task)) {
                return false;
            }
        }
        return true;
    }

    public boolean isTaskDone(LevelTaskType task){
        return (m_levelTasks.containsKey(task) && m_levelTasks.get(task));
    }

    public void doneTask(LevelTaskType task){
        m_levelTasks.put(task, true);
    }

    public void unDoneTask(LevelTaskType task){
        m_levelTasks.put(task, false);
    }

    public void restart() {
        for (PlayerComponent player : m_finishers.values()) {
            player.resetTasks();
        }

        for (LevelTaskType type : m_levelTasks.keys()) {
            unDoneTask(type);
        }
    }

}
