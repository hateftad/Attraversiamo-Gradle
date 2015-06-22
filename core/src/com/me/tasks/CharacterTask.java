package com.me.tasks;

/**
 * Created by hateftadayon on 5/30/15.
 */
public abstract class CharacterTask {


    protected boolean m_isFinished = false;

    public abstract CharacterTask createCopy();

    public abstract void finish();

    public abstract void unFinish();

    public abstract boolean isFinished();

    public abstract void reset();

    public void setFinished(boolean finished){
        m_isFinished = finished;
    }
}
