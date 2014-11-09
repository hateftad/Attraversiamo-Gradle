package com.me.component;

public class PlayerComponent extends BaseComponent{
	
	public enum State{
		WALKING, IDLE, JUMPING, 
		DYING, JUMPED, HANGING,
		CRAWLING, LYINGDOWN, GETTINGUP
	}
	
	public enum PlayerNumber{
		ONE,
		TWO,
		THREE
	}
	
	private PlayerNumber m_playerNr;
	
	private State m_state = State.IDLE;
	
	private boolean m_facingLeft;
	
	private boolean m_active;
	
	private boolean m_onGround;
	
	private boolean m_canDeactivate;
	
	private boolean m_isFinishedAnimating;
	
	private boolean m_reachedEnd;
	
	public PlayerComponent(String player)
	{
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
	
	public void setCanBecomeInactive(boolean state){
		m_canDeactivate = state;
	}
	
	public boolean canDeActivate(){
		return m_canDeactivate;
	}

	public boolean isFacingLeft() {
		return m_facingLeft;
	}

	public void setFacingLeft(boolean m_facingLeft) {
		this.m_facingLeft = m_facingLeft;
	}
	
	public boolean isActive()
	{
		return m_active;
	}
	
	public void setActive(boolean active)
	{
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
		if(playerNr.equalsIgnoreCase("playerOne")){
			m_playerNr = PlayerNumber.ONE;
		}else if(playerNr.equalsIgnoreCase("playerTwo")){
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
	
	public void rechedEnd(boolean end){
		m_reachedEnd = end;
	}
	
	public boolean hasReachedEnd(){
		return m_reachedEnd;
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		m_facingLeft = false;
		m_reachedEnd = false;
		m_isFinishedAnimating = false;
	}
}
