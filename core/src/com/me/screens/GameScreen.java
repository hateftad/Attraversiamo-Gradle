package com.me.screens;

import box2dLight.RayHandler;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.me.attraversiamo.Attraversiamo;
import com.me.component.CameraController;
import com.me.component.HudComponent;
import com.me.level.Level;
import com.me.listeners.LevelEventListener;
import com.me.manager.PersistenceManager;
import com.me.physics.JointFactory;
import com.me.systems.*;
import com.me.ui.InputManager;
import com.me.ui.UserInterface;

public class GameScreen extends AbstractScreen implements LevelEventListener {

    private final Level currentLevel;
    private GameEntityWorld entityWorld;
    private PhysicsSystem physicsSystem;
    private PlayerSystem playerOneSystem;
    private PlayerSystem playerTwoSystem;
    private RenderSystem renderSystem;
    private CameraSystem cameraSystem;
    private BackgroundObjectSystem backgroundObjectSystem;

    private boolean loadedNextLevel;


    public GameScreen(Attraversiamo game, Level currentLevel) {
        super(game);
        this.currentLevel = currentLevel;
        this.camera.viewportWidth = 800;
        this.camera.viewportHeight = 600;
        this.camera.zoom = currentLevel.getLevelConfig().getZoom();
        this.entityWorld = new GameEntityWorld();
        this.entityWorld.setManager(new GroupManager());
        this.physicsSystem = new PhysicsSystem(new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -14), true));
        JointFactory.getInstance().initialize(physicsSystem.getPhysicsWorld());
        RayHandler rayHandler = new RayHandler(physicsSystem.getPhysicsWorld());
        rayHandler.setAmbientLight(0.5f);
        rayHandler.setShadows(false);
        rayHandler.setAmbientLight(Color.GREEN);
        CameraController cameraController = new CameraController(camera);
        this.cameraSystem = entityWorld.setSystem(new CameraSystem(rayHandler, cameraController));
        this.renderSystem = entityWorld.setSystem(new RenderSystem(camera));
        this.entityWorld.setSystem(physicsSystem);
        this.entityWorld.setSystem(new LevelSystem());
        this.backgroundObjectSystem = this.entityWorld.setSystem(new BackgroundObjectSystem(cameraController));
        this.entityWorld.setSystem(new ContinousParticlesSystem(2));
        this.entityWorld.setSystem(new EventParticlesSystem());
        this.entityWorld.setSystem(new ManInteractionSystem());
        this.entityWorld.setSystem(new GirlInteractionSystem());
        this.entityWorld.setSystem(new HudSystem());
        this.entityWorld.setSystem(new AISteeringSystem());
        this.entityWorld.setSystem(new PlayerAISteeringSystem());
        this.playerOneSystem = new ManSystem(currentLevel);
        this.entityWorld.setSystem(playerOneSystem);
        this.playerTwoSystem = entityWorld.setSystem(new GirlSystem(currentLevel));
        this.entityWorld.initialize();
        this.entityWorld.addObserver(physicsSystem);
        this.entityWorld.addObserver(this);

        UserInterface userInterface = new UserInterface(currentLevel, entityWorld.getLevelEventListeners());
        userInterface.init();

        Entity hudEntity = this.entityWorld.createEntity();
        hudEntity.addComponent(new HudComponent(userInterface));
        this.entityWorld.addEntity(hudEntity);

        this.game.processors.clear();
        this.game.processors.add(userInterface.getStage());
        this.game.processors.add(cameraSystem);
        this.game.processors.add(playerOneSystem);
        this.game.multiPlexer.setProcessors(this.game.processors);
        Gdx.input.setInputProcessor(this.game.multiPlexer);
        InputManager.getInstance().reset();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        entityWorld.setDelta(Gdx.graphics.getDeltaTime());
        entityWorld.process();

        cameraSystem.process();

        InputManager.getInstance().update();

        if (loadedNextLevel) {
            OnStartLevel();
        }
    }

    public GameEntityWorld getEntityWorld() {
        return entityWorld;
    }

    public PhysicsSystem getPhysicsSystem() {
        return physicsSystem;
    }

    public CameraSystem getCameraSystem() {
        return cameraSystem;
    }

    public PlayerSystem getPlayerSystem() {
        return playerOneSystem;
    }

    public void toggleProcessingOnSystems(boolean state) {
        playerOneSystem.toggleProcessing(state);
        playerTwoSystem.toggleProcessing(state);
        cameraSystem.toggleProcess(state);
        physicsSystem.toggleProcessing(state);
        backgroundObjectSystem.toggleProcessing(state);
    }

    public void clear() {
        entityWorld = null;
        physicsSystem.clearSystem();
        renderSystem.dispose();
        cameraSystem.dispose();

    }

    public void printInfo() {
        if (entityWorld != null && entityWorld.getEntityManager() != null) {
            System.out.println("Active Entities" + entityWorld.getEntityManager().getActiveEntityCount());
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }


    @Override
    public void show() {
        super.show();
        System.out.println("show()");
        OnStartLevel();
        game.showBannerAd(false);

    }

    @Override
    public void hide() {
        System.out.println("hide()");
    }

    @Override
    public void pause() {
        System.out.println("pause()");
        physicsSystem.setEnabled(false);
        PersistenceManager.getInstance().saveProgress(currentLevel.getLevelNumber());
    }

    @Override
    public void resume() {
        System.out.println("resume()");
        physicsSystem.setEnabled(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        physicsSystem.dispose();
        entityWorld.dispose();
        renderSystem.dispose();
        cameraSystem.dispose();
    }

    @Override
    public void onRestartLevel() {
        toggleProcessingOnSystems(true);
        loadedNextLevel = false;
    }

    @Override
    public void onLevelPaused() {
        toggleProcessingOnSystems(false);
    }

    @Override
    public void onLevelResumed() {
        toggleProcessingOnSystems(true);
    }

    @Override
    public void OnStartLevel() {
        loadedNextLevel = false;
    }

    @Override
    public void onFinishedLevel(Level currentLevel) {
        game.showInterstitialAd();
        PersistenceManager.getInstance().saveProgress(currentLevel.getNextLevel());
        game.loadingScreen.load(currentLevel.getNextLevel());
        game.setScreen(game.loadingScreen);
        loadedNextLevel = true;
    }

    @Override
    public void onDied() {

    }

    @Override
    protected String getName() {
        return getClass().getSimpleName();
    }

}
