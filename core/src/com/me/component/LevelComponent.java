package com.me.component;

import com.badlogic.gdx.utils.ObjectMap;
import com.me.component.PlayerComponent.PlayerNumber;
import com.me.component.PlayerComponent.Tasks;
import com.me.utils.LevelConfig;

public class LevelComponent extends BaseComponent {

	public enum LevelTasks{
		PlayingFinishAnimation
	}

	public ObjectMap<PlayerNumber, PlayerComponent> m_finishers = new ObjectMap<PlayerNumber, PlayerComponent>();
	public int m_nrOfFinishers;
	public boolean m_hasPortal;
	public boolean m_finishFacingLeft;

	private ObjectMap<LevelTasks, Boolean> m_levelTasks = new ObjectMap<LevelTasks, Boolean>();

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

	public boolean isTaskDone(LevelTasks task){
		return (m_levelTasks.containsKey(task) && m_levelTasks.get(task));
	}

	public void doneTask(LevelTasks task){
		m_levelTasks.put(task, true);
	}

	public void unDoneTask(LevelTasks task){
		m_levelTasks.put(task, false);
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
