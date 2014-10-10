package com.me.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.me.listeners.LevelEventListener;
import com.me.systems.CameraSystem;

public class InputManager {

	private int left = 0, right = 1, restart = 2, down = 3, jump = 4, rag = 5, first = 6, action = 7; 
	
	public enum PlayerSelection{
		ONE,
		TWO
	}
	
	private Stage m_stage;
	private TextureAtlas m_atlas;
	private Skin m_skin;
	private UIButton m_leftBtn;
	private UIButton m_rightBtn;
	private UIButton m_restartBtn;
	private UIButton m_jumpBtn;
	private UIButton m_actionBtn;
	private UIButton m_charSwitchBtn;
	public PlayerSelection m_playerSelected;
	public boolean playerOneActive;
	private ArrayList<LevelEventListener> m_levelListeners;
	private Table m_bottomBtnsTable;
	private Table m_topBtnsTable;
	private boolean[] m_button  = new boolean[10];
	
	
	private static InputManager instance = null;
	
	public static InputManager getInstance() {
	      if(instance == null) {
	         instance = new InputManager();
	      }
	      return instance;
	   }
	
	public void setListener(LevelEventListener listener){
		
		m_levelListeners.add(listener);
		
	}
	
	public void setSelectedPlayer(int nr, boolean active){
		
		if(nr == 1 && active){
			playerOneActive = true;
			m_playerSelected = PlayerSelection.ONE;
		} else if(nr == 2 && active){
			playerOneActive = false;
			m_playerSelected = PlayerSelection.TWO;
		}
	}
	
