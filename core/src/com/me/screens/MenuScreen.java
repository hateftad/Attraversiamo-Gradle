package com.me.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.me.attraversiamo.Attraversiamo;
import com.me.component.AnimationComponent;
import com.me.component.LevelAnimationComponent;
import com.me.ui.UIButton;

public class MenuScreen extends AbstractScreen {


	private static final String SCENEPATH = "data/ui/menu";
	private static final float SCALE = 0.5f;
	
	private Array<AnimationComponent> m_animation;

	public MenuScreen(Attraversiamo game) {
		super(game);
		init();
	}
	
	private void init(){
		m_camera.viewportWidth = 800;
		m_camera.viewportHeight = 600;
		m_camera.zoom = 2f;
		m_animation = new Array<AnimationComponent>();

		Vector2 middlePoint = new Vector2(0, 0);
		AnimationComponent scene = new LevelAnimationComponent(SCENEPATH, SCENEPATH, SCALE);
		scene.setUp(middlePoint, "running");
		m_animation.add(scene);
	}

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		
		super.render(delta);
		m_camera.update();
		m_spriteBatch.setProjectionMatrix(m_camera.combined);
		m_spriteBatch.begin();
		for(AnimationComponent comp: m_animation){
			comp.update(m_spriteBatch, delta);
		}
		m_spriteBatch.end();
		if(Gdx.input.justTouched()){
			m_game.m_loadingScreen = new LoadingScreen(m_game);
			m_game.m_loadingScreen.load(6);
			m_game.setScreen(m_game.m_loadingScreen);
		}
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void dispose(){
		m_stage.dispose();
		m_spriteBatch.dispose();
        for(AnimationComponent animationComponent : m_animation){
            animationComponent.dispose();
        }
	}

	@Override
	public void resize(int width, int height) {
		//super.resize(width, height);

	}

}
