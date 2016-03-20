package com.me.level;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.me.config.LevelConfig;

public class LevelConfigSerializer extends Json.ReadOnlySerializer<LevelConfig> {

    @Override
    public LevelConfig read(Json json, JsonValue jsonData, Class type) {

        LevelConfig levelConfig = new LevelConfig(jsonData.getString("name"));
        levelConfig.setLevelNr(jsonData.getInt("levelNr"));
        levelConfig.setLightColor(jsonData.getString("lightColor"));
        levelConfig.setHasPortal(jsonData.getBoolean("hasPortal"));
        levelConfig.setNumberOfPlayers(jsonData.getInt("nrOfPlayers"));
        levelConfig.setZoom(jsonData.getInt("zoom"));
        levelConfig.setNextLevel(jsonData.getInt("nextLevel"));

        return levelConfig;
    }
}
