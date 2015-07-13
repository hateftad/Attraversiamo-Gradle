package com.me.component;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.me.event.GameEvent;
import com.me.event.GameEventType;

public class ParticleComponent extends GameEventObserverComponent {



    public enum ParticleType{
		PORTAL,
		PICKUP;
    }
	private ParticleEffect m_particle;

    private ParticleType m_type;
    private ParticleEffectPool m_pool;
    private Array<ParticleEffectPool.PooledEffect> m_effects;
    private Vector2 m_position = Vector2.Zero;
	public ParticleComponent(String effect, ParticleType type, int max){
		m_particle = new ParticleEffect();
		m_particle.load(Gdx.files.internal("data/"+effect+".p"), Gdx.files.internal("data"));
		m_particle.start();

		m_pool = new ParticleEffectPool(m_particle, 0, max);
		m_effects = new Array<ParticleEffectPool.PooledEffect>();

		m_type = type;
	}

	public ParticleType getType(){
		return m_type;
	}

	public void start(){
		ParticleEffectPool.PooledEffect effect = m_pool.obtain();
		effect.setPosition(m_position.x, m_position.y);
		m_effects.add(effect);
	}

	public boolean isCompleted(){
		return m_effects.size == 0;
	}

	public void setPosition(Vector2 position){
		m_position = position.cpy();
	}

    public boolean needsDrawAndUpdate(){
        return m_effects.size > 0;
    }

	public void draw(SpriteBatch spriteBatch, float delta){
		for(ParticleEffectPool.PooledEffect effect : m_effects) {
			effect.draw(spriteBatch, delta);
			if(effect.isComplete()) {
				m_effects.removeValue(effect, true);
				effect.free();
			}
		}
	}

    @Override
    public void onNotify(GameEventType event) {
        if(event == GameEventType.AllReachedEnd){
            if(m_type == ParticleType.PORTAL){
                start();
            }
        }
    }

	public void dispose(){
		m_particle.dispose();
	}

	@Override
	public void restart() {

	}

}
