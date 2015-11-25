package com.me.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.me.utils.ImmediateModeRendererUtils;

/**
 * Created by hateftadayon on 25/11/15.
 */
public class FadeInTransitionEffect extends TransitionEffect {
    private Color color = new Color();

    public FadeInTransitionEffect(float duration) {
        super(duration);
    }

    @Override
    public void render(float delta, Screen current, Screen next) {
        next.render(delta);
        color.set(0f, 0f, 0f, 1 - getAlpha());
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        ImmediateModeRendererUtils.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ImmediateModeRendererUtils.fillRectangle(0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), color);
        Gdx.gl20.glDisable(GL20.GL_BLEND);
    }
}
