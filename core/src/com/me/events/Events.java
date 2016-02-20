package com.me.events;

import com.me.component.PlayerComponent;
import com.me.systems.GameEntityProcessingSystem;
import com.me.systems.GameEntityWorld;

/**
 * Created by hateftadayon on 7/13/15.
 */
public abstract class Events {
    public abstract void notify(GameEntityProcessingSystem entityProcessingSystem, PlayerComponent.PlayerNumber playerNumber);
    public abstract void notify(GameEntityWorld entityProcessingSystem);
}
