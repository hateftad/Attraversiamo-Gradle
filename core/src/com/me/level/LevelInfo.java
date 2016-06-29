package com.me.level;

public class LevelInfo{
    private int currentLevel;
    private int currentLives;

    public LevelInfo(int current_level, int current_lives) {

        this.currentLevel = current_level;
        this.currentLives = current_lives;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getLives() {
        return currentLives;
    }

    public void setLives(int lives) {
        this.currentLives = lives;
    }
}
