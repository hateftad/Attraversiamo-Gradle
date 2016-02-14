package com.me.screens;

import box2dLight.RayHandler;

import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.me.attraversiamo.Attraversiamo;
import com.me.level.Level;
import com.me.listeners.LevelEventListener;
import com.me.physics.JointFactory;
import com.me.systems.*;
import com.me.ui.InputManager;
import com.me.ui.UserInterface;

public class GameScreen extends AbstractScreen implements LevelEventListener {

    private GameEntityWorld m_entityWorld;
    private PhysicsSystem m_physicsSystem;
    private PlayerSystem m_playerOneSystem;
    private PlayerSystem m_playerTwoSystem;
    private RenderSystem m_renderSystem;
    private CameraSystem m_cameraSystem;
    private UserInterface m_userInterface;
    private boolean m_loadedNextLevel;


    public GameScreen(Attraversiamo game, Level currentLevel) {
        super(game);
        m_camera.viewportWidth = 800;
        m_camera.viewportHeight = 600;
        m_camera.zoom = currentLevel.getLevelConfig().getZoom();
        m_entityWorld = new GameEntityWorld();
        m_entityWorld.setManager(new GroupManager());
        m_physicsSystem = new PhysicsSystem(new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -14), true));
        JointFactory.getInstance().initialize(m_physicsSystem.getWorld());
        RayHandler rayHandler = new RayHandler(m_physicsSystem.getWorld());
        rayHandler.setAmbientLight(0.5f);
        rayHandler.setShadows(false);
        rayHandler.setAmbientLight(Color.GREEN);
        m_cameraSystem = m_entityWorld.setSystem(new CameraSystem(rayHandler, m_camera));
        m_renderSystem = m_entityWorld.setSystem(new RenderSystem(m_camera));
        m_entityWorld.setSystem(m_physicsSystem);
        m_entityWorld.setSystem(new LevelSystem(this));
        m_entityWorld.setSystem(new ParticlesSystem(2));
        m_entityWorld.setSystem(new ManInteractionSystem());
        m_entityWorld.setSystem(new GirlInteractionSystem());
        m_playerOneSystem = new ManSystem(m_physicsSystem);
        m_entityWorld.setSystem(m_playerOneSystem);
        m_playerTwoSystem = m_entityWorld.setSystem(new GirlSystem());
        m_entityWorld.initialize();
        game.m_processors.add(m_cameraSystem);
        game.m_processors.add(m_playerOneSystem);

//        m_userInterface = new UserInterface(currentLevel);
//        m_userInterface.init();

        if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
            m_userInterface = new UserInterface(currentLevel);
            m_userInterface.init();
        } else {
            Gdx.input.setInputProcessor(game.m_multiPlexer);
        }

        InputManager.getInstance().reset();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        m_entityWorld.setDelta(Gdx.graphics.getDeltaTime());
        m_entityWorld.process();

        m_cameraSystem.process();
        InputManager.getInstance().update();

        if (m_userInterface != null) {
            m_userInterface.update(delta);
        }

        if (m_loadedNextLevel) {
            OnStartLevel();
        }
    }

    public GameEntityWorld getEntityWorld() {
        return m_entityWorld;
    }

    public PhysicsSystem getPhysicsSystem() {
        return m_physicsSystem;
    }

    public CameraSystem getCameraSystem() {
        return m_cameraSystem;
    }

    public PlayerSystem getPlayerSystem() {
        return m_playerOneSystem;
    }

    public void toggleProcessingOnSystems(boolean state) {
        m_playerOneSystem.toggleProcessing(state);
        m_playerTwoSystem.toggleProcessing(state);
        m_cameraSystem.toggleProcess(state);
        m_physicsSystem.toggleProcessing(state);
    }

    public void clear() {
        m_entityWorld = null;
        m_physicsSystem.clearSystem();
        m_renderSystem.dispose();
        m_cameraSystem.dispose();

    }

    public void printInfo() {
        if (m_entityWorld != null && m_entityWorld.getEntityManager() != null) {
            System.out.println("Active Entities" + m_entityWorld.getEntityManager().getActiveEntityCount());
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
    }


    @Override
    public void show() {
        super.show();
        System.out.println("show()");
        OnStartLevel();
        m_game.showAd(false);
    }

    @Override
    public void hide() {
        System.out.println("hide()");
    }

    @Override
    public void pause() {
        System.out.println("pause()");
        m_physicsSystem.setEnabled(false);
    }

    @Override
    public void resume() {
        System.out.println("resume()");
        m_physicsSystem.setEnabled(true);
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
        m_game.m_loadingScreen.load(nr);
        m_game.setScreen(m_game.m_loadingScreen);
        m_loadedNextLevel = true;

    }

    @Override
    protected String getName() {
        return getClass().getSimpleName();
    }

}
