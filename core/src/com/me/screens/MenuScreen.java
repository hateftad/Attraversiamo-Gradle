package com.me.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.esotericsoftware.spine.Skeleton;
import com.me.attraversiamo.Attraversiamo;
import com.me.component.AnimationComponent;
import com.me.ui.UIButton;

public class MenuScreen extends AbstractScreen implements InputProcessor {

	private Stage m_stage;
	private TextureAtlas m_atlas;
	private Skin m_skin;
	private Table m_table;
	private UIButton m_newGameBtn;
	private Attraversiamo m_game;

	private AnimationComponent m_animation;

	public MenuScreen(Attraversiamo game) {
		super(game);
		m_game = game;
		init();
	}
	
	private void init(){
		m_animation = new AnimationComponent("data/menu", "data/menu", 1f);
		m_animation.setUp(
				new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics
						.getHeight() / 2), "slideIn");
		Skeleton skel = m_animation.getSkeleton();
		skel.getAttachment(0, "");
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
	}

	@Override
	public void show() {

		
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		m_stage.act(delta);
		m_stage.draw();
		//m_table.drawDebug(m_stage);
		m_spriteBatch.begin();
		m_animation.update(m_spriteBatch, delta / 2);
		m_spriteBatch.end();
		if(Gdx.input.justTouched()){
			m_game.m_loadingScreen = new LoadingScreen(m_game);
			m_game.m_loadingScreen.load(5);
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
		m_animation.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

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
