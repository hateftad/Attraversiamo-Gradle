package com.me.level;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.me.config.LevelConfig;
import com.me.config.LevelConfigMap;
import com.me.config.PlayerConfig;
import com.me.config.PlayerConfigMap;

public class LevelConfigSerializer extends Json.ReadOnlySerializer<LevelConfigMap> {

    @Override
    public LevelConfigMap read(Json json, JsonValue jsonData, Class type) {

        LevelConfigMap levelConfigs = new LevelConfigMap();
        for (int i = 0; i < jsonData.child.size; i++) {
            JsonValue jsonValue = jsonData.child.get(i);
            String name = jsonValue.getString("name");
            LevelConfig levelConfig = new LevelConfig(name);
            levelConfig.setLevelNr(jsonValue.getInt("levelNr"));
            levelConfig.setLightColor(jsonValue.getString("lightColor"));
            levelConfig.setHasPortal(jsonValue.getBoolean("hasPortal"));
            levelConfig.setNumberOfPlayers(jsonValue.getInt("nrOfPlayers"));
            levelConfig.setZoom(jsonValue.getInt("zoom"));
            levelConfig.setNextLevel(jsonValue.getInt("nextLevel"));
            addPlayerConfig(levelConfig, jsonValue.get("player"));
            levelConfigs.put(name, levelConfig);
        }
        return levelConfigs;
    }

    private void addPlayerConfig(LevelConfig levelConfig, JsonValue jsonData) {

        for (JsonValue jsonValue : jsonData) {
            PlayerConfig playerConfig = new PlayerConfig();
            int playerNr = jsonValue.getInt("playerNr");
            playerConfig.setPlayerNumber(playerNr);
            playerConfig.setActive(jsonValue.getBoolean("active"));
            playerConfig.setFinishFacingleft(jsonValue.getBoolean("finishFacingLeft"));
            playerConfig.setFacingleft(jsonValue.getBoolean("facingLeft"));
            playerConfig.setCanDeactivate(jsonValue.getBoolean("canDeactivate"));
            playerConfig.setSkinName(jsonValue.getString("skinName"));
            playerConfig.setName(jsonValue.getString("playerName"));
            levelConfig.addPlayerConfig(playerConfig);
        }
    }

}
