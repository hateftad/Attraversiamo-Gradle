package com.me.utils;

import com.badlogic.gdx.Gdx;

public class GlobalConfig {

	private GlobalConfig(){

	}

	private static GlobalConfig instance = new GlobalConfig();
	public GameConfig config = new GameConfig();
	public static GlobalConfig getInstance( ) {
		return instance;
	}
	public void setConfig(GameConfig config){
		this.config = config;
	}
	public void updateZoom() {
		this.config.zoom = DimensionsHelper.initDimension(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
	}


}
