package com.me.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.me.config.LevelConfig;
import com.me.config.PlayerConfig;

/**
 * Created by hateftadayon on 6/23/15.
 */
public class Level {

    public class LevelBoundaries {
        public float minX;
        public float maxX;
        public float minY;
        public float maxY;
        public float minimumLevelY;
    }

    private LevelConfig levelConfig;
    private LevelBoundaries levelBoundaries;


    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean state) {
        isFinished = state;
    }

    private boolean isFinished;

    public Level(LevelConfig levelConfig) {
        this.levelConfig = levelConfig;
        this.levelBoundaries = new LevelBoundaries();
    }

    public void addPlayerPosition(int player, Vector2 position) {
        levelConfig.addPlayerPosition(player, position);
    }

    public Vector2 getPlayerPosition(int player) {
        return levelConfig.getPlayerPosition(player);
    }

    public int getNumberOfFinishers() {
        return levelConfig.getNumberOfPlayers();
    }

    public String getLevelName() {
        return levelConfig.getLevelName();
    }

    public int getLevelNumber() {
        return levelConfig.getLevelNr();
    }

    public int getNextLevel() {
        return levelConfig.getNextLevel();
    }

    public boolean hasPortal() {
        return levelConfig.hasPortal();
    }

    public LevelConfig getLevelConfig() {
        return levelConfig;
    }

    public LevelBoundaries getLevelBoundaries() {
        return levelBoundaries;
    }

    public ObjectMap.Values<PlayerConfig> getPlayerConfigs() {
        return levelConfig.getPlayerConfigs();
    }

    public void restart() {

    }


}
