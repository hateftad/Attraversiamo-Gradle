package com.me.config;

import com.badlogic.gdx.math.Vector2;
import com.me.events.states.PlayerState;

public class PlayerConfig {

    private Vector2 playerPosition;
    private boolean facingleft;
    private boolean active;
    private boolean canDeactivate;
    private String name;
    private PlayerState finishAnimation;
 	private String skinName;
    private int playerNumber;
    private boolean finishFacingLeft;

 	public PlayerConfig(){
 		playerPosition = new Vector2();
 	}

    public void setFacingleft(boolean facingleft) {
        this.facingleft = facingleft;
    }

    public void setFinishFacingleft(boolean facingleft) {
        this.finishFacingLeft = facingleft;
    }
    public boolean getFinishFacingleft() {
        return this.finishFacingLeft;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCanDeactivate(boolean canDeactivate) {
        this.canDeactivate = canDeactivate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayerPosition(Vector2 position){
        playerPosition.set(position);
    }

    public void setPlayerNumber(int number){
        playerNumber = number;
    }

    public int getPlayerNumber(){
        return playerNumber;
    }

    public Vector2 getPlayerPosition() {
        return playerPosition;
    }

    public boolean isFacingleft() {
        return facingleft;
    }

    public boolean isActive() {
        return active;
    }

    public boolean canDeactivate() {
        return canDeactivate;
    }

    public PlayerState getFinishAnimation() {
        return finishAnimation;
    }

 	public String getName(){
 		return name;
 	}

 	public void setFinishAnimation(String state){
 		
 		if(state.equals("suckin")){
 			finishAnimation = PlayerState.SuckIn;
 		}
 		if(state.equals("walkout")){
 			finishAnimation = PlayerState.WalkOut;
 		}
 		if(state.equals("runOut")){
 			finishAnimation = PlayerState.RunOut;
 		}
 	}
 	
	public void setSkinName(String name){
		skinName = name;
	}
	
	public String getSkinName(){
		return skinName;
	}
}
