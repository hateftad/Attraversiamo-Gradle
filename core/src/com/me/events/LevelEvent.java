package com.me.events;

import com.me.component.PlayerComponent;
import com.me.level.Level;
import com.me.systems.GameEntityProcessingSystem;
import com.me.systems.GameEntityWorld;

/**
 * Created by hateftadayon on 7/16/16.
 */
public class LevelEvent extends Events {

    private Level currentLevel;
    private LevelEventType type;

    public LevelEvent(LevelEventType type) {
        this.type = type;
    }

    public LevelEvent(LevelEventType type, Level currentLevel) {
        this.type = type;
        this.currentLevel = currentLevel;
    }

    @Override
    public void notify(GameEntityProcessingSystem entityProcessingSystem, PlayerComponent.PlayerNumber playerNumber) {

    }

    @Override
    public void notify(GameEntityWorld entityProcessingSystem) {
        entityProcessingSystem.onNotify(this);
    }

    public LevelEventType getType() {
        return type;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }
}
