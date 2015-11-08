package com.me.events;

import com.artemis.Entity;
import com.me.component.PlayerComponent;
import com.me.level.tasks.BodyInfo;
import com.me.systems.GameEntityProcessingSystem;
import com.me.systems.GameEntityWorld;

/**
 * Created by hateftadayon on 7/12/15.
 */
public class TaskEvent extends GameEvent {

    private int m_eventId;
    private PlayerComponent.PlayerNumber m_playerNr;
    public int getEventId() {
        return m_eventId;
    }

    public TaskEvent(BodyInfo bodyInfo){
        super(bodyInfo.getEventType());
        m_eventId = bodyInfo.getEventId();
    }

    public TaskEvent(GameEventType type, PlayerComponent.PlayerNumber playerNumber){
        super(type);
        m_playerNr = playerNumber;
    }

    public PlayerComponent.PlayerNumber getPlayerNr(){
        return m_playerNr;
    }

    public void notify(GameEntityProcessingSystem entityProcessingSystem){
        entityProcessingSystem.notifyObservers(this);
    }

    @Override
    public void notify(GameEntityWorld gameEntityWorld) {
        gameEntityWorld.onNotify(this);
    }
}
