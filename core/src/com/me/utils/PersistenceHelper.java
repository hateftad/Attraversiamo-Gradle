package com.me.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PersistenceHelper {

    private static final String NAME = "Preferences";
    private static PersistenceHelper instance;
    private Preferences preferences;

    public static PersistenceHelper getSharedInstance(){
        if(instance == null){
            instance = new PersistenceHelper();
        }
        return instance;
    }

    private PersistenceHelper(){
        preferences = Gdx.app.getPreferences(NAME);
    }

    public void saveThis(){
        preferences.putString("key", "saving this");
    }

}
