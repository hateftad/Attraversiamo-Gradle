package com.me.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.me.component.LevelComponent;
import com.me.component.PlayerComponent.Tasks;

public class LevelConfig {

	public PlayerConfig m_playerOne;
	public PlayerConfig m_playerTwo;
	public boolean facingRight;
	private String m_name;
	private Color m_skyLightColor;
	private int m_nrOfPlayers;
	private int m_levelNr;
	private boolean m_finishFacingLeft;
	private boolean m_hasPortal;
	private LevelComponent m_levelComponent;
	public float m_minX, m_maxX, m_minY;
	private Array<Tasks> m_tasks = new Array<Tasks>();

	public LevelConfig(String name) {
		m_name = name;
	}
	
	public void addTask(String task){
		if(task.equalsIgnoreCase("OpenDoor")){
			m_tasks.add(Tasks.OpenDoor);
		} else if(task.equalsIgnoreCase("TouchEnd")){
			m_tasks.add(Tasks.TouchedEnd);
		}
	}
	
	public Array<Tasks> getTasks(){
		return m_tasks;
	}
	
	public void setLevelComponent(LevelComponent lvlComp){
		m_levelComponent = lvlComp;
	}
	
	public LevelComponent getLevelComponent(){
		return m_levelComponent;
	}
	
	public void setPlayerOneConfig(PlayerConfig p1){
		m_playerOne = p1;
	}
	
	public void setPlayerTwoConfig(PlayerConfig p2){
		m_playerTwo = p2;
	}
	
	public String getLevelName(){
		return m_name;
	}
	
	public void setNrOfPlayers(int nr){
		m_nrOfPlayers = nr;
	}
	
	public int getNrOfPlayers(){
		return m_nrOfPlayers;
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
		m_playerOne = null;
		m_playerTwo = null;
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
