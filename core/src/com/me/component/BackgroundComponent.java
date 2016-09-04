package com.me.component;

/**
 * Created by hateftadayon on 9/4/16.
 */
public class BackgroundComponent extends BaseComponent {

    private float velocityX;

    public BackgroundComponent(float velocityX){
        this.velocityX = velocityX;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }

    public float getVelocityX() {
        return velocityX;
    }

}
