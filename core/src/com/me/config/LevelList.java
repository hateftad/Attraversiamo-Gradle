package com.me.config;

public class LevelList {

    private LevelConfigMap levels;

    public LevelConfig getLevel(String levelNumber){
        return levels.get(levelNumber);
    }
}
