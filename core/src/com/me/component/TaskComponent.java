package com.me.component;

import com.me.level.tasks.LevelTask;

/**
 * Created by hateftadayon on 6/7/15.
 */
public class TaskComponent extends BaseComponent {

    private LevelTask m_task;


    public TaskComponent(){
    }

    public TaskComponent(LevelTask task){
        m_task = task;
    }

    public LevelTask getTask() {
        return m_task;
    }

    public void setTask(LevelTask task){
        m_task = task;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }
}
