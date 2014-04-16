package com.me.utils;

import com.badlogic.gdx.math.Vector2;
import com.me.component.AnimationComponent.State;

public class PlayerConfig {
	
	public Vector2 m_playerPosition;
 	public boolean m_facingleft;
 	public boolean m_active;
 	public boolean m_canDeactivate;
 	public String m_name;
 	public State m_finishAnimation;
 	
 	public PlayerConfig(){
 		m_playerPosition = new Vector2();
 	}
 	
 	public String getName(){
 		return m_name;
 	}
 	
 	public void setFinishAnimation(String state){
 		
 		if(state.equals("suckin")){
 			m_finishAnimation = State.SUCKIN;
 		}
 		if(state.equals("walkout")){
 			m_finishAnimation = State.WALKOUT;
 		}
 	}
}
