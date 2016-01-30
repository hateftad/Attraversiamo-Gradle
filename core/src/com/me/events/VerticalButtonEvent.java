package com.me.events;

import com.me.component.PlayerComponent;
import com.me.level.tasks.BodyInfo;
import com.me.systems.GameEntityProcessingSystem;
import com.me.utils.Direction;

/**
 * Created by hateftadayon on 1/30/16.
 */
public class VerticalButtonEvent extends ButtonEvent {

    private Direction m_direction;
    public VerticalButtonEvent(BodyInfo bodyInfo) {
        super(bodyInfo);
        m_direction = new Direction(Direction.Up);
    }

    public void notify(GameEntityProcessingSystem entityProcessingSystem, PlayerComponent.PlayerNumber playerNumber) {
        entityProcessingSystem.notifyObservers(this);
    }

    public void update(){
        m_direction.upToDown();
    }

    public int getDirection(){
        return m_direction.m_currentDirection;
    }
}
