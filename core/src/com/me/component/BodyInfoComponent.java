package com.me.component;

import com.me.level.tasks.BodyInfo;

/**
 * Created by hateftadayon on 6/7/15.
 */
public class BodyInfoComponent extends BaseComponent {

    private BodyInfo m_task;
    private boolean m_completed;

    public BodyInfo getTask() {
        return m_task;
    }

    public void setBodyInfo(BodyInfo task){
        m_task = task;
    }

    public boolean isCompleted(){
        return m_completed;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }
}
