package com.me.loaders;

import com.me.level.Player;
import com.me.attraversiamo.Attraversiamo;
import com.me.level.Level;
import com.me.listeners.LoadCompletionListener;
import com.me.screens.GameScreen;
import com.me.systems.LevelSystem;
import com.me.ui.InputManager;
import com.me.config.PlayerConfig;

public class BackgroundLoader{

	private static final String LEVEL = "level";
	private LoadCompletionListener m_listener;
	private EntityLoader m_loader;
	private ConfigReader m_configLoader;
	private int m_level;
	private Attraversiamo m_game;
    private Level m_currentLevel;

	public BackgroundLoader(Attraversiamo game){
		m_loader = new EntityLoader();
		m_game = game;
		m_configLoader = new ConfigReader();
	}

	public void setLevel(int level){
		m_level = level;
	}

	public void setListener(LoadCompletionListener listener){
		m_listener = listener;
	}

	public void run() {
		try{
			doRun();
		} catch(Exception e){
			System.out.println("Exception while loading " + e.getMessage());
		}
		finally{
			m_listener.onComplete();
		}
	}

	public void doRun(){

        m_currentLevel = new Level(m_configLoader.getLevelConfigByName(LEVEL+m_level));

		if(m_game.m_gameScreen != null){
			stopProcessingSystems();
			clearLevel();
		} else {
			m_game.m_gameScreen = new GameScreen(m_game);
		}

		m_loader.loadLevel(m_currentLevel, m_game.m_gameScreen.getEntityWorld(), m_game.m_gameScreen.getPhysicsSystem().getWorld(), m_game.m_gameScreen.getCameraSystem().getRayHandler());

		for(PlayerConfig playerConfig : m_currentLevel.getPlayerConfigs()){
            Player player = new Player(playerConfig);
			m_loader.loadCharacter(player, m_game.m_gameScreen.getEntityWorld(), m_game.m_gameScreen.getPhysicsSystem().getWorld());
            InputManager.getInstance().setSelectedPlayer(player.getPlayerNumber(), player.isActive());
		}
			
		m_loader.dispose();
		startProcessingSystems();

	}

	private void startProcessingSystems() {
		
		LevelSystem lvlSystem = m_game.m_gameScreen.getEntityWorld().getSystem(LevelSystem.class);
        lvlSystem.setCurrentLevel(m_currentLevel);
		lvlSystem.setProcessing(true);
		
		m_game.m_gameScreen.getPhysicsSystem().toggleProcessing(true);
		
		m_game.m_gameScreen.getCameraSystem().toggleProcess(true);
		m_game.m_gameScreen.getCameraSystem().setLevelBoundariesForCamera(m_currentLevel.getLevelBoundaries());
		
		m_game.m_gameScreen.getPlayerSystem().toggleProcessing(true);
		m_game.m_gameScreen.getPlayerSystem().restartSystem();
		m_game.m_gameScreen.toggleProcessingOnSystems(true);
	}

	private void clearLevel(){

		m_game.m_gameScreen.clear();
		m_game.m_gameScreen = new GameScreen(m_game);

	}
	
	private void stopProcessingSystems(){
		m_game.m_gameScreen.toggleProcessingOnSystems(false);
	}

}
