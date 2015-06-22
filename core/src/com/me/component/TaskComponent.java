package com.me.component;

/**
 * Created by hateftadayon on 6/7/15.
 */
public class TaskComponent extends BaseComponent {

    private int m_taskId;

    public void setTaskId(int taskId){
        m_taskId = taskId;
    }

    public int getTaskId(){
        return m_taskId;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }
}
