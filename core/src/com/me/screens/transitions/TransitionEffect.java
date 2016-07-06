package com.me.screens.transitions;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.utils.TimeTransition;

/**
 * Created by hateftadayon on 25/11/15.
 */
public abstract class TransitionEffect {

    TimeTransition timeTransition;

    public TransitionEffect(float duration){
        timeTransition = new TimeTransition();
        timeTransition.start(duration);
    }

    protected float getAlpha(){
        return timeTransition.get();
    }
    public void update(float delta) {
        timeTransition.update(delta);
    }

    public abstract void render(float delta, Screen current, Screen next);
    public boolean isFinished(){
        return timeTransition.isFinished();
    }
}
