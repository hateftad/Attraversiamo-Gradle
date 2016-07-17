package com.me.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.me.attraversiamo.Attraversiamo;
import com.me.component.AnimationComponent;
import com.me.component.LevelAnimationComponent;
import com.me.screens.transitions.FadeInTransitionEffect;
import com.me.screens.transitions.FadeOutTransitionEffect;
import com.me.screens.transitions.TransitionEffect;

import java.util.ArrayList;

public class SplashScreen extends AbstractScreen {

    private AnimationComponent animation;
    private boolean changingScreen;
    private boolean timerIsOn = false;

    public SplashScreen(Attraversiamo game) {
        super(game);
        this.camera.viewportWidth = 800;
        this.camera.viewportHeight = 600;
        this.animation = new LevelAnimationComponent("data/intro", "data/intro", 1f);
        this.animation.setUp(new Vector2(0, 0), "intro");
        this.camera.zoom = 2f;
    }

    @Override
    protected String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        animation.update(spriteBatch, delta / 2);
        spriteBatch.end();

        if (!timerIsOn) {
            timerIsOn = true;
            changeScreenTask = Timer.schedule(changeScreenTask, 3);
        } else if (Gdx.input.isTouched()) {
            Timer.instance().clear();
            if(!changingScreen) {
                changeScreen();
            }
        }
    }

    Task changeScreenTask = new Task() {
        @Override
        public void run() {
            changeScreen();
        }
    };

    private void changeScreen() {
        changingScreen = true;
        Screen current = this;
        Screen next = new MenuScreen(game);

        ArrayList<TransitionEffect> effects = new ArrayList<>();

        effects.add(new FadeOutTransitionEffect(1f));
        effects.add(new FadeInTransitionEffect(2f));

        Screen transitionScreen = new TransitionScreen(game, current, next, effects);

        game.setScreen(transitionScreen);
        game.showBannerAd(true);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        game.setScreenName("Splash Screen");
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        animation.dispose();
    }

}
