package com.me.attraversiamo.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.me.attraversiamo.Attraversiamo;
import com.me.utils.GameConfig;
import com.me.utils.GameConfig.Platform;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		GameConfig cfg = new GameConfig();
		cfg.platform = Platform.ANDROID;
		cfg.timeStep = 1/60f;
		cfg.showUI = true;
		cfg.zoom = 9f;
		
		initialize(new Attraversiamo(cfg), config);
	}
}
