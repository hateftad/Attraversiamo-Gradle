package com.me.loaders;

import com.me.attraversiamo.Attraversiamo;
import com.me.component.PlayerComponent.PlayerNumber;
import com.me.listeners.LoadCompletionListener;
import com.me.screens.GameScreen;
import com.me.systems.LevelSystem;
import com.me.systems.PlayerOneSystem;
import com.me.systems.PlayerTwoSystem;
import com.me.ui.InputManager;
import com.me.utils.Converters;
import com.me.utils.LevelConfig;
import com.me.utils.PlayerConfig;

public class BackgroundLoader{

	private static final String LEVEL = "level";
	private LoadCompletionListener m_listener;
	private EntityLoader m_loader;
	private ConfigReader m_configLoader;
	private int m_level;
	private Attraversiamo m_game;
	private LevelConfig m_levelConfig;
	
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
			System.out.println("Exceptio while loading " + e.getMessage());
		}
		finally{
			m_listener.onComplete();
		}
	}

	public void doRun(){

		m_levelConfig = m_configLoader.getLevelConfigByName(LEVEL+m_level);
		
		if(m_game.m_gameScreen != null){
			stopProcessingSystems();
			clearLevel();
		} else {
			m_game.m_gameScreen = new GameScreen(m_game);
		}

		
		m_loader.loadLevel(m_levelConfig, m_game.m_gameScreen.getEntityWorld(), m_game.m_gameScreen.getPhysicsSystem().getWorld(), 
				m_game.m_gameScreen.getCameraSystem().getRayHandler());
		
		if(m_levelConfig.m_playerOne != null){
			m_loader.loadCharacter(m_levelConfig, m_game.m_gameScreen.getEntityWorld(), m_game.m_gameScreen.getPhysicsSystem().getWorld(), 
					m_game.m_gameScreen.getCameraSystem().getRayHandler(), PlayerNumber.ONE);
			setupCharacter(m_levelConfig.m_playerOne, 1);
		}	
			
		if(m_levelConfig.m_playerTwo != null){
			m_loader.loadCharacter(m_levelConfig, m_game.m_gameScreen.getEntityWorld(), m_game.m_gameScreen.getPhysicsSystem().getWorld(),
					m_game.m_gameScreen.getCameraSystem().getRayHandler(), PlayerNumber.TWO);
			setupCharacter(m_levelConfig.m_playerTwo, 2);
		}
		
		m_loader.dispose();
		startProcessingSystems();
		
	}
	
	private void setupCharacter(PlayerConfig playerCfg, int playerNr){
		
		switch (playerNr) {
		case 1:
			playerCfg.minimumY = Converters.ToBox(m_levelConfig.m_minY);
			m_game.m_gameScreen.getEntityWorld().getSystem(PlayerOneSystem.class).setPlayerConfig(playerCfg);
			InputManager.getInstance().setSelectedPlayer(playerNr, playerCfg.m_active);
			break;
		case 2:
			playerCfg.minimumY = Converters.ToBox(m_levelConfig.m_minY);
			m_game.m_gameScreen.getEntityWorld().getSystem(PlayerTwoSystem.class).setPlayerConfig(playerCfg);
			InputManager.getInstance().setSelectedPlayer(playerNr, playerCfg.m_active);
			break;
		default:
			break;
		}
			
	}

	private void startProcessingSystems() {
		
		LevelSystem lvlSystem = m_game.m_gameScreen.getEntityWorld().getSystem(LevelSystem.class);
		lvlSystem.setLevelConfig(m_levelConfig);
		lvlSystem.setProcessing(true);
		
		m_game.m_gameScreen.getPhysicsSystem().toggleProcessing(true);
		
		m_game.m_gameScreen.getCameraSystem().toggleProcess(true);
		m_game.m_gameScreen.getCameraSystem().setLimits(m_levelConfig);
		
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
