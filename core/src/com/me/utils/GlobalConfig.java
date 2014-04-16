package com.me.utils;

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


}
