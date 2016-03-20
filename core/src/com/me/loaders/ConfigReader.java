package com.me.loaders;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.me.config.LevelConfig;
import com.me.config.LevelList;
import com.me.config.PlayerConfig;
import com.me.level.LevelConfigSerializer;

public class ConfigReader {
	
	private XmlReader m_reader;
	private static final String PATH = "data/level/lvlconf.xml";
	private static final String JSONPATH = "data/level/levelconfig.json";
	
	private Map<String, LevelConfig> m_levelConfigs;
	
	public ConfigReader(){

		m_levelConfigs = new HashMap<>();
		m_reader = new XmlReader();
//		Json json = new Json();
//		json.setSerializer(LevelConfig.class, new LevelConfigSerializer());
//		LevelList levelList = json.fromJson(LevelList.class, Gdx.files.internal(JSONPATH));


		try {
			Element root = m_reader.parse(Gdx.files.internal(PATH));
			Array<Element> items = root.getChildrenByName("Level");
			for (Element child : items){
				
				LevelConfig config = new LevelConfig(child.getAttribute("name"));
				Array<Element> children = child.getChildrenByName("player");
				for(Element gChild : children){
						PlayerConfig pConfig = new PlayerConfig();
						pConfig.setSkinName(gChild.get("skinName"));
						pConfig.setActive(gChild.getBoolean("active"));
						pConfig.setCanDeactivate(gChild.getBoolean("canDeactivate"));
						pConfig.setFacingleft(gChild.getBoolean("facingLeft"));
						pConfig.setFinishAnimation(gChild.get("finishAnim"));
						pConfig.setName(gChild.get("playerName"));
                        pConfig.setPlayerNumber(gChild.getInt("playerNr"));
						pConfig.setFinishFacingleft(gChild.getBoolean("finishFacingLeft"));
						config.addPlayerConfig(pConfig);
				}
				config.setLevelNr(child.getInt("levelNr"));
				config.setLightColor(child.get("lightColor"));
				config.setHasPortal(child.getBoolean("hasPortal"));
				config.setNumberOfPlayers(child.getInt("nrOfPlayers"));
                config.setZoom(child.getInt("zoom"));
                config.setNextLevel(child.getInt("nextLevel"));
				m_levelConfigs.put(child.getAttribute("name"), config);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public LevelConfig getLevelConfigByName(String name){
		return m_levelConfigs.get(name);
	}

}
