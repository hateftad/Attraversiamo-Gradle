package com.me.tasks;

/**
 * Created by hateftadayon on 6/1/15.
 */
public class LeverTask extends CharacterTask {

    
    @Override
    public CharacterTask createCopy() {
        return new LeverTask();
    }

    @Override
    public TaskType getType() {
        return TaskType.Lever;
    }

    @Override
    public void finish() {

    }

    @Override
    public void unFinish() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void reset() {

    }
}
