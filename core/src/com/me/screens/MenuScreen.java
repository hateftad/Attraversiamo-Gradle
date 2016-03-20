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

    private Array<AnimationComponent> animation;

    public MenuScreen(Attraversiamo game) {
        super(game);
        init();
    }

    @Override
    protected String getName() {
        return getClass().getSimpleName();
    }

    private void init() {
        this.camera.viewportWidth = 800;
        this.camera.viewportHeight = 600;
        this.camera.zoom = 2f;
        this.animation = new Array<>();

        Vector2 middlePoint = new Vector2(0, 0);
        AnimationComponent scene = new LevelAnimationComponent(SCENEPATH, SCENEPATH, SCALE);
        scene.setUp(middlePoint, "running");
        animation.add(scene);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (AnimationComponent comp : animation) {
            comp.update(spriteBatch, delta);
        }
        spriteBatch.end();
        if (Gdx.input.justTouched()) {
            changeScreen();
        }
    }

    private void changeScreen() {
        Screen current = game.getScreen();
        game.loadingScreen = new LoadingScreen(game);
        game.loadingScreen.load(1);

//        ArrayList<TransitionEffect> effects = new ArrayList<TransitionEffect>();
//        effects.add(new FadeOutTransitionEffect(1f));
//        Screen transitionScreen = new TransitionScreen(game, current, game.loadingScreen, effects);

        game.setScreen(game.loadingScreen);
    }

    @Override
    public void pause() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        for (AnimationComponent animationComponent : animation) {
            animationComponent.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        //super.resize(width, height);

    }

}
