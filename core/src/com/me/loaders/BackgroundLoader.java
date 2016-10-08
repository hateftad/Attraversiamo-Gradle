package com.me.loaders;

import com.me.config.AI;
import com.me.config.AIConfig;
import com.me.config.Player;
import com.me.attraversiamo.Attraversiamo;
import com.me.level.Level;
import com.me.listeners.LoadCompletionListener;
import com.me.screens.GameScreen;
import com.me.systems.LevelSystem;
import com.me.ui.InputManager;
import com.me.config.PlayerConfig;

public class BackgroundLoader {

    private static final String LEVEL = "level";
    private LoadCompletionListener listener;
    private EntityLoader loader;
    private ConfigReader configLoader;
    private int level;
    private Attraversiamo game;
    private Level currentLevel;

    public BackgroundLoader(Attraversiamo game) {
        this.loader = new EntityLoader();
        this.game = game;
        this.configLoader = new ConfigReader();
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setListener(LoadCompletionListener listener) {
        this.listener = listener;
    }

    public void run() {
        doRun();
        listener.onComplete();
    }

    public void doRun() {

        currentLevel = new Level(configLoader.getLevelConfigByName(LEVEL + level));

        if (game.gameScreen != null) {
            clearLevel();
        } else {
            game.gameScreen = new GameScreen(game, currentLevel);
        }
        stopProcessingSystems();

        loader.loadLevel(currentLevel, game.gameScreen.getEntityWorld(), game.gameScreen.getPhysicsSystem().getPhysicsWorld(), game.gameScreen.getCameraSystem().getRayHandler());

        for (PlayerConfig playerConfig : currentLevel.getPlayerConfigs()) {
            Player player = new Player(playerConfig);
            loader.loadCharacter(player, game.gameScreen.getEntityWorld(), game.gameScreen.getPhysicsSystem().getPhysicsWorld());
            InputManager.getInstance().setSelectedPlayer(player.getPlayerNumber(), player.isActive());
        }

//        for (AIConfig aiConfig : currentLevel.getAIConfigs()) {
//            AI ai = new AI(aiConfig);
//            loader.loadCharacter(ai, game.gameScreen.getEntityWorld(), game.gameScreen.getPhysicsSystem().getPhysicsWorld());
//        }

        loader.dispose();
        startProcessingSystems();

    }

    private void startProcessingSystems() {

        LevelSystem lvlSystem = game.gameScreen.getEntityWorld().getSystem(LevelSystem.class);
        lvlSystem.setCurrentLevel(currentLevel);
        lvlSystem.setProcessing(true);

        game.gameScreen.getPhysicsSystem().toggleProcessing(true);

        game.gameScreen.getCameraSystem().toggleProcess(true);
        game.gameScreen.getCameraSystem().setLevelBoundariesForCamera(currentLevel.getLevelBoundaries());

        game.gameScreen.getPlayerSystem().toggleProcessing(true);
        game.gameScreen.getPlayerSystem().restartSystem();
        game.gameScreen.toggleProcessingOnSystems(true);
    }

    private void clearLevel() {

        game.gameScreen.clear();
        game.gameScreen = new GameScreen(game, currentLevel);

    }

    private void stopProcessingSystems() {
        game.gameScreen.toggleProcessingOnSystems(false);
    }

}
