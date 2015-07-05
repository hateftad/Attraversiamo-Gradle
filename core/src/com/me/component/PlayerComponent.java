package com.me.component;

public class PlayerComponent extends BaseComponent {

	public enum State {
		WALKING, IDLE, JUMPING, DYING, JUMPED, HANGING, CRAWLING, LYINGDOWN, GETTINGUP, WAITTILDONE
	}

	public enum PlayerNumber {
		ONE, TWO, THREE
	}



	private PlayerNumber m_playerNr;

	private State m_state = State.IDLE;

	private boolean m_facingLeft;

	private boolean m_active;

	private boolean m_onGround;

	private boolean m_canDeactivate;

	private boolean m_isFinishedAnimating;

    private AnimationComponent.AnimState m_finishAnimation;

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

    public void setFinishAnimaiton(AnimationComponent.AnimState animState){
        m_finishAnimation = animState;
    }

    public AnimationComponent.AnimState getFinishAnimation(){
        return m_finishAnimation;
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

	@Override
	public void restart() {
		m_facingLeft = false;
		m_isFinishedAnimating = false;
	}
}
