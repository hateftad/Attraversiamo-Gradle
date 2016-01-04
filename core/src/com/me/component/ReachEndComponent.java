package com.me.component;

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
    public void onNotify(TaskEvent event) {
        if(event.getEventType() == GameEventType.InsideFinishArea){
            put(event.getPlayerNr());
        } else if (event.getEventType() == GameEventType.OutsideFinishArea){
            remove(event.getPlayerNr());
        }
    }

    private void put(PlayerComponent.PlayerNumber playerNumber){
        if(!m_finishers.containsKey(playerNumber)){
            m_finishers.put(playerNumber, true);
        }
    }

    private void remove(PlayerComponent.PlayerNumber playerNumber){
        m_finishers.remove(playerNumber);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }
}
