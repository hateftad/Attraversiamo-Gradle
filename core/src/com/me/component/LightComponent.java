package com.me.component;

import com.badlogic.gdx.graphics.Color;
import com.me.utils.Converters;

import box2dLight.Light;

public class LightComponent extends BaseComponent {

    private Light light;
    private String name;

    public LightComponent(Light l, String name) {
        setName(name);
        setLight(l);
    }

    public void setPosition(float x, float y) {
        light.setPosition(x, y);
    }

    public void setColor(Color c) {
        light.setColor(c);
    }

    public void setColor(String c) {
        light.setColor(Converters.getColor(c));
    }

    public void setActive(boolean active) {
        light.setActive(active);
    }

    public void setAlpha(float a) {
        light.setColor(light.getColor().r, light.getColor().g, light.getColor().b, a);
    }

    public float getAlpha() {
        return light.getColor().a;
    }

    public boolean isActive() {
        return light.isActive();
    }

    public void setLight(Light l) {
        light = l;
    }

    public Light getLight() {
        return light;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void dispose() {
        light.remove();
    }

    @Override
    public void restart() {
        // TODO Auto-generated method stub

    }
}
