package com.me.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParticleComponent extends BaseComponent {

	public enum ParticleType{
		PORTAL,
		PICKUP
	}
	
	private boolean m_start = false;
	private ParticleEffect m_particles;
	private boolean m_setToStart;
	private ParticleType m_type;

	public ParticleComponent(String effect, ParticleType type){
		m_particles = new ParticleEffect();
		m_particles.load(Gdx.files.internal("data/"+effect+".p"), Gdx.files.internal("data"));
		m_type = type;
	}
	
	public ParticleType getType(){
		return m_type;
	}

	public void setToStart(boolean start){
		m_setToStart = start;
	}
	
	public boolean isSetToStart(){
		return m_setToStart;
	}
	
	public void start(){
		m_particles.start();
		m_start = true;
	}

	public boolean isStarted(){
		return m_start;
	}
	public void setStart(boolean s){
		this.m_start = s;
	}

	public boolean isCompleted(){
		return m_particles.isComplete();
	}

	public void setPosition(float x, float y){
		m_particles.setPosition(x, y);
	}

	public void draw(SpriteBatch sb, float delta){
		m_particles.update(delta);
		m_particles.draw(sb);
	}
	
	public void dispose(){
		m_particles.dispose();
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		m_start = false;
		m_setToStart = false;
	}

}
