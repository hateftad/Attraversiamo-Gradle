package com.me.attraversiamo;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.utils.Array;
import com.me.ads.IActivityRequestHandler;
import com.me.ads.PlayServices;
import com.me.screens.GameScreen;
import com.me.screens.LoadingScreen;
import com.me.screens.SplashScreen;
import com.me.config.GameConfig;
import com.me.config.GameConfig.Platform;
import com.me.config.GlobalConfig;

public class Attraversiamo extends Game implements ApplicationListener {

    public GameScreen gameScreen;
    public LoadingScreen loadingScreen;
    private FPSLogger fpsLogger;
    private IActivityRequestHandler adRequestHandler;
    private PlayServices playServices;
    public Array<InputProcessor> processors = new Array<>();
    public InputMultiplexer multiPlexer = new InputMultiplexer();

    public Attraversiamo(GameConfig config, IActivityRequestHandler requestHandler) {
        if (config != null) {
            GlobalConfig.getInstance().setConfig(config);
        } else {
            GameConfig conf = new GameConfig();
            conf.platform = Platform.DESKTOP;
            conf.showUI = false;
            conf.timeStep = 1 / 60f;
        }
        adRequestHandler = requestHandler;
    }

    @Override
    public void create() {
        fpsLogger = new FPSLogger();
        setScreen(new SplashScreen(this));
        multiPlexer.setProcessors(processors);
    }

    public void setPlayServices(PlayServices playSevices){
        playServices = playSevices;
    }


    public void showAd(boolean show) {
        adRequestHandler.showAds(show);
    }

    public void setScreenName(String name) {
        adRequestHandler.setScreenName(name);
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

    public PlayServices getPlayServices() {
        return playServices;
    }
}
