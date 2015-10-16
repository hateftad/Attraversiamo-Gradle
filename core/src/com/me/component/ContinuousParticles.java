package com.me.component;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by hateftadayon on 16/10/15.
 */
public class ContinuousParticles extends ParticleComponent {
    public ContinuousParticles(String effect, Vector2 position) {
        super(effect, ParticleType.CONTINIOUS, position);
    }
}
