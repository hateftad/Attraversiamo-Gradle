package com.me.attraversiamo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.me.ads.IActivityRequestHandler;
import com.me.ads.PlayServices;
import com.me.attraversiamo.Attraversiamo;
import com.me.config.GameConfig;
import com.me.config.GameConfig.Platform;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.width = 1024;
        cfg.height = 768;

        GameConfig config = new GameConfig();
        config.platform = Platform.DESKTOP;
        config.timeStep = 1 / 45f;
        config.showUI = true;
        config.zoom = 9f;
        Attraversiamo attraversiamo = new Attraversiamo(config, requestHandler);
        attraversiamo.setPlayServices(playServices);
        new LwjglApplication(attraversiamo, cfg);
    }

    private static IActivityRequestHandler requestHandler = new IActivityRequestHandler() {
        @Override
        public void showBannerAd(boolean show) {

        }

        @Override
        public void showInterstitialAd() {

        }

        @Override
        public void setScreenName(String name) {

        }
    };

    private static PlayServices playServices = new PlayServices() {
        @Override
        public void signIn() {

        }

        @Override
        public void signOut() {

        }

        @Override
        public void rateGame() {

        }

        @Override
        public void unlockAchievement() {

        }

        @Override
        public void submitScore(int highScore) {

        }

        @Override
        public void showAchievement() {

        }

        @Override
        public void showScore() {

        }

        @Override
        public boolean isSignedIn() {
            return false;
        }
    };
}
