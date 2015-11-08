package com.me.events;

import com.artemis.Entity;
import com.me.systems.GameEntityProcessingSystem;
import com.me.systems.GameEntityWorld;

/**
 * Created by hateftadayon on 7/13/15.
 */
public abstract class Events {
    public abstract void notify(GameEntityProcessingSystem entityProcessingSystem);
    public abstract void notify(GameEntityWorld entityProcessingSystem);
}
