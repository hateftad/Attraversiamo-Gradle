package com.me.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.me.attraversiamo.Attraversiamo;

public class AbstractScreen implements Screen {

	protected Attraversiamo m_game;
	protected SpriteBatch m_spriteBatch;
	protected OrthographicCamera m_camera;

	public AbstractScreen(Attraversiamo game){

		this.m_game = game;
		this.m_spriteBatch = new SpriteBatch();

		m_camera = new OrthographicCamera();  

		m_camera.position.set(m_camera.viewportWidth * .5f, m_camera.viewportHeight * .5f, 0f); 
		
		m_camera.update();
		
	}

	protected String getName()
    {
        return getClass().getSimpleName();
    }

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		//m_stage.setViewport( width, height, true );
	}


	@Override
	public void show() {
		// TODO Auto-generated method stub

	}


	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}


	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}


	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}


	@Override
	public void dispose() {
		m_spriteBatch.dispose();
	}

}
