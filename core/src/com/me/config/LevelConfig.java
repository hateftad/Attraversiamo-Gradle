package com.me.config;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public class LevelConfig {

	private Array<PlayerConfig> m_playerConfigs = new Array<PlayerConfig>();
	private String m_name;
	private Color m_skyLightColor;
	private int m_levelNr;
	private boolean m_finishFacingLeft;
	private boolean m_hasPortal;
	public float m_minX, m_maxX, m_minY;

	public LevelConfig(String name) {
		m_name = name;
	}

    public void addPlayerConfig(PlayerConfig config){
        m_playerConfigs.add(config);
        config.setPlayerNumber(m_playerConfigs.size);
    }

    public Array<PlayerConfig> getPlayerConfigs(){
        return m_playerConfigs;
    }

	public String getLevelName(){
		return m_name;
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
	
	public void finishFacingLeft(boolean facingLeft){
		m_finishFacingLeft = facingLeft;
	}
	
	public boolean finishLeft(){
		return m_finishFacingLeft;
	}
	
	public void setHasPortal(boolean hasPortal){
		m_hasPortal = hasPortal;
	}
	
	public boolean hasPortal(){
		return m_hasPortal;
	}
	
	public void setLimit(float minX, float maxX, float minY){
		m_minX = minX;
		m_maxX = maxX;
		m_minY = minY;
	}
}
