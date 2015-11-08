package com.me.component;

import com.me.component.interfaces.GameEventObserverComponent;
import com.me.component.interfaces.TaskEventObserverComponent;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;

/**
 * Created by hateftadayon on 10/26/15.
 */
public class SingleParticleComponent extends ParticleComponent implements GameEventObserverComponent, TaskEventObserverComponent {

    private int m_id;

    public SingleParticleComponent(String effect, ParticleType type, int max) {
        super(effect, type, max);
    }

    public SingleParticleComponent(String effect, ParticleType type){
        super(effect, type, 10);
    }

    public SingleParticleComponent(String effect, int id){
        super(effect, ParticleType.SINGLE, 10);
        m_id = id;
    }

    @Override
    public void onNotify(GameEventType event) {
        if (event == GameEventType.AllReachedEnd) {
            if (m_type == ParticleType.PORTAL) {
                start();
            }
        }

    }

    @Override
    public void onNotify(TaskEvent event) {
        if(event.getEventType() == GameEventType.GroundTouch && m_id == event.getEventId()){
            if(m_type == ParticleType.SINGLE){
                start();
            }
        }
    }
}
