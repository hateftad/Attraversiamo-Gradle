package com.me.screens.transitions;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.utils.TimeTransition;

/**
 * Created by hateftadayon on 25/11/15.
 */
public abstract class TransitionEffect {

    TimeTransition m_timeTransition;

    public TransitionEffect(float duration){
        m_timeTransition = new TimeTransition();
        m_timeTransition.start(duration);
    }

    protected float getAlpha(){
        return m_timeTransition.get();
    }
    public void update(float delta) {
        m_timeTransition.update(delta);
    }

    public abstract void render(float delta, Screen current, Screen next);
    public boolean isFinished(){
        return m_timeTransition.isFinished();
    }
}
