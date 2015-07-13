package com.me.component;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.event.GameEvent;
import com.me.event.GameEventType;

/**
 * Created by hateftadayon on 7/13/15.
 */
public class LevelAnimationComponent extends AnimationComponent {


    public LevelAnimationComponent(String atlas, String skeleton, float scale) {
        super(atlas, skeleton, scale);
    }

    @Override
    public void onNotify(GameEventType event) {

    }

    @Override
    public void setAnimationState(AnimState animationState) {

    }

    @Override
    public Vector2 getPositionRelative(String attachmentName) {
        return null;
    }

    @Override
    public void update(SpriteBatch sb, float dt) {
        m_animationState.update(dt);
        m_animationState.apply(m_skeleton);
        m_skeleton.update(dt);
        m_skeleton.updateWorldTransform();
        m_renderer.draw(sb, m_skeleton);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }
}
