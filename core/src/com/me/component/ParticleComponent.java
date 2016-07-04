package com.me.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.me.utils.Converters;

public abstract class ParticleComponent extends BaseComponent {

	protected ParticleEffect particle;
    private Vector2 position = Vector2.Zero;
    protected ParticleEffectPool pool;
    protected Array<ParticleEffectPool.PooledEffect> effects;
    protected String effect;
    private boolean isStarted;

	public ParticleComponent(String effect, int max){
		particle = loadParticle(effect);
		pool = new ParticleEffectPool(particle, 0, max);
		effects = new Array<>();
	}

	public ParticleComponent(String effect, Vector2 position){
		particle = loadParticle(effect);
		pool = new ParticleEffectPool(particle, 10, 10);
		effects = new Array<>();
		setPosition(Converters.ToWorld(position));
	}

    public ParticleComponent(){}

	protected ParticleEffect loadParticle(String effectName){
        effect = effectName;
		ParticleEffect particle = new ParticleEffect();
		particle.load(Gdx.files.internal("data/particles/" + effect + ".p"), Gdx.files.internal("data"));
		particle.start();
		return particle;
	}

	public void start(){
		ParticleEffectPool.PooledEffect effect = pool.obtain();

		effect.setPosition(Converters.ToWorld(position.x), Converters.ToWorld(position.y));
		effects.add(effect);
        setStarted(true);
	}

	public boolean isCompleted(){
		return effects.size == 0;
	}

	public void setPosition(Vector2 position){
		this.position = position;
	}

    public boolean needsDrawAndUpdate(){
        return effects.size > 0;
    }

	public void draw(SpriteBatch spriteBatch, float delta){
		for(ParticleEffectPool.PooledEffect effect : effects) {
			effect.draw(spriteBatch, delta);
			if(effect.isComplete()) {
				effects.removeValue(effect, true);
				effect.free();
			}
		}
	}

	public void dispose(){
        pool.freeAll(effects);
		particle.dispose();
	}

	@Override
	public void restart() {

	}

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }
}
