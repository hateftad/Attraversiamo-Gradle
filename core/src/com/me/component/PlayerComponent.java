package com.me.component;

import com.badlogic.gdx.utils.ObjectMap;
import com.me.tasks.Task;
import com.me.tasks.Task.TaskType;

public class PlayerComponent extends BaseComponent {

	public enum State {
		WALKING, IDLE, JUMPING, DYING, JUMPED, HANGING, CRAWLING, LYINGDOWN, GETTINGUP, WAITTILDONE
	}

	public enum PlayerNumber {
		ONE, TWO, THREE
	}


	private ObjectMap<TaskType, Task> m_tasks = new ObjectMap<TaskType, Task>();

	private PlayerNumber m_playerNr;

	private State m_state = State.IDLE;

	private boolean m_facingLeft;

	private boolean m_active;

	private boolean m_onGround;

	private boolean m_canDeactivate;

	private boolean m_isFinishedAnimating;

	public PlayerComponent(String player) {
		setPlayerNr(player);
		setFacingLeft(true);
		setState(State.IDLE);
	}

	public State getState() {
		return m_state;
	}

	public void setState(State m_state) {
		this.m_state = m_state;
	}

	public void setCanBecomeInactive(boolean state) {
		m_canDeactivate = state;
	}

	public boolean canDeActivate() {
		return m_canDeactivate;
	}

	public boolean isFacingLeft() {
		return m_facingLeft;
	}

	public void setFacingLeft(boolean m_facingLeft) {
		this.m_facingLeft = m_facingLeft;
	}

	public boolean isActive() {
		return m_active;
	}

	public void setActive(boolean active) {
		m_active = active;
	}

	public boolean isOnGround() {
		return m_onGround;
	}

	public void setOnGround(boolean onGround) {
		this.m_onGround = onGround;
	}

	public PlayerNumber getPlayerNr() {
		return m_playerNr;
	}

	public void setPlayerNr(String playerNr) {
		if (playerNr.equalsIgnoreCase("playerOne")) {
			m_playerNr = PlayerNumber.ONE;
		} else if (playerNr.equalsIgnoreCase("playerTwo")) {
			m_playerNr = PlayerNumber.TWO;
		}
	}

	@Override
	public void dispose() {

	}

	public boolean isFinishedAnimating() {
		return m_isFinishedAnimating;
	}

	public void setIsFinishedAnimating(boolean isFinished) {
		m_isFinishedAnimating = isFinished;
	}

	public boolean isTaskDone(TaskType task) {
		return (m_tasks.containsKey(task) && m_tasks.get(task).isFinished());
	}
	
	public void doneTask(TaskType taskType){
        if(m_tasks.containsKey(taskType)) {
            m_tasks.get(taskType).finish();
        }
	}

    public void unDoneTask(TaskType taskType){
        if(m_tasks.containsKey(taskType)) {
            m_tasks.get(taskType).unFinish();
        }
    }
	
	public void addTask(Task task){
		m_tasks.put(task.getType(), task);
	}

	@Override
	public void restart() {
		m_facingLeft = false;
		m_isFinishedAnimating = false;
	}
}
