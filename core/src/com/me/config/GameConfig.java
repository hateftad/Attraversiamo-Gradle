package com.me.config;

public class GameConfig {
	
	public enum Platform{
		IPHONE,
		ANDROID,
		DESKTOP
	}
	
	public float timeStep;
	public int screenHeight;
	public int screenWidth;
	public boolean showUI;
	public float zoom;
	public Platform platform;

}
