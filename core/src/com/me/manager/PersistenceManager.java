package com.me.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.me.level.LevelInfo;

public class PersistenceManager {

    private static final String AppPrefName = "attraversiamo_preferences";
    private static final String CurrentLives = "current_lives";
    private static final String CurrentLevel = "current_level";
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

    public void saveProgress(int level) {
        int currentLives = preferences.getInteger(CurrentLives, 5);
        preferences.putInteger(CurrentLevel, level);
        preferences.putInteger(CurrentLives, currentLives);
        preferences.flush();
    }

    public LevelInfo getLevelInfo() {
        int currentLevel = preferences.getInteger(CurrentLevel, 1);
        int currentLives = preferences.getInteger(CurrentLives, 5);

        return new LevelInfo(currentLevel, currentLives);
    }

    public int getLives(){
        return preferences.getInteger(CurrentLives, 5);
    }
}
