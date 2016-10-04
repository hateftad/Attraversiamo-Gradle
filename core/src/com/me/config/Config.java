package com.me.config;

import com.badlogic.gdx.math.Vector2;
import com.me.events.states.PlayerState;

/**
 * Created by hateftadayon on 10/4/16.
 */
public interface Config {

    boolean canDeactivate();

    String getName();

    boolean isFacingLeft();

    boolean isActive();

    String getSkinName();

    Vector2 getPosition();

    PlayerState getFinishAnimation();

    int getPlayerNumber();

    boolean isFinishFacingLeft();
}
