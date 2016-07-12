package com.me.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.me.config.PlayerConfig;
import com.me.level.Level;

public class UserInterface {

    private Stage stage;
    private Skin skin;
    private int nrOfPlayers;
    private Window pauseWindow;
    private UIButton jumpBtn;


    public UserInterface(Level level){

		stage = new Stage();

        loadSkin();
        nrOfPlayers = level.getNumberOfFinishers();
        for (PlayerConfig playerConfig : level.getPlayerConfigs()) {

        }
    }

    public Stage getStage(){
        return stage;
    }

    private void loadSkin(){
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/ui/hud/buttons.atlas"));
        skin = new Skin(atlas);
    }
	
	public void init(){


        UIButton leftBtn = createButton("left.up", "left.down");
        final Container leftBtnWrapper = createWrapper(leftBtn, 1);
        leftBtnWrapper.bottom().left();
        leftBtn.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyDown(Input.Keys.A);
                leftBtnWrapper.setScale(0.95f);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyUp(Input.Keys.A);
                leftBtnWrapper.setScale(1f);
            }
        });
        leftBtn.bottom().left();

        UIButton rightBtn = createButton("right.up", "right.down");
        final Container rightBtnWrapper = createWrapper(rightBtn, 1);

        rightBtn.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyDown(Input.Keys.D);
                rightBtnWrapper.setScale(0.95f);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyUp(Input.Keys.D);
                rightBtnWrapper.setScale(1);
            }
        });

        UIButton menuBtn = createButton("menu.up", "menu.down");
        final Container menuBtnWrapper = createWrapper(menuBtn, 0.8f);
        menuBtn.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuBtnWrapper.setScale(0.85f);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                menuBtnWrapper.setScale(.8f);
                setPauseVisibility(true);
            }
        });

		int size = Gdx.graphics.getWidth() / 10;
        setupBottomRightButtons(size);
        rightBtnWrapper.width(size).height(size);
        Table bottomLeftBtnsTable = new Table();
        bottomLeftBtnsTable.setFillParent(true);
        bottomLeftBtnsTable.setClip(true);
		bottomLeftBtnsTable.add(leftBtnWrapper.width(size).height(size)).padLeft(20).space(50);
		bottomLeftBtnsTable.add(rightBtnWrapper.width(size).height(size));
        bottomLeftBtnsTable.bottom().left();
        bottomLeftBtnsTable.padLeft(10);


        Table topButtonsTable = new Table();
        topButtonsTable.setFillParent(true);
        topButtonsTable.add(menuBtnWrapper.width(size).height(size)).top().left();
        topButtonsTable.padLeft(10);
        topButtonsTable.top().left();


		stage.addActor(bottomLeftBtnsTable);
		stage.addActor(topButtonsTable);

        createPauseMenu(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), size);
	}

    private void setupBottomRightButtons(int size){

        jumpBtn = createButton("jump.up", "jump.down");
        final Container jumpBtnWrapper = createWrapper(jumpBtn, 1f);
        jumpBtn.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyDown(Input.Keys.SPACE);
                jumpBtnWrapper.setScale(0.95f);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyUp(Input.Keys.SPACE);
                jumpBtnWrapper.setScale(1f);
            }
        });

        UIButton actionBtn = createButton("action.up", "action.down");
        final Container actionBtnWrapper = createWrapper(actionBtn, 1f);
        actionBtn.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyDown(Input.Keys.F);
                actionBtnWrapper.setScale(0.95f);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyUp(Input.Keys.F);
                actionBtnWrapper.setScale(1f);
            }
        });

        Table bottomRightBtnsTable = new Table();
        if(nrOfPlayers > 1) {
            final TextButtonStyle btnStyleFirst = new TextButtonStyle();
            final TextButtonStyle btnStyleSecond = new TextButtonStyle();
            btnStyleFirst.up = skin.getDrawable("change1.up");
            btnStyleSecond.up = skin.getDrawable("change2.up");

            final UIButton charSwitchBtn = createButton(btnStyleSecond);
            charSwitchBtn.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    InputManager.getInstance().characterSwitch();
                    return true;
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    InputManager.getInstance().keyUp(Input.Keys.C);
                }
            });

            charSwitchBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (InputManager.getInstance().playerOneActive) {
                        charSwitchBtn.setStyle(btnStyleSecond);
                    } else {
                        charSwitchBtn.setStyle(btnStyleFirst);
                    }
                }
            });

            Table charSwitchTable = new Table();
            stage.addActor(charSwitchTable);
            charSwitchTable.setFillParent(true);
            charSwitchTable.bottom().right().padBottom(size);
            charSwitchTable.add(charSwitchBtn).bottom().right().width(size * 1.5f).height(size * 1.5f);
        }
        bottomRightBtnsTable.setFillParent(true);
        bottomRightBtnsTable.bottom().right();
        bottomRightBtnsTable.add(actionBtnWrapper.width(size).height(size)).bottom().right().space(20);
        bottomRightBtnsTable.add(jumpBtnWrapper.width(size).height(size)).bottom().right().padRight(size);

        stage.addActor(bottomRightBtnsTable);
    }

    public void createPauseMenu(int width, int height, int size){



        UIButton continueButton = createButton("continue.up", "continue.up", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPauseVisibility(false);
            }
        });

        Container continueWrapper = createWrapper(continueButton, 1f);
        UIButton restartButton = createButton("reset.up", "reset.up", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPauseVisibility(false);
                InputManager.getInstance().callRestart();
            }
        });

        Container restartWrapper = createWrapper(restartButton, 1f);
        UIButton backButton = createButton("back.up", "back.up", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPauseVisibility(false);

            }
        });

        Skin skin = new Skin(Gdx.files.internal("data/ui/window/uiskin.json"), new TextureAtlas(Gdx.files.internal("data/ui/window/window.atlas")));
        Container backWrapper = createWrapper(backButton, 1f);

        pauseWindow = new Window("", skin);
        pauseWindow.setSize(width, height);
        pauseWindow.setPosition(width / 2 - pauseWindow.getWidth() / 2, height / 2 - pauseWindow.getHeight() / 2);
        pauseWindow.add(continueWrapper).height(continueButton.getPrefHeight()).width(continueButton.getPrefWidth()).padBottom(50).row();
        pauseWindow.add(restartWrapper).height(restartButton.getPrefHeight()).width(restartButton.getPrefWidth()).padBottom(50).row();
        pauseWindow.add(backWrapper).height(backButton.getPrefHeight()).width(backButton.getPrefWidth()).row();
        stage.addActor(pauseWindow);
        pauseWindow.setVisible(false);
    }

    public void setPauseVisibility(boolean visibility){
        pauseWindow.setVisible(visibility);
    }

    public boolean isPaused(){
        return pauseWindow.isVisible();
    }

    private UIButton createButton(String upDrawable, String downDrawable, InputListener inputListener){
        TextButtonStyle btnStyle = new TextButtonStyle();
        btnStyle.up = skin.getDrawable(upDrawable);
        btnStyle.down = skin.getDrawable(downDrawable);
        UIButton button = createButton(btnStyle);
        button.addListener(inputListener);
        return button;
    }

    private UIButton createButton(String upDrawable, String downDrawable){
        TextButtonStyle btnStyle = new TextButtonStyle();
        btnStyle.up = skin.getDrawable(upDrawable);
        btnStyle.down = skin.getDrawable(downDrawable);
        UIButton button = createButton(btnStyle);
        return button;
    }

    private UIButton createButton(TextButtonStyle btnStyle){
        return new UIButton(btnStyle);
    }

    private Container createWrapper(WidgetGroup btn, float scale){
        Container wrapper = new Container(btn);
        wrapper.setTransform(true);
        wrapper.setOrigin(wrapper.getPrefWidth() / 2, wrapper.getPrefHeight() / 2);
        wrapper.setScale(scale);
        return wrapper;
    }
	
	public void update(float delta){

        jumpBtn.setDisabled(InputManager.getInstance().playerOneActive);

        stage.act(delta);
        stage.draw();
	}
}
