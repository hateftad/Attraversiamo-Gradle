package com.me.tasks;

/**
 * Created by hateftadayon on 5/30/15.
 */
public class ReachEndTask extends Task {

    @Override
    public Task createCopy() {
        return new ReachEndTask();
    }

    @Override
    public TaskType getType() {
        return TaskType.ReachedEnd;
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
