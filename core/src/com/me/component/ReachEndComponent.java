package com.me.component;

import com.artemis.Entity;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;

/**
 * Created by hateftadayon on 7/11/15.
 */
public class ReachEndComponent extends TaskComponent {

    public ReachEndComponent(int finishers) {
        super(finishers);
    }

    @Override
    public void onNotify(Entity entity, TaskEvent event) {
        if(event.getEventType() == GameEventType.InsideFinishArea){
            m_finishers.add(entity.getComponent(PlayerComponent.class).getPlayerNr());
        } else if (event.getEventType() == GameEventType.OutsideFinishArea){
            m_finishers.removeValue(entity.getComponent(PlayerComponent.class).getPlayerNr(), false);
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }
}
