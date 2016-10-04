package com.me.config;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by hateftadayon on 10/4/16.
 */
public class AIConfig {

    private Vector2 position;
    private String name;

    public AIConfig(Vector2 position, String name){
        this.position = position;
        this.name = name;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
