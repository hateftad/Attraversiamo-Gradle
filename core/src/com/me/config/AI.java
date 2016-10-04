package com.me.config;

import com.badlogic.gdx.math.Vector2;
import com.me.events.states.PlayerState;

/**
 * Created by hateftadayon on 10/4/16.
 */
public class AI implements Config {

    AIConfig config;

    public AI(AIConfig config){
        this.config = config;
    }

    @Override
    public boolean canDeactivate() {
        return false;
    }

    @Override
    public String getName() {
        return config.getName();
    }

    @Override
    public boolean isFacingLeft() {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public String getSkinName() {
        return config.getName();
    }

    @Override
    public Vector2 getPosition() {
        return config.getPosition();
    }

    @Override
    public PlayerState getFinishAnimation() {
        return null;
    }

    @Override
    public int getPlayerNumber() {
        return 0;
    }

    @Override
    public boolean isFinishFacingLeft() {
        return false;
    }
}
