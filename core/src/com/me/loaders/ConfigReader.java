package com.me.loaders;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.me.utils.LevelConfig;
import com.me.utils.PlayerConfig;

public class ConfigReader {
	
	private XmlReader m_reader;
	private static final String PATH = "data/level/lvlconf.xml";
	
	private Map<String, LevelConfig> m_levelConfigs;
	
	public ConfigReader(){

		m_levelConfigs = new HashMap<String, LevelConfig>();
		m_reader = new XmlReader();

		try {
			Element root = m_reader.parse(Gdx.files.internal(PATH));
			Array<Element> items = root.getChildrenByName("Level");
			for (Element child : items){
				
				LevelConfig config = new LevelConfig(child.getAttribute("name"));
				Array<Element> children = child.getChildrenByName("player");
				for(Element gChild : children){
					if(gChild.get("playerNr").equals("one")){
						PlayerConfig pConfig = new PlayerConfig();
						pConfig.setSkinName(gChild.get("skinName"));
						pConfig.m_active = gChild.getBoolean("active");
						pConfig.m_canDeactivate = gChild.getBoolean("canDeactivate");
						pConfig.m_playerPosition.x = gChild.getChildByName("position").getFloat("x");
						pConfig.m_playerPosition.y = gChild.getChildByName("position").getFloat("y");
						pConfig.m_facingleft = gChild.getBoolean("facingLeft");
						pConfig.setFinishAnimation(gChild.get("finishAnim"));
						pConfig.m_name = gChild.get("playerName");
						config.setPlayerOneConfig(pConfig);
						Element tasks = gChild.getChild(0);
						for(int i=0; i < tasks.getChildCount(); i++){
							String childrenTask = tasks.getChild(i).get("name");
							pConfig.addTask(childrenTask);
						}
					}
					if(gChild.get("playerNr").equals("two")){
						PlayerConfig pConfig = new PlayerConfig();
						pConfig.setSkinName(gChild.get("skinName"));
						pConfig.m_active = gChild.getBoolean("active");
						pConfig.m_canDeactivate = gChild.getBoolean("canDeactivate");
						pConfig.m_playerPosition.x = gChild.getChildByName("position").getFloat("x");
						pConfig.m_playerPosition.y = gChild.getChildByName("position").getFloat("y");
						pConfig.m_facingleft = gChild.getBoolean("facingLeft");
						pConfig.setFinishAnimation(gChild.get("finishAnim"));
						pConfig.m_name = gChild.get("playerName");
						config.setPlayerTwoConfig(pConfig);
                        Element tasks = gChild.getChild(0);
                        for(int i=0; i < tasks.getChildCount(); i++){
                            String childrenTask = tasks.getChild(i).get("name");
                            pConfig.addTask(childrenTask);
                        }
					}
				}
				config.setLevelNr(child.getInt("levelNr"));
				config.setLightColor(child.get("lightColor"));
				config.setNrOfPlayers(child.getInt("nrOfPlayers"));
				config.finishFacingLeft(child.getBoolean("finishFacingLeft"));
				config.setHasPortal(child.getBoolean("hasPortal"));
				
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
