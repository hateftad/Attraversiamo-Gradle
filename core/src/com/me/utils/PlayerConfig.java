package com.me.utils;

import com.badlogic.gdx.math.Vector2;
import com.me.component.AnimationComponent.AnimState;

public class PlayerConfig {
	
	public Vector2 m_playerPosition;
 	public boolean m_facingleft;
 	public boolean m_active;
 	public boolean m_canDeactivate;
 	public String m_name;
 	public AnimState m_finishAnimation;
 	public float minimumY;
 	public PlayerConfig(){
 		m_playerPosition = new Vector2();
 	}
 	
 	public String getName(){
 		return m_name;
 	}
 	
 	public void setFinishAnimation(String state){
 		
 		if(state.equals("suckin")){
 			m_finishAnimation = AnimState.SUCKIN;
 		}
 		if(state.equals("walkout")){
 			m_finishAnimation = AnimState.WALKOUT;
 		}
 	}
}
