package com.me.component;

import com.me.utils.LevelConfig;

public class LevelComponent extends BaseComponent {

	public int m_nrOfFinishers;
	public boolean m_finished;
	public boolean m_hasPortal;
	public boolean m_finishFacingLeft;
	
	public LevelComponent(LevelConfig lvlConf){
		m_nrOfFinishers = lvlConf.getNrOfPlayers();
		m_hasPortal = lvlConf.hasPortal();
		m_finishFacingLeft = lvlConf.finishLeft();
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		m_finished = false;
	}

}
