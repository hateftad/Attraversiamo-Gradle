package com.me.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.me.attraversiamo.Attraversiamo;
import com.me.component.AnimationComponent;
import com.me.ui.UIButton;

public class MenuScreen extends AbstractScreen implements InputProcessor {

	private Stage m_stage;
	private TextureAtlas m_atlas;
	private Skin m_skin;
	private Table m_table;
	private UIButton m_newGameBtn;
	
	private static final String PONEPATH = "data/character/littleGirl/littleGirl";
	private static final String PTWOPATH = "data/character/smallCharacter/bigGuy";
	private static final String SCENEPATH = "data/ui/menu";
	private static final float SCALE = 0.5f;
	
	private ArrayList<AnimationComponent> m_animation;

	public MenuScreen(Attraversiamo game) {
		super(game);
		init();
	}
	
	private void init(){
		m_camera.viewportWidth = 1024;
		m_camera.viewportHeight = 768;
		
		m_animation = new ArrayList<AnimationComponent>();

		Vector2 middlePoint = new Vector2(Gdx.graphics.getWidth()/2, 0);
		AnimationComponent scene = new AnimationComponent(SCENEPATH, SCENEPATH, SCALE);
		scene.setUp(middlePoint, "running");
		
		//AnimationComponent littleGirl = new AnimationComponent(PONEPATH ,"data/ui/littleGirl", SCALE);
		//littleGirl.setUp(middlePoint, "walking");
		
		//AnimationComponent bigGuy = new AnimationComponent(PTWOPATH, "data/ui/bigGuy", SCALE);
		//bigGuy.setUp(middlePoint, "walking");
		
		m_animation.add(scene);
		//m_animation.add(littleGirl);
		//m_animation.add(bigGuy);
		//m_camera.lookAt(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		m_camera.zoom = 1f;
		//Skeleton skel = m_animation.getSkeleton();
		//skel.getAttachment(0, "");
		/*
		m_stage = new Stage();
		m_game.m_processors.add(m_stage);
		m_atlas = new TextureAtlas(Gdx.files.internal("data/ui/buttons.pack"));
		m_skin = new Skin(m_atlas);

		m_table = new Table(m_skin);
		m_table.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		TextButtonStyle btnStyle = new TextButtonStyle();
		btnStyle.up = m_skin.getDrawable("newgame.up");
		btnStyle.pressedOffsetX = 10;

		m_newGameBtn = new UIButton(btnStyle);
		m_newGameBtn.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					
			}
		});
		m_table.add(m_newGameBtn);
		//m_table.addListener();
		m_table.debug();
		m_stage.addActor(m_table);
		//m_stage.addActor(m_newGameBtn);
		*/

	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		
		super.render(delta);
		m_camera.update();
		//m_stage.act(delta);
		//m_stage.draw();
		m_spriteBatch.setProjectionMatrix(m_camera.combined);
		m_spriteBatch.begin();
		for(AnimationComponent comp:m_animation){
			comp.update(m_spriteBatch, delta);
		}
		m_spriteBatch.end();
		if(Gdx.input.justTouched()){
			m_game.m_loadingScreen = new LoadingScreen(m_game);
			m_game.m_loadingScreen.load(1);
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
		m_atlas.dispose();
		m_skin.dispose();
		//m_animation.dispose();
	}

	@Override
	public void resize(int width, int height) {
		//super.resize(width, height);

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		/*
		if(m_newGameBtn.contains(screenX, screenY)){
			m_game.setScreen(new LoadingScreen(m_game));
			m_game.m_multiPlexer.removeProcessor(this);
		}
		 */
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
