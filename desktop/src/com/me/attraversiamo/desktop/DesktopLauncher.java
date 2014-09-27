package com.me.attraversiamo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.me.attraversiamo.Attraversiamo;
import com.me.utils.GameConfig;
import com.me.utils.GameConfig.Platform;

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
		new LwjglApplication(new Attraversiamo(config), cfg);
	}
}
