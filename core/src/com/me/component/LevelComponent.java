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

    private int m_type;
    private int m_taskId;
    private boolean m_finished;

    public LevelComponent(int finishers, int type, int taskId){
        super(finishers);
        m_type = type;
        m_taskId = taskId;
    }

    @Override
    public void onNotify(TaskEvent event) {
        if(event.getEventType() == GameEventType.FinishAnimationDone){
            if (!m_finishers.containsKey(event.getPlayerNr())) {
                m_finishers.put(event.getPlayerNr(), true);
            }
        }
    }

    public int getType() {
        return m_type;
    }

    @Override
    public boolean allFinished() {
        return m_type == RUNOUT ? super.allFinished() : m_finished;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void onNotify(ParticleEvent particleEvent) {
        if(particleEvent.getEventId() == m_taskId) {
            if (particleEvent.getEventType() == GameEventType.PortalParticleFinish) {
                m_finished = true;
            }
        }
    }
}
