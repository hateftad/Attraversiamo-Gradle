package com.me.component;

import java.util.HashMap;

import com.me.component.PlayerComponent.PlayerNumber;
import com.me.utils.LevelConfig;

public class LevelComponent extends BaseComponent {

	public HashMap<PlayerNumber, PlayerComponent> m_finishers = new HashMap<PlayerNumber, PlayerComponent>();
	public int m_nrOfFinishers;
	public boolean m_hasPortal;
	public boolean m_finishFacingLeft;
	
	public LevelComponent(LevelConfig lvlConf){
		m_nrOfFinishers = lvlConf.getNrOfPlayers();
		m_hasPortal = lvlConf.hasPortal();
		m_finishFacingLeft = lvlConf.finishLeft();
	}
	
	public void addFinisher(PlayerComponent player){
		m_finishers.put(player.getPlayerNr(), player);
	}
	
	public boolean allFinished(){
		return m_finishers.size() == m_nrOfFinishers;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		m_finishers.clear();
	}

}
