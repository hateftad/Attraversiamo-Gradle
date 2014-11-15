package com.me.component;

import com.badlogic.gdx.utils.ObjectMap;
import com.me.component.PlayerComponent.PlayerNumber;
import com.me.component.PlayerComponent.Tasks;
import com.me.utils.LevelConfig;

public class LevelComponent extends BaseComponent {

	public ObjectMap<PlayerNumber, PlayerComponent> m_finishers = new ObjectMap<PlayerNumber, PlayerComponent>();
	public int m_nrOfFinishers;
	public boolean m_hasPortal;
	public boolean m_finishFacingLeft;

	public LevelComponent(LevelConfig lvlConf) {
		m_nrOfFinishers = lvlConf.getNrOfPlayers();
		m_hasPortal = lvlConf.hasPortal();
		m_finishFacingLeft = lvlConf.finishLeft();
	}

	public void addFinisher(PlayerComponent player) {
		m_finishers.put(player.getPlayerNr(), player);
	}

	public boolean isTaskDone(Tasks task) {
		int finished = 0;
		for (PlayerComponent player : m_finishers.values()) {
			if (player.isTaskDone(task)) {
				finished++;
			}
		}
		return m_finishers.size == finished;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void restart() {
		//m_finishers.clear();
	}

}
