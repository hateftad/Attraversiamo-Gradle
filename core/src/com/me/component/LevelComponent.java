package com.me.component;

import com.me.component.interfaces.ParticleEventObserverComponent;
import com.me.events.GameEventType;
import com.me.events.ParticleEvent;
import com.me.events.TaskEvent;

/**
 * Created by hateftadayon on 7/11/15.
 */
public class LevelComponent extends TaskComponent implements ParticleEventObserverComponent {

    public static final int PORTAL = 1, RUNOUT = 2;

    private int type;
    private int taskId;
    private boolean finished;

    public LevelComponent(int finishers, int type, int taskId){
        super(finishers);
        this.type = type;
        this.taskId = taskId;
    }

    @Override
    public void onNotify(TaskEvent event) {
        if(event.getEventType() == GameEventType.FinishAnimationDone){
            if (!finishers.containsKey(event.getPlayerNr())) {
                finishers.put(event.getPlayerNr(), true);
            }
        }
    }

    public int getType() {
        return type;
    }

    @Override
    public boolean allFinished() {
        return type == RUNOUT ? super.allFinished() : finished;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void onNotify(ParticleEvent particleEvent) {
        if(particleEvent.getEventId() == taskId) {
            if (particleEvent.getEventType() == GameEventType.PortalParticleFinish) {
                finished = true;
            }
        }
    }
}
