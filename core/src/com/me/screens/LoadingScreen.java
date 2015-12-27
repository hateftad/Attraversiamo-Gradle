package com.me.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.attraversiamo.Attraversiamo;
import com.me.listeners.LoadCompletionListener;
import com.me.loaders.BackgroundLoader;
import com.me.screens.transitions.FadeInTransitionEffect;
import com.me.screens.transitions.FadeOutTransitionEffect;
import com.me.screens.transitions.TransitionEffect;

import java.util.ArrayList;

public class LoadingScreen extends AbstractScreen {

    private Texture m_splsh;
    private BackgroundLoader m_loader;
    private boolean m_loadComplete = false;

    public LoadingScreen(Attraversiamo game) {
        super(game);
        m_splsh = new Texture(Gdx.files.internal("data/loading.png"));
        m_loader = new BackgroundLoader(game);
        m_loader.setListener(new LoadCompletionListener() {
            @Override
            public void onComplete() {
                m_loadComplete = true;
            }
        });
    }

    public void load(int levelNr) {
        m_loader.setLevel(levelNr);
        m_loader.run();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        m_spriteBatch.begin();
        m_spriteBatch.draw(m_splsh, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2.5f);
        m_spriteBatch.end();
        if (m_loadComplete) {
            changeScreen();
        }

    }

    private void changeScreen() {
        Screen current = m_game.getScreen();
        Screen next = m_game.m_gameScreen;

        ArrayList<TransitionEffect> effects = new ArrayList<TransitionEffect>();

        effects.add(new FadeOutTransitionEffect(1f));

        Screen transitionScreen = new TransitionScreen(m_game, current, next, effects);

        m_game.setScreen(transitionScreen);
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        m_spriteBatch.dispose();
        m_splsh.dispose();

    }

    @Override
    protected String getName() {
        return getClass().getSimpleName();
    }

}
