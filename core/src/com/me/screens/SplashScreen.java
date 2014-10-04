package com.me.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.me.attraversiamo.Attraversiamo;
import com.me.component.AnimationComponent;

public class SplashScreen extends AbstractScreen {

	private AnimationComponent m_animation;
	private boolean timerIsOn = false;

	public SplashScreen(Attraversiamo game){
		
		super(game);
		m_animation = new AnimationComponent("data/intro", "data/intro", 1f);
		m_animation.setUp(new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2.5f), "intro");
		m_camera.zoom = 1f;
	}

	@Override
	public void render(float delta) {
		
		super.render(delta);
		m_camera.update();
		m_spriteBatch.setProjectionMatrix(m_camera.combined);
		m_spriteBatch.begin();
		m_animation.update(m_spriteBatch, delta/2);
		m_spriteBatch.end();

		 if(!timerIsOn) {
	         timerIsOn = true;
	         
	         Timer.schedule(new Task() {
	            
	            @Override
	            public void run() {
	               changeScreen();
	            }

	         }, 3);
	             
	      } else if(Gdx.input.isTouched()) {
	           // Remove the task so we don't call changeScreen twice: 
	           Timer.instance().clear(); 
	           changeScreen();
	      }
	}
	
	private void changeScreen(){
		m_game.setScreen(new MenuScreen(m_game));
    	System.out.println("starting");
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
		m_animation.dispose();

	}

}
