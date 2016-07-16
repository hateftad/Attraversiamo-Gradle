package com.me.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.me.attraversiamo.Attraversiamo;
import com.me.component.AnimationComponent;
import com.me.component.LevelAnimationComponent;
import com.me.component.PlayerAnimationComponent;
import com.me.level.LevelInfo;
import com.me.manager.PersistenceManager;
import com.me.screens.transitions.FadeInTransitionEffect;
import com.me.screens.transitions.FadeOutTransitionEffect;
import com.me.screens.transitions.TransitionEffect;
import com.me.ui.InputManager;
import com.me.ui.UIButton;

import java.util.ArrayList;

public class MenuScreen extends AbstractScreen {

    private static final String SCENEPATH = "data/ui/start_menu/menu";
    private static final String CHARPATH = "data/character/";
    private static final float SCALE = 1f;

    private Array<AnimationComponent> animation;
    private Stage stage = new Stage();
    private Skin skin;

    public MenuScreen(Attraversiamo game) {
        super(game);
        init();
    }

    @Override
    protected String getName() {
        return getClass().getSimpleName();
    }

    private void init() {
        this.camera.viewportWidth = 1024;
        this.camera.viewportHeight = 720;
        this.camera.zoom = 3.0f;

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/ui/start_menu/menu.atlas"));
        skin = new Skin(atlas);

        Table middleButtons = new Table();
        UIButton startButton = createButton("menuButtons/input/start", "menuButtons/input/start");

        final Container wrapper = createWrapper(startButton, 1);
        startButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                wrapper.setScale(0.95f);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                changeScreen();
                wrapper.setScale(1f);
            }
        });

        middleButtons.add(wrapper.height(startButton.getPrefHeight()).width(startButton.getPrefWidth()).padTop(100));
        middleButtons.setFillParent(true);
        middleButtons.setClip(true);
        middleButtons.top().padTop(100);
        stage.addActor(middleButtons);

        Table soundTable = new Table();
        soundTable.setFillParent(true);
        Stack stack = new Stack();
        final UIButton soundButtonOn = createButton("menuButtons/input/sound_on", "menuButtons/input/sound_on");
        final UIButton soundButtonOff = createButton("menuButtons/input/sound_off", "menuButtons/input/sound_off");
        final Container soundButtonOnwrapper = createWrapper(soundButtonOn, 0.8f);
        soundButtonOn.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                soundButtonOnwrapper.setScale(0.75f);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                soundButtonOnwrapper.setScale(0.8f);
                PersistenceManager.getInstance().toggleSound(true);
                soundButtonOn.setVisible(false);
                soundButtonOff.setVisible(true);
            }
        });

        final Container soundButtonOffwrapper = createWrapper(soundButtonOff, 0.8f);
        soundButtonOff.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                soundButtonOffwrapper.setScale(0.75f);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                soundButtonOffwrapper.setScale(0.8f);
                PersistenceManager.getInstance().toggleSound(false);
                soundButtonOff.setVisible(false);
                soundButtonOn.setVisible(true);
            }
        });
        stack.add(soundButtonOffwrapper);
        stack.add(soundButtonOnwrapper);
        soundTable.add(stack);
        soundTable.bottom().left().padBottom(soundButtonOff.getPrefHeight());

        stage.addActor(soundTable);

        this.animation = new Array<>();

        Vector2 middlePoint = new Vector2(0, 0);
        AnimationComponent scene = new LevelAnimationComponent(atlas, SCENEPATH, SCALE);
        scene.setUp(middlePoint, "start");
        scene.playAnimation("start", true);

        AnimationComponent playerOne = new LevelAnimationComponent(CHARPATH + "smallCharacter/bigGuy", CHARPATH + "smallCharacter/bigGuySkeleton", 0.5f);
        playerOne.setUp(new Vector2(240, -338), "running");
        playerOne.playAnimation("running", true);
        playerOne.setSkin("silhouette");
        AnimationComponent playerTwo = new LevelAnimationComponent(CHARPATH + "littleGirl/littleGirl", CHARPATH + "littleGirl/littleGirlSkeleton", 0.5f);
        playerTwo.setUp(new Vector2(0, -338), "running");
        playerTwo.playAnimation("running", true);
        playerTwo.setSkin("silhouette");
        animation.add(scene);
        animation.add(playerOne);
        animation.add(playerTwo);
        Gdx.input.setInputProcessor(stage);
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
        for (int i = 0; i < animation.size; i++) {
            AnimationComponent comp = animation.get(i);
            comp.update(spriteBatch, delta);
        }

        spriteBatch.end();

        stage.act(delta);
        stage.draw();
    }

    private void changeScreen() {
        Screen current = game.getScreen();
        game.loadingScreen = new LoadingScreen(game);
        LevelInfo levelInfo = PersistenceManager.getInstance().getLevelInfo();
//        game.loadingScreen.load(levelInfo.getCurrentLevel());
        game.loadingScreen.load(7);

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

    private Container createWrapper(WidgetGroup btn, float scale){
        Container wrapper = new Container(btn);
        wrapper.setTransform(true);
        wrapper.setOrigin(wrapper.getPrefWidth() / 2, wrapper.getPrefHeight() / 2);
        wrapper.setScale(scale);
        return wrapper;
    }

    private UIButton createButton(String upDrawable, String downDrawable) {
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.up = skin.getDrawable(upDrawable);
        btnStyle.down = skin.getDrawable(downDrawable);
        UIButton button = createButton(btnStyle);
        return button;
    }

    private UIButton createButton(TextButton.TextButtonStyle btnStyle) {
        return new UIButton(btnStyle);
    }

}
