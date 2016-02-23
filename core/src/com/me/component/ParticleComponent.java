package com.me.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.me.utils.Converters;

public abstract class ParticleComponent extends BaseComponent {
    public enum ParticleType{
        SINGLE,
        PORTAL,
        CONTINUOUS
    }

	protected ParticleEffect m_particle;
    private Vector2 m_position = Vector2.Zero;
    protected ParticleType m_type;
    protected ParticleEffectPool m_pool;
    protected Array<ParticleEffectPool.PooledEffect> m_effects;
    protected String m_effect;

	public ParticleComponent(String effect, ParticleType type, int max){
		m_particle = loadParticle(effect);
		m_pool = new ParticleEffectPool(m_particle, 0, max);
		m_effects = new Array<>();
		m_type = type;
	}

	public ParticleComponent(String effect, ParticleType type, Vector2 position){
		m_particle = loadParticle(effect);
		m_pool = new ParticleEffectPool(m_particle, 10, 10);
		m_effects = new Array<>();
		m_type = type;
		setPosition(Converters.ToWorld(position));
	}

    public ParticleComponent(){}

	protected ParticleEffect loadParticle(String effectName){
        m_effect = effectName;
		ParticleEffect particle = new ParticleEffect();
		particle.load(Gdx.files.internal("data/particles/" + m_effect + ".p"), Gdx.files.internal("data"));
		particle.start();
		return particle;
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

	public void dispose(){
        m_pool.freeAll(m_effects);
		m_particle.dispose();
	}

	@Override
	public void restart() {

	}

}
