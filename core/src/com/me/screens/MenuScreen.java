package com.me.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.me.attraversiamo.Attraversiamo;
import com.me.component.AnimationComponent;
import com.me.component.LevelAnimationComponent;
import com.me.screens.transitions.FadeInTransitionEffect;
import com.me.screens.transitions.FadeOutTransitionEffect;
import com.me.screens.transitions.TransitionEffect;
import com.me.ui.UIButton;

import java.util.ArrayList;

public class MenuScreen extends AbstractScreen {

    private static final String SCENEPATH = "data/ui/menu";
    private static final float SCALE = 0.5f;

    private Array<AnimationComponent> m_animation;

    public MenuScreen(Attraversiamo game) {
        super(game);
        init();
    }

    @Override
    protected String getName() {
        return getClass().getSimpleName();
    }

    private void init() {
        m_camera.viewportWidth = 800;
        m_camera.viewportHeight = 600;
        m_camera.zoom = 2f;
        m_animation = new Array<AnimationComponent>();

        Vector2 middlePoint = new Vector2(0, 0);
        AnimationComponent scene = new LevelAnimationComponent(SCENEPATH, SCENEPATH, SCALE);
        scene.setUp(middlePoint, "running");
        m_animation.add(scene);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        m_camera.update();
        m_spriteBatch.setProjectionMatrix(m_camera.combined);
        m_spriteBatch.begin();
        for (AnimationComponent comp : m_animation) {
            comp.update(m_spriteBatch, delta);
        }
        m_spriteBatch.end();
        if (Gdx.input.justTouched()) {
            changeScreen();
        }
    }

    private void changeScreen() {
        Screen current = m_game.getScreen();
        m_game.m_loadingScreen = new LoadingScreen(m_game);
        m_game.m_loadingScreen.load(1);

//        ArrayList<TransitionEffect> effects = new ArrayList<TransitionEffect>();
//        effects.add(new FadeOutTransitionEffect(1f));
//        Screen transitionScreen = new TransitionScreen(m_game, current, m_game.m_loadingScreen, effects);

        m_game.setScreen(m_game.m_loadingScreen);
    }

    @Override
    public void pause() {

    }

    @Override
    public void dispose() {
        m_spriteBatch.dispose();
        for (AnimationComponent animationComponent : m_animation) {
            animationComponent.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        //super.resize(width, height);

    }

}
