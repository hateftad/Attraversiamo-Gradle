package com.me.screens;

import box2dLight.RayHandler;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.me.attraversiamo.Attraversiamo;
import com.me.listeners.LevelEventListener;
import com.me.physics.JointFactory;
import com.me.scripting.ScriptManager;
import com.me.systems.CameraSystem;
import com.me.systems.PhysicsSystem;
import com.me.systems.PlayerAttributeSystem;
import com.me.systems.PlayerOneSystem;
import com.me.systems.PlayerTwoSystem;
import com.me.systems.RenderSystem;
import com.me.systems.LevelSystem;
import com.me.utils.GameConfig.Platform;
import com.me.utils.GlobalConfig;

public class GameScreen extends AbstractScreen implements LevelEventListener{

	private World m_entityWorld;
	private PhysicsSystem m_physicsSystem;
	private PlayerOneSystem m_playerOneSystem;
	private PlayerTwoSystem m_playerTwoSystem;
	private RenderSystem m_renderSystem;
	private CameraSystem m_cameraSystem;
	private boolean m_loadedNextLevel;


	public GameScreen(Attraversiamo game)
	{
		super(game);
		m_entityWorld = new World();
		m_entityWorld.setManager(new GroupManager());
		m_physicsSystem = new PhysicsSystem(new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -14), true));
		JointFactory.getInstance().initialize(m_physicsSystem.getWorld());
		RayHandler rayHandler = new RayHandler(m_physicsSystem.getWorld());
		rayHandler.setAmbientLight(0.8f);
		//rayHandler.setShadows(false);
		//rayHandler.setAmbientLight(Color.GREEN);
		
		m_renderSystem = m_entityWorld.setSystem(new RenderSystem());
		m_entityWorld.setSystem(m_physicsSystem);
		m_entityWorld.setSystem(new PlayerAttributeSystem());
		m_entityWorld.setSystem(new LevelSystem(this));
		m_cameraSystem = m_entityWorld.setSystem(new CameraSystem(rayHandler, m_camera));
		
		m_playerOneSystem = new PlayerOneSystem(m_physicsSystem);
		m_entityWorld.setSystem(m_playerOneSystem);
		m_playerTwoSystem = m_entityWorld.setSystem(new PlayerTwoSystem());
		m_entityWorld.initialize();
		game.m_processors.add(m_cameraSystem);
		game.m_processors.add(m_playerOneSystem);
		if(GlobalConfig.getInstance().config.platform == Platform.DESKTOP){
			Gdx.input.setInputProcessor(game.m_multiPlexer);
		}
		
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		
		m_entityWorld.setDelta(Gdx.graphics.getDeltaTime());
		m_entityWorld.process();
		
		if(m_loadedNextLevel){		
			OnStartLevel();
		}
	}
	
	public World getEntityWorld(){
		return m_entityWorld;
	}
	
	public PhysicsSystem getPhysicsSystem(){
		return m_physicsSystem;
	}
	
	public CameraSystem getCameraSystem(){
		return m_cameraSystem;
	}
	
	public PlayerOneSystem getPlayerSystem(){
		return m_playerOneSystem;
	}
	
	public void toggleProcessingOnSystems(boolean state){
		m_playerOneSystem.toggleProcessing(state);
		m_playerTwoSystem.toggleProcessing(state);
		m_cameraSystem.toggleProcess(state);
		m_physicsSystem.toggleProcessing(state);
	}
	
	public void clear(){
		m_entityWorld = null;
		m_physicsSystem.clearSystem();
		m_renderSystem.dispose();
		m_cameraSystem.dispose();
		
	}

	@Override
	public void resize(int width, int height) {
		super.resize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
	}

	@Override
	public void show() {
		OnStartLevel();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		m_physicsSystem.dispose();
		super.dispose();
	}

	@Override
	public void onRestartLevel() {
		m_loadedNextLevel = false;
	}

	@Override
	public void OnStartLevel() {
		m_loadedNextLevel = false;
	}

	@Override
	public void onFinishedLevel(int nr) {
		//nr++;
		m_game.m_loadingScreen.load(5);
		m_game.setScreen(m_game.m_loadingScreen);
		m_loadedNextLevel = true;
		
	}

}
