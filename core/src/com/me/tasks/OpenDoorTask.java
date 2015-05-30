package com.me.tasks;

/**
 * Created by hateftadayon on 5/30/15.
 */
public class OpenDoorTask extends Task {


    public OpenDoorTask(){
        m_isFinished = false;
    }

    @Override
    public TaskType getType() {
        return TaskType.OpenDoor;
    }

    @Override
    public void finish() {
        setFinished(true);
    }

    @Override
    public void unFinish() {
        setFinished(false);
    }

    @Override
    public boolean isFinished() {
        return m_isFinished;
    }
}
