package com.me.attraversiamo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.me.ads.IActivityRequestHandler;
import com.me.attraversiamo.Attraversiamo;
import com.me.config.GameConfig;
import com.me.config.GameConfig.Platform;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.width = 1024;
		cfg.height = 768;
		
		GameConfig config = new GameConfig();
		config.platform = Platform.DESKTOP;
		config.timeStep = 1/45f;
		config.showUI = true;
		config.zoom = 9f;
		new LwjglApplication(new Attraversiamo(config, requestHandler), cfg);
	}

    private static IActivityRequestHandler requestHandler = new IActivityRequestHandler() {
        @Override
        public void showAds(boolean show) {

        }

        @Override
        public void setScreenName(String name) {

        }
    };
}
