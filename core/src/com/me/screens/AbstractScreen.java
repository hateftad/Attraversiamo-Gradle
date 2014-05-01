package com.me.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.me.attraversiamo.Attraversiamo;
import com.me.utils.GlobalConfig;

public class AbstractScreen implements Screen {

	protected Attraversiamo m_game;
	protected SpriteBatch m_spriteBatch;
	protected Stage m_stage;
	protected OrthographicCamera m_camera;

	public AbstractScreen(Attraversiamo game){

		this.m_game = game;
		this.m_spriteBatch = new SpriteBatch();
		this.m_stage = new Stage( );
		
		m_camera = new OrthographicCamera();  
		m_camera.viewportHeight = Gdx.graphics.getHeight();  
		m_camera.viewportWidth = Gdx.graphics.getWidth();  
		m_camera.position.set(m_camera.viewportWidth * .5f, m_camera.viewportHeight * .5f, 0f);  
		m_camera.zoom = GlobalConfig.getInstance().config.zoom;
		m_camera.update();
		
	}

	protected String getName()
    {
        return getClass().getSimpleName();
    }

	@Override
	public void render(float delta) {
		 // the following code clears the screen with the given RGB color (black)
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//m_camera.update();
        // update and draw the stage actors
        //m_stage.act(delta);
        //m_stage.draw();
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
		m_stage.dispose();
	}

}
