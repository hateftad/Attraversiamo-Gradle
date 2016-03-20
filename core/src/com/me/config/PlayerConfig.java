package com.me.config;

import com.badlogic.gdx.math.Vector2;
import com.me.events.states.PlayerState;

public class PlayerConfig {

    private Vector2 m_playerPosition;
    private boolean m_facingleft;
    private boolean m_active;
    private boolean m_canDeactivate;
    private String m_name;
    private PlayerState m_finishAnimation;
 	private String m_skinName;
    private int m_playerNumber;
    private boolean m_finishFacingLeft;

 	public PlayerConfig(){
 		m_playerPosition = new Vector2();
 	}

    public void setFacingleft(boolean m_facingleft) {
        this.m_facingleft = m_facingleft;
    }

    public void setFinishFacingleft(boolean m_facingleft) {
        this.m_finishFacingLeft = m_facingleft;
    }
    public boolean getFinishFacingleft() {
        return this.m_finishFacingLeft;
    }

    public void setActive(boolean m_active) {
        this.m_active = m_active;
    }

    public void setCanDeactivate(boolean m_canDeactivate) {
        this.m_canDeactivate = m_canDeactivate;
    }

    public void setName(String m_name) {
        this.m_name = m_name;
    }

    public void setPlayerPosition(Vector2 position){
        m_playerPosition.set(position);
    }

    public void setPlayerNumber(int number){
        m_playerNumber = number;
    }

    public int getPlayerNumber(){
        return m_playerNumber;
    }

    public Vector2 getPlayerPosition() {
        return m_playerPosition;
    }

    public boolean isFacingleft() {
        return m_facingleft;
    }

    public boolean isActive() {
        return m_active;
    }

    public boolean canDeactivate() {
        return m_canDeactivate;
    }

    public PlayerState getFinishAnimation() {
        return m_finishAnimation;
    }

 	public String getName(){
 		return m_name;
 	}

 	public void setFinishAnimation(String state){
 		
 		if(state.equals("suckin")){
 			m_finishAnimation = PlayerState.SuckIn;
 		}
 		if(state.equals("walkout")){
 			m_finishAnimation = PlayerState.WalkOut;
 		}
 		if(state.equals("runOut")){
 			m_finishAnimation = PlayerState.RunOut;
 		}
 	}
 	
	public void setSkinName(String name){
		m_skinName = name;
	}
	
	public String getSkinName(){
		return m_skinName;
	}
}
