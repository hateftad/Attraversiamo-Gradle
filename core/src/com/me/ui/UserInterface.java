package com.me.ui;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.me.utils.GlobalConfig;

public class UserInterface {

	private UIButton m_leftBtn;
	private UIButton m_rightBtn;
	private UIButton m_restartBtn;
	private UIButton m_jumpBtn;
	private UIButton m_actionBtn;
	private UIButton m_charSwitchBtn;
	private Table m_bottomLeftBtnsTable;
	private Table m_bottomRightBtnsTable;
	private Table m_topBtnsTable;
	private Stage m_stage;
	private TextureAtlas m_atlas;
	private Skin m_skin;
	
	
	public UserInterface(){
		
		m_stage = new Stage();
		
		m_bottomLeftBtnsTable = new Table();
		m_bottomRightBtnsTable = new Table();
		m_topBtnsTable = new Table();
		
		m_atlas = new TextureAtlas(Gdx.files.internal("data/ui/buttons.pack"));
		m_skin = new Skin(m_atlas);
	}
	
	public void init(){
		
		TextButtonStyle btnStyle = new TextButtonStyle();
		btnStyle.up = m_skin.getDrawable("left.up");

		m_leftBtn = new UIButton(btnStyle);
		//m_leftBtn.debug();
		m_leftBtn.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				InputManager.getInstance().keyDown(Input.Keys.A);
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				InputManager.getInstance().keyUp(Input.Keys.A);
			}
		});
		
		TextButtonStyle btnStyle2 = new TextButtonStyle();
		btnStyle2.up = m_skin.getDrawable("right.up");

		m_rightBtn = new UIButton(btnStyle2);
		//m_rightBtn.debug();
		m_rightBtn.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				InputManager.getInstance().keyDown(Input.Keys.D);
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				InputManager.getInstance().keyUp(Input.Keys.D);
			}
		});
		
		
		TextButtonStyle btnStyle5 = new TextButtonStyle();
		btnStyle5.up = m_skin.getDrawable("change.up");
		m_restartBtn = new UIButton(btnStyle5);
		//m_restartBtn.debug();
		m_restartBtn.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				InputManager.getInstance().callRestart();
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
			}
		});
		
		TextButtonStyle btnStyle4 = new TextButtonStyle();
		btnStyle4.up = m_skin.getDrawable("change.up");
		
		m_charSwitchBtn = new UIButton(btnStyle4);
		//m_charSwitchBtn.debug();
		m_charSwitchBtn.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				InputManager.getInstance().characterSwitch();
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				InputManager.getInstance().keyUp(Input.Keys.C);
			}
		});
		
		TextButtonStyle btnStyle3 = new TextButtonStyle();
		btnStyle3.up = m_skin.getDrawable("up.up");
		
		m_jumpBtn = new UIButton(btnStyle3);
		//m_jumpBtn.debug();
		m_jumpBtn.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				InputManager.getInstance().keyDown(Input.Keys.SPACE);
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				InputManager.getInstance().keyUp(Input.Keys.SPACE);
			}
		});
		
		TextButtonStyle btnStyle6 = new TextButtonStyle();
		btnStyle6.up = m_skin.getDrawable("up.up");
		
		m_actionBtn = new UIButton(btnStyle6);
		//m_actionBtn.debug();
		m_actionBtn.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				InputManager.getInstance().keyDown(Input.Keys.F);
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				InputManager.getInstance().keyUp(Input.Keys.F);
			}
		});
		
		
		
		m_bottomLeftBtnsTable.setFillParent(true);
		m_bottomLeftBtnsTable.bottom().left();
		m_bottomLeftBtnsTable.add(m_leftBtn).bottom().left().width(200).height(200);
		m_bottomLeftBtnsTable.add(m_rightBtn).bottom().left().width(200).height(200);
		
		m_bottomRightBtnsTable.setFillParent(true);
		m_bottomRightBtnsTable.bottom().right().setHeight(200);
		m_bottomRightBtnsTable.add(m_actionBtn).bottom().right().width(200).height(200);
		m_bottomRightBtnsTable.add(m_jumpBtn).bottom().right().width(200).height(200);
		m_bottomRightBtnsTable.add(m_charSwitchBtn).bottom().right().width(200).height(200);
		
		m_topBtnsTable.setFillParent(true);
		m_topBtnsTable.top().left();
		m_topBtnsTable.add(m_restartBtn).width(100).height(100);
	
		
		m_stage.addActor(m_topBtnsTable);
		m_stage.addActor(m_bottomLeftBtnsTable);
		m_stage.addActor(m_bottomRightBtnsTable);
		if(Gdx.app.getType() != ApplicationType.Desktop){
			Gdx.input.setInputProcessor(m_stage);
		}
	}
	
	public void update(float delta){
		//if(GlobalConfig.getInstance().config.showUI){
			m_stage.act(delta);
			m_stage.draw();
			//Table.drawDebug(m_stage);
		//}
	}
}
