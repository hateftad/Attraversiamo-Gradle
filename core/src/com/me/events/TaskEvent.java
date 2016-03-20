package com.me.events;

import com.me.component.PlayerComponent;
import com.me.level.tasks.BodyInfo;
import com.me.systems.GameEntityProcessingSystem;
import com.me.systems.GameEntityWorld;

/**
 * Created by hateftadayon on 7/12/15.
 */
public class TaskEvent extends GameEvent {

    private int eventId;
    private PlayerComponent.PlayerNumber playerNr;
    public TaskEvent(BodyInfo bodyInfo){
        super(bodyInfo.getEventType());
        eventId = bodyInfo.getEventId();
        playerNr = PlayerComponent.PlayerNumber.NONE;
    }

    public TaskEvent(GameEventType type, PlayerComponent.PlayerNumber playerNumber){
        super(type);
        playerNr = playerNumber;
    }

    public TaskEvent(GameEventType type){
        super(type);
    }

    public int getEventId() {
        return eventId;
    }

    public PlayerComponent.PlayerNumber getPlayerNr(){
        return playerNr;
    }

    public void notify(GameEntityProcessingSystem entityProcessingSystem, PlayerComponent.PlayerNumber playerNumber){
        playerNr = playerNumber;
        entityProcessingSystem.notifyObservers(this);
    }

    @Override
    public void notify(GameEntityWorld gameEntityWorld) {
        gameEntityWorld.onNotify(this);
    }
}
