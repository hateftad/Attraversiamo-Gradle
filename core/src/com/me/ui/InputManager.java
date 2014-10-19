package com.me.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.me.listeners.LevelEventListener;

public class InputManager {

	private int left = 0, right = 1, restart = 2, down = 3, jump = 4, rag = 5, first = 6, action = 7, skinChange = 8; 
	
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
	
	public void addEventListener(LevelEventListener listener){
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
	private String skinName = "color";
	public String toggleSkins(){
		if(skinName.equals("color")){
			skinName = "silhouette";
			return skinName;
		}
		else if(skinName.equals("silhouette")){
			skinName = "color";
			return skinName;
		}
		return skinName;
	}
	
	public String getSkinName(){
		return skinName;
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
		if(keycode == Input.Keys.Q){
			m_button[skinChange] = true;
			toggleSkins();
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
		if(keycode == Input.Keys.Q){
			m_button[skinChange] = false;
		}
		
	}
	
}
