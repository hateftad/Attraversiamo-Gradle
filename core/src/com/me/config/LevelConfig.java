package com.me.config;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.HashMap;

public class LevelConfig {

	private ObjectMap<Integer, PlayerConfig> m_playerConfigs = new ObjectMap<Integer, PlayerConfig>();
	private String m_name;
	private Color m_skyLightColor;
	private int m_levelNr;
	private boolean m_hasPortal;
    private int m_numberOfPlayers;
    private float m_zoom;
    private int m_nextLevel;

	public LevelConfig(String name) {
		m_name = name;
	}

    public void addPlayerConfig(PlayerConfig config){
        m_playerConfigs.put(config.getPlayerNumber(), config);
    }

    public ObjectMap.Values<PlayerConfig> getPlayerConfigs(){
        return m_playerConfigs.values();
    }

	public String getLevelName(){
		return m_name;
	}
	public void setZoom(float zoom){
        m_zoom = zoom;
    }
    public float getZoom(){
        return m_zoom;
    }

	public void setLightColor(String color){
				
		if(color.equals("Gray")){
			m_skyLightColor = Color.GRAY;
		}
		if(color.equals("Blue")){
			m_skyLightColor = Color.BLUE;
		}
		if(color.equals("Red")){
			m_skyLightColor = Color.RED;
		}
		if(color.equals("Black")){
			m_skyLightColor = Color.BLACK;
		}
		if(color.equals("Green")){
			m_skyLightColor = Color.GREEN;
		}
		if(color.equals("Clear")){
			m_skyLightColor = Color.CLEAR;
		}
		if(color.equals("Magenta")){
			m_skyLightColor = Color.MAGENTA;
		}
		if(color.equals("Yellow")){
			m_skyLightColor = Color.YELLOW;
		}
		
	}

    public void addPlayerPosition(int player, Vector2 position){
        m_playerConfigs.get(player).setPlayerPosition(position);
    }

    public Vector2 getPlayerPosition(int player){
        return m_playerConfigs.get(player).getPlayerPosition();
    }
		
	public Color getLightColor(){
		return m_skyLightColor;
	}
	
	public void dispose(){
        m_playerConfigs.clear();
	}

	public int getLevelNr() {
		return m_levelNr;
	}

	public void setLevelNr(int m_levelNr) {
		this.m_levelNr = m_levelNr;
	}

    public void setNextLevel(int level){
        m_nextLevel = level;
    }

    public int getNextLevel(){
        return m_nextLevel;
    }

    public void setNumberOfPlayers(int nrOfPlayers){
        m_numberOfPlayers = nrOfPlayers;
    }

    public int getNumberOfPlayers(){
        return m_numberOfPlayers;
    }

	public void setHasPortal(boolean hasPortal){
		m_hasPortal = hasPortal;
	}
	
	public boolean hasPortal(){
		return m_hasPortal;
	}
}
