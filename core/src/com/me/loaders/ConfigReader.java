package com.me.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.me.config.LevelConfig;
import com.me.config.LevelConfigMap;
import com.me.config.LevelList;
import com.me.level.LevelConfigSerializer;

public class ConfigReader {

	private static final String PATH = "data/level/lvlconf.xml";
	private static final String JSONPATH = "data/level/levelconfig.json";
	private LevelList levelList;

	public ConfigReader(){
		Json json = new Json();
		json.setSerializer(LevelConfigMap.class, new LevelConfigSerializer());
		levelList = json.fromJson(LevelList.class, Gdx.files.internal(JSONPATH));
	}
	
	public LevelConfig getLevelConfigByName(String name){
		return levelList.getLevel(name);
	}

}
