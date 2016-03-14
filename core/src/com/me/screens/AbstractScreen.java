package com.me.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.me.attraversiamo.Attraversiamo;

public abstract class AbstractScreen implements Screen {

    protected Attraversiamo m_game;
    protected SpriteBatch m_spriteBatch;
    protected OrthographicCamera m_camera;

    public AbstractScreen(Attraversiamo game) {
        this.m_game = game;
        this.m_spriteBatch = new SpriteBatch();

        m_camera = new OrthographicCamera();
        m_camera.position.set(m_camera.viewportWidth * .5f, m_camera.viewportHeight * .5f, 0f);
        m_camera.update();

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
        m_game.setScreenName(getName());
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
    }

}
