package com.me.tasks;

/**
 * Created by hateftadayon on 5/30/15.
 */
public abstract class Task {

    public enum TaskType {
        OpenDoor, Collected, ReachedEnd
    }

    protected boolean m_isFinished = false;

    public abstract TaskType getType();

    public abstract void finish();

    public abstract void unFinish();

    public abstract boolean isFinished();

    public void setFinished(boolean finished){
        m_isFinished = finished;
    }
}
