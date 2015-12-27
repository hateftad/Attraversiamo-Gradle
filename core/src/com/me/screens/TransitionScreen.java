package com.me.screens;

import com.badlogic.gdx.Screen;
import com.me.attraversiamo.Attraversiamo;
import com.me.screens.transitions.TransitionEffect;

import java.util.ArrayList;

/**
 * Created by hateftadayon on 25/11/15.
 */
public class TransitionScreen implements Screen {

    private Screen current;
    private Screen next;
    private Attraversiamo m_game;

    private int currentTransitionEffect;
    private ArrayList<TransitionEffect> transitionEffects;

    public TransitionScreen(Attraversiamo game, Screen current, Screen next, ArrayList<TransitionEffect> transitionEffects) {
        m_game = game;
        this.current = current;
        this.next = next;
        this.transitionEffects = transitionEffects;
        this.currentTransitionEffect = 0;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (currentTransitionEffect >= transitionEffects.size()) {
            m_game.setScreen(next);
            return;
        }

        transitionEffects.get(currentTransitionEffect).update(delta);
        transitionEffects.get(currentTransitionEffect).render(delta, current, next);

        if (transitionEffects.get(currentTransitionEffect).isFinished()) {
            currentTransitionEffect++;
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
