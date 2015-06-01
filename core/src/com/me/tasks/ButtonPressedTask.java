package com.me.tasks;

/**
 * Created by hateftadayon on 5/30/15.
 */
public class ButtonPressedTask extends CharacterTask {


    public ButtonPressedTask(){
        m_isFinished = false;
    }

    @Override
    public CharacterTask createCopy() {
        return new ButtonPressedTask();
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

    @Override
    public void reset() {
        setFinished(false);
    }
}
