package com.me;

import com.badlogic.gdx.math.Vector2;
import com.me.component.AnimationComponent;
import com.me.config.PlayerConfig;

/**
 * Created by hateftadayon on 7/5/15.
 */
public class Player {

    private PlayerConfig m_playerConfig;

    public Player(PlayerConfig playerConfig){
        m_playerConfig = playerConfig;
    }

    public String getName(){
        return m_playerConfig.getName();
    }

    public boolean canDeactivate(){
        return m_playerConfig.canDeactivate();
    }

    public boolean isFacingLeft(){
        return m_playerConfig.isFacingleft();
    }

    public boolean isActive(){
        return m_playerConfig.isActive();
    }

    public String getSkinName(){
        return m_playerConfig.getSkinName();
    }

    public Vector2 getPosition(){
        return m_playerConfig.getPlayerPosition();
    }

    public int getPlayerNumber(){
        return m_playerConfig.getPlayerNumber();
    }

    public AnimationComponent.AnimState getFinishAnimation(){
        return m_playerConfig.getFinishAnimation();
    }
}
