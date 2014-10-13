package com.me.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
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
	
	public PlayerSelection m_playerSelected;
	public boolean playerOneActive;
	private ArrayList<LevelEventListener> m_levelListeners;

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

		for (@SuppressWarnings("unused") boolean b : m_button) {
			b = false;
		}
		
	}
	
	public void characterSwitch(){
		m_button[first] = true;
		playerOneActive = !playerOneActive;
		m_playerSelected = (playerOneActive ? PlayerSelection.ONE : PlayerSelection.TWO);
	}
	
	public void reset(){
		
		for (@SuppressWarnings("unused") boolean b : m_button) {
			b = false;
		}
	}
	
	public void update(){
		m_playerSelected = (playerOneActive ? PlayerSelection.ONE : PlayerSelection.TWO);
	}
	
	public void callRestart(){
		for(LevelEventListener listener: m_levelListeners){
			listener.onRestartLevel();
		}
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
