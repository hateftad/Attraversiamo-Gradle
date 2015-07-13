package com.me.event;

import com.artemis.Entity;
import com.me.systems.GameEntityProcessingSystem;

/**
 * Created by hateftadayon on 7/13/15.
 */
public abstract class Event {
    public abstract void notify(Entity entity, GameEntityProcessingSystem entityProcessingSystem);
}
