package com.me.attraversiamo;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.utils.Array;
import com.me.ads.IActivityRequestHandler;
import com.me.screens.GameScreen;
import com.me.screens.LoadingScreen;
import com.me.screens.SplashScreen;
import com.me.config.GameConfig;
import com.me.config.GameConfig.Platform;
import com.me.config.GlobalConfig;

public class Attraversiamo extends Game implements ApplicationListener {
	
	public GameScreen m_gameScreen;
	public LoadingScreen m_loadingScreen;
	private FPSLogger m_fpsLogger;
    public IActivityRequestHandler m_adRequestHandler;
	public Array<InputProcessor> m_processors = new Array<InputProcessor>();
	public InputMultiplexer m_multiPlexer = new InputMultiplexer();

	public Attraversiamo(GameConfig config){
		if(config != null){
			GlobalConfig.getInstance().setConfig(config);
		} else {
			GameConfig conf = new GameConfig();
			conf.platform = Platform.DESKTOP;
			conf.showUI = false;
			conf.timeStep = 1/60f;
		}

	}
	
	public Attraversiamo(){
		this(null);
	}

	@Override
	public void create() {		
		
		m_fpsLogger = new FPSLogger();
		setScreen(new SplashScreen(this));
		m_multiPlexer.setProcessors(m_processors);		
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
