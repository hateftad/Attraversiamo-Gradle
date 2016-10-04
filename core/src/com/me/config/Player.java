package com.me.config;

import com.badlogic.gdx.math.Vector2;
import com.me.component.AnimationComponent;
import com.me.config.Config;
import com.me.config.PlayerConfig;
import com.me.events.states.PlayerState;

/**
 * Created by hateftadayon on 7/5/15.
 */
public class Player implements Config {

    private PlayerConfig playerConfig;

    public Player(PlayerConfig playerConfig){
        this.playerConfig = playerConfig;
    }

    public String getName(){
        return playerConfig.getName();
    }

    public boolean canDeactivate(){
        return playerConfig.canDeactivate();
    }

    public boolean isFacingLeft(){
        return playerConfig.isFacingleft();
    }
    public boolean isFinishFacingLeft(){
        return playerConfig.getFinishFacingleft();
    }

    public boolean isActive(){
        return playerConfig.isActive();
    }

    public String getSkinName(){
        return playerConfig.getSkinName();
    }

    public Vector2 getPosition(){
        return playerConfig.getPlayerPosition();
    }

    public int getPlayerNumber(){
        return playerConfig.getPlayerNumber();
    }

    public PlayerState getFinishAnimation(){
        return playerConfig.getFinishAnimation();
    }
}
