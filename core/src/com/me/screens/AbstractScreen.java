package com.me.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.me.attraversiamo.Attraversiamo;

public abstract class AbstractScreen implements Screen {

    protected Attraversiamo game;
    protected SpriteBatch spriteBatch;
    protected OrthographicCamera camera;

    public AbstractScreen(Attraversiamo game) {
        this.game = game;
        this.spriteBatch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.camera.position.set(camera.viewportWidth * .5f, camera.viewportHeight * .5f, 0f);
        camera.update();

    }

    protected abstract String getName();

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {

    }


    @Override
    public void show() {
        game.setScreenName(getName());
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
    }

}