	private InputManager(){
		
		m_levelListeners = new ArrayList<LevelEventListener>(); 
		m_bottomBtnsTable = new Table();
		m_topBtnsTable = new Table();
		for (@SuppressWarnings("unused") boolean b : m_button) {
			b = false;
		}
		int widthBtn = (int) (Gdx.graphics.getWidth() / 8f);
		int height = (int) (Gdx.graphics.getHeight() / 6f);
		float ppcx = Gdx.graphics.getPpcX();
		float ppcy = Gdx.graphics.getPpcY();
		
		float heightOfBtn = 0.3f*ppcx;
		float widthScr = Gdx.graphics.getWidth();
		float heightScr = Gdx.graphics.getHeight();
		
		m_stage = new Stage();
		
		m_atlas = new TextureAtlas(Gdx.files.internal("data/ui/buttons.pack"));
		m_skin = new Skin(m_atlas);

		TextButtonStyle btnStyle = new TextButtonStyle();
		btnStyle.up = m_skin.getDrawable("left.up");

		m_leftBtn = new UIButton(btnStyle);
		m_leftBtn.debug();
		m_leftBtn.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				m_button[left] = true;
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				m_button[left] = false;
			}
		});
		
		TextButtonStyle btnStyle2 = new TextButtonStyle();
		btnStyle2.up = m_skin.getDrawable("right.up");

		m_rightBtn = new UIButton(btnStyle2);
		m_rightBtn.setBounds((m_leftBtn.getX() + widthBtn) + (ppcy/5), heightOfBtn, widthBtn, height);
		m_rightBtn.debug();
		m_rightBtn.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				m_button[right] = true;
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				m_button[right] = false;
			}
		});
		
		
		TextButtonStyle btnStyle5 = new TextButtonStyle();
		btnStyle5.up = m_skin.getDrawable("change.up");
		m_restartBtn = new UIButton(btnStyle5);
		m_restartBtn.setBounds((widthScr - widthBtn) - (ppcy/3), heightScr - m_restartBtn.getHeight(), widthBtn/1.3f, height/1.3f);
		m_restartBtn.debug();
		m_restartBtn.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				callRestart();
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
			}
		});
		
		TextButtonStyle btnStyle4 = new TextButtonStyle();
		btnStyle4.up = m_skin.getDrawable("change.up");
		
		m_charSwitchBtn = new UIButton(btnStyle4);
		m_charSwitchBtn.setBounds((widthScr - widthBtn) - (ppcy/3), heightOfBtn, widthBtn, height);
		m_charSwitchBtn.debug();
		m_charSwitchBtn.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				m_button[first] = true;
				playerOneActive = !playerOneActive;
				m_playerSelected = (playerOneActive ? PlayerSelection.ONE : PlayerSelection.TWO);
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				m_button[first] = false;
			}
		});
		
		TextButtonStyle btnStyle3 = new TextButtonStyle();
		btnStyle3.up = m_skin.getDrawable("up.up");
		
		m_jumpBtn = new UIButton(btnStyle3);
		m_jumpBtn.debug();
		m_jumpBtn.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				m_button[jump] = true;
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				m_button[jump] = false;
			}
		});
		
		TextButtonStyle btnStyle6 = new TextButtonStyle();
		btnStyle6.up = m_skin.getDrawable("up.up");
		
		m_actionBtn = new UIButton(btnStyle6);
		m_actionBtn.debug();
		m_actionBtn.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				m_button[action] = true;
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				m_button[action] = false;
			}
		});
		m_bottomBtnsTable.setFillParent(true);
		m_bottomBtnsTable.bottom().left();
		m_bottomBtnsTable.add(m_leftBtn).bottom().left();
		m_bottomBtnsTable.add(m_rightBtn).bottom().left();
		m_bottomBtnsTable.add(m_actionBtn).expand().bottom().right();
		m_bottomBtnsTable.add(m_jumpBtn).bottom().right();
		m_bottomBtnsTable.add(m_charSwitchBtn).bottom().right();
		
		m_topBtnsTable.setFillParent(true);
		m_topBtnsTable.top().right();
		m_topBtnsTable.add(m_restartBtn).top().right();

		m_stage.addActor(m_topBtnsTable);
		m_stage.addActor(m_bottomBtnsTable);
		Gdx.input.setInputProcessor(m_stage);
	}
	
	public void reset(){
		
		for (@SuppressWarnings("unused") boolean b : m_button) {
			b = false;
		}
	}
	
	public void update(float delta, boolean showUI){
		
		if(showUI){
			m_stage.act(delta);
			m_stage.draw();
			Table.drawDebug(m_stage);
			
		}
		m_playerSelected = (playerOneActive ? PlayerSelection.ONE : PlayerSelection.TWO);
	}
	
	private void callRestart(){
		for(LevelEventListener listener: m_levelListeners)
			listener.onRestartLevel();
	}
	
	public boolean isDown(int nr){
		return m_button[nr];
	}

	public void keyDown(int keycode){
	
		if (keycode == Input.Keys.A) {
			m_button[left] = true;
		}
		if(keycode == Input.Keys.D) {
			m_button[right] = true;
		}

		if(keycode == Input.Keys.W) {
			m_button[restart] = true;
		}
		if(keycode == Input.Keys.S) {
			m_button[down] = true;
		}
		if(keycode == Input.Keys.SPACE){
			m_button[jump] = true;
		}
		if(keycode == Input.Keys.F){
			m_button[rag] = true;
		}
		if(keycode == Input.Keys.C){
			m_button[first] = true;
			playerOneActive = !playerOneActive;			
		}
		if(keycode == Input.Keys.F){
			m_button[action] = true;
		}
		
		if(keycode == Input.Keys.R){
			callRestart();
		}
	
	}
	
	public void keyUp(int keycode){
		
		if(keycode == Input.Keys.A) {
			m_button[left] = false;
		}
		if(keycode == Input.Keys.D) {

			m_button[right] = false;
		}

		if(keycode == Input.Keys.W) {
			m_button[restart] = false;
		}
		if(keycode == Input.Keys.S) {
			m_button[down] = false;
		}
		if(keycode == Input.Keys.SPACE){
			m_button[jump] = false;
		}

		if(keycode == Input.Keys.G){
			m_button[rag] = false;
		}
		
		if(keycode == Input.Keys.C){
			m_button[first] = false;
		}
		
		if(keycode == Input.Keys.F){
			m_button[action] = false;
		}
		
	}
	
}
