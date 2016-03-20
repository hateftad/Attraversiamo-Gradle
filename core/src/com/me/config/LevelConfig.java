package com.me.config;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.me.utils.GameUtils;

public class LevelConfig {

	private ObjectMap<Integer, PlayerConfig> playerConfigs = new ObjectMap<>();
	private String name;
	private Color skyLightColor;
	private int levelNr;
	private boolean hasPortal;
    private int numberOfPlayers;
    private float zoom;
    private int nextLevel;

    public LevelConfig(){}

	public LevelConfig(String name) {
		this.name = name;
	}

    public void addPlayerConfig(PlayerConfig config){
        playerConfigs.put(config.getPlayerNumber(), config);
    }

    public ObjectMap.Values<PlayerConfig> getPlayerConfigs(){
        return playerConfigs.values();
    }

	public String getLevelName(){
		return name;
	}
	public void setZoom(float zoom){
        this.zoom = zoom;
    }
    public float getZoom(){
        return zoom;
    }

	public void setLightColor(String color){
		skyLightColor = GameUtils.getColor(color);
	}

    public void addPlayerPosition(int player, Vector2 position){
        playerConfigs.get(player).setPlayerPosition(position);
    }

    public Vector2 getPlayerPosition(int player){
        return playerConfigs.get(player).getPlayerPosition();
    }
		
	public Color getLightColor(){
		return skyLightColor;
	}
	
	public void dispose(){
        playerConfigs.clear();
	}

	public int getLevelNr() {
		return levelNr;
	}

	public void setLevelNr(int levelNr) {
		this.levelNr = levelNr;
	}

    public void setNextLevel(int level){
        nextLevel = level;
    }

    public int getNextLevel(){
        return nextLevel;
    }

    public void setNumberOfPlayers(int nrOfPlayers){
        numberOfPlayers = nrOfPlayers;
    }

    public int getNumberOfPlayers(){
        return numberOfPlayers;
    }

	public void setHasPortal(boolean hasPortal){
		this.hasPortal = hasPortal;
	}
	
	public boolean hasPortal(){
		return hasPortal;
	}
}
