package com.me.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.attraversiamo.Attraversiamo;
import com.me.component.AnimationComponent;

public class SplashScreen implements Screen {


	private SpriteBatch m_spriteBatch;
	private AnimationComponent m_animation;
	private Attraversiamo m_myGame;


	public SplashScreen(Attraversiamo game){
		m_myGame = game;
		m_spriteBatch = new SpriteBatch();
		m_animation = new AnimationComponent("data/intro", "data/intro", 1f);
		m_animation.setUp(new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2.5f), "intro");
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		m_spriteBatch.begin();
		m_animation.update(m_spriteBatch, delta/2);
		m_spriteBatch.end();

		if(Gdx.input.justTouched()){
			m_myGame.setScreen(new MenuScreen(m_myGame));
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {


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
		m_animation.dispose();

	}

}
