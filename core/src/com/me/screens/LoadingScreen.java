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

    private Texture splsh;
    private BackgroundLoader loader;
    private boolean loadComplete = false;

    public LoadingScreen(Attraversiamo game) {
        super(game);
        this.splsh = new Texture(Gdx.files.internal("data/loading.png"));
        this.loader = new BackgroundLoader(game);
        this.loader.setListener(new LoadCompletionListener() {
            @Override
            public void onComplete() {
                loadComplete = true;
            }
        });
    }

    public void load(int levelNr) {
        loader.setLevel(levelNr);
        loader.run();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(splsh, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2.5f);
        spriteBatch.end();
        if (loadComplete) {
            changeScreen();
        }

    }

    private void changeScreen() {
        Screen current = game.getScreen();
        Screen next = game.gameScreen;

//        ArrayList<TransitionEffect> effects = new ArrayList<TransitionEffect>();
//
//        effects.add(new FadeOutTransitionEffect(1f));
//
//        Screen transitionScreen = new TransitionScreen(game, current, next, effects);

        game.setScreen(next);
    }

    @Override
    public void resize(int width, int height) {
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
        spriteBatch.dispose();
        splsh.dispose();
    }

    @Override
    protected String getName() {
        return getClass().getSimpleName();
    }

}
