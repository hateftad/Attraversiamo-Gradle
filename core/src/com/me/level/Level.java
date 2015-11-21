package com.me.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.me.config.LevelConfig;
import com.me.config.PlayerConfig;

import java.util.HashMap;

/**
 * Created by hateftadayon on 6/23/15.
 */
public class Level {

    public class LevelBoundaries{
        public float minX;
        public float maxX;
        public float minY;
    }

    private LevelConfig m_levelConfig;
    private LevelBoundaries m_levelBoundaries;


    public boolean isFinished() {
        return m_isFinished;
    }

    public void setFinished(boolean state){
        m_isFinished = state;
    }

    private boolean m_isFinished;

    public Level(LevelConfig levelConfig){
        m_levelConfig = levelConfig;
        m_levelBoundaries = new LevelBoundaries();

    }

    public void addPlayerPosition(int player, Vector2 position){
        m_levelConfig.addPlayerPosition(player, position);
    }

    public Vector2 getPlayerPosition(int player){
        return m_levelConfig.getPlayerPosition(player);
    }

    public int getNumberOfFinishers(){
        return m_levelConfig.getNumberOfPlayers();
    }

    public String getLevelName(){
        return m_levelConfig.getLevelName();
    }

    public int getLevelNumber(){
        return m_levelConfig.getLevelNr();
    }

    public int getNextLevel(){
        return m_levelConfig.getNextLevel();
    }

    public boolean hasPortal() {
        return m_levelConfig.hasPortal();
    }

    public LevelConfig getLevelConfig() {
        return m_levelConfig;
    }

    public LevelBoundaries getLevelBoundaries(){
        return m_levelBoundaries;
    }

    public ObjectMap.Values<PlayerConfig> getPlayerConfigs(){
        return m_levelConfig.getPlayerConfigs();
    }

    public void restart() {

    }


}
