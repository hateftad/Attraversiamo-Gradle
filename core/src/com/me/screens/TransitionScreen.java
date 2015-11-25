package com.me.screens;

import com.badlogic.gdx.Screen;
import com.me.attraversiamo.Attraversiamo;
import com.me.screens.transitions.TransitionEffect;

import java.util.ArrayList;

/**
 * Created by hateftadayon on 25/11/15.
 */
public class TransitionScreen extends AbstractScreen {

    private Screen current;
    private Screen next;

    private int currentTransitionEffect;
    private ArrayList<TransitionEffect> transitionEffects;

    public TransitionScreen(Attraversiamo game, Screen current, Screen next, ArrayList<TransitionEffect> transitionEffects) {
        super(game);
        this.current = current;
        this.next = next;
        this.transitionEffects = transitionEffects;
        this.currentTransitionEffect = 0;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (currentTransitionEffect >= transitionEffects.size()) {
            m_game.setScreen(next);
            return;
        }
        current.render(delta);
        transitionEffects.get(currentTransitionEffect).update(delta);
        transitionEffects.get(currentTransitionEffect).render();

        if (transitionEffects.get(currentTransitionEffect).isFinished())
            currentTransitionEffect++;
    }

}
