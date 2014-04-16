package com.me.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class UIButton extends Button {
	
	public UIButton(TextButtonStyle style){
		super(style);
		
	}
	
	public boolean contains(int screenX, int screenY){
		
		
		if(screenX < getX()+getWidth() && screenX > getX() &&
				screenY < getY() + getHeight() && screenY > getY()){
			return true;
		}
		return false;
	}

}
