package com.me.listeners;

import com.me.level.Level;

public interface LevelEventListener {

    void onRestartLevel();
	void OnStartLevel();
	void onFinishedLevel(Level levelNr);
    void onDied();
}
