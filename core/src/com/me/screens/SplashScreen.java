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

    private AnimationComponent m_animation;
    private boolean timerIsOn = false;

    public SplashScreen(Attraversiamo game) {

        super(game);
        m_camera.viewportWidth = 800;
        m_camera.viewportHeight = 600;
        m_animation = new LevelAnimationComponent("data/intro", "data/intro", 1f);
        m_animation.setUp(new Vector2(0, 0), "intro");
        m_camera.zoom = 2f;
        game.getPlayServices().signIn();
    }

    @Override
    protected String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        m_camera.update();
        m_spriteBatch.setProjectionMatrix(m_camera.combined);
        m_spriteBatch.begin();
        m_animation.update(m_spriteBatch, delta / 2);
        m_spriteBatch.end();

        if (!timerIsOn) {
            timerIsOn = true;

            Timer.schedule(new Task() {

                @Override
                public void run() {
                    changeScreen();
                }

            }, 3);

        } else if (Gdx.input.isTouched()) {
            Timer.instance().clear();
            changeScreen();
        }
    }

    private void changeScreen() {
        Screen current = m_game.getScreen();
        Screen next = new MenuScreen(m_game);

//        ArrayList<TransitionEffect> effects = new ArrayList<TransitionEffect>();
//
//        effects.add(new FadeOutTransitionEffect(1f));
//        effects.add(new FadeInTransitionEffect(1f));
//
//        Screen transitionScreen = new TransitionScreen(m_game, current, next, effects);

        m_game.setScreen(next);
        m_game.showAd(true);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        m_game.setScreenName("Splash Screen");
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
        m_animation.dispose();
    }

}
