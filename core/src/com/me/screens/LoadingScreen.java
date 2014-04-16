package com.me.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.attraversiamo.Attraversiamo;
import com.me.listeners.LoadCompletionListener;
import com.me.loaders.BackgroundLoader;

public class LoadingScreen implements Screen {

	private SpriteBatch m_spriteBatch;
	private Texture m_splsh;
	private Attraversiamo m_game;
	private BackgroundLoader m_loader;
	private boolean m_loadComplete = false;

	public LoadingScreen(Attraversiamo game){

		m_spriteBatch = new SpriteBatch();
		m_splsh = new Texture(Gdx.files.internal("data/loading.png"));
		m_game = game;
		m_game.setScreen(this);
		m_loader = new BackgroundLoader(game);
		m_loader.setListener(new LoadCompletionListener() {
			@Override
			public void onComplete() {
				m_loadComplete = true;
			}
		});
	}
	
	public void load(int levelNr){
		
		m_loader.setLevel(levelNr);
		m_loader.run();
	}
	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		m_spriteBatch.begin();
		m_spriteBatch.draw(m_splsh,Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2.5f);
		m_spriteBatch.end();
		if(m_loadComplete){
			m_game.setScreen(m_game.m_gameScreen);
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
		m_splsh.dispose();

	}

}
