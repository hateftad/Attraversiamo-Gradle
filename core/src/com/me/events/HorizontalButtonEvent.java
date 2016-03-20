package com.me.events;

import com.me.component.PlayerComponent;
import com.me.level.tasks.BodyInfo;
import com.me.systems.GameEntityProcessingSystem;
import com.me.utils.Direction;

/**
 * Created by hateftadayon on 7/13/15.
 */
public class HorizontalButtonEvent extends ButtonEvent {

    private Direction direction;
    public HorizontalButtonEvent(BodyInfo bodyInfo) {
        super(bodyInfo);
        direction = new Direction(Direction.Left);
    }

    public void notify(GameEntityProcessingSystem entityProcessingSystem, PlayerComponent.PlayerNumber playerNumber) {
        entityProcessingSystem.notifyObservers(this);
    }

    public void update(){
        direction.LeftToRight();
    }

    public int getDirection(){
        return direction.currentDirection;
    }
}
