package com.me.config;

import com.badlogic.gdx.utils.ObjectMap;

public class LevelList {

    private ObjectMap<String, LevelConfig> levels;

    public LevelConfig getLevel(String levelNumber){
        return levels.get(levelNumber);
    }
}
