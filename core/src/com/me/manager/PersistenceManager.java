package com.me.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.me.level.LevelInfo;

public class PersistenceManager {

    private static final String AppPrefName = "AttraversiamoPrefrences";
    private static PersistenceManager instance;
    private Preferences preferences;

    private PersistenceManager() {
        preferences = Gdx.app.getPreferences(AppPrefName);
    }

    public static PersistenceManager getInstance() {
        if (instance == null) {
            instance = new PersistenceManager();
        }
        return instance;
    }

    public void saveSetting(String name, boolean onOff) {
        preferences.putBoolean(name, onOff);
    }

    public void saveLevelProgress(int level, int lives) {
        preferences.putInteger("current_level", level);
        preferences.putInteger("current_lives", lives);
        preferences.flush();
    }

    public LevelInfo getLevelInfo() {
        int current_level = preferences.getInteger("current_level", 1);
        int current_lives = preferences.getInteger("current_lives", 5);

        return new LevelInfo(current_level, current_lives);
    }
}
