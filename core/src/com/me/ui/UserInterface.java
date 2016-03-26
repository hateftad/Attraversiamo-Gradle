package com.me.ui;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

    private void loadSkin(){
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/ui/buttons.atlas"));
        skin = new Skin(atlas);
    }
	
	public void init(){

        UIButton leftBtn = createButton("left.up", "left.down", new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyDown(Input.Keys.A);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyUp(Input.Keys.A);
            }
        });

        UIButton rightBtn = createButton("right.up", "right.down",new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyDown(Input.Keys.D);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyUp(Input.Keys.D);
            }
        });

        UIButton menuBtn = createButton("menu.up", "menu.down", new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setPauseVisibility(true);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });

		int size = Gdx.graphics.getWidth() / 10;
        setupBottomRightButtons(size);

        Table bottomLeftBtnsTable = new Table();
        Table topBtnsTable = new Table();
		
		bottomLeftBtnsTable.setFillParent(true);
		bottomLeftBtnsTable.bottom().left();
		bottomLeftBtnsTable.add(leftBtn).bottom().left().space(20).width(size).height(size).padRight(20).padLeft(20);
		bottomLeftBtnsTable.add(rightBtn).bottom().left().width(size).height(size);

		topBtnsTable.setFillParent(true);
		topBtnsTable.top().left();
		topBtnsTable.add(menuBtn).width(size*0.6f).height(size*0.6f).padLeft(20).padTop(20);
		
		stage.addActor(topBtnsTable);
		stage.addActor(bottomLeftBtnsTable);


		if(Gdx.app.getType() != ApplicationType.Desktop){
			Gdx.input.setInputProcessor(stage);
		}

        createPauseMenu(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), size);
	}

    private void setupBottomRightButtons(int size){


        jumpBtn = createButton("jump.up", "jump.down", new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyDown(Input.Keys.SPACE);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyUp(Input.Keys.SPACE);
            }
        });

        UIButton actionBtn = createButton("action.up", "action.down", new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyDown(Input.Keys.F);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyUp(Input.Keys.F);
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
        bottomRightBtnsTable.bottom().right().setHeight(size);
        bottomRightBtnsTable.add(actionBtn).bottom().right().space(20).width(size).height(size);
        bottomRightBtnsTable.add(jumpBtn).bottom().right().padRight(size).width(size).height(size);

        stage.addActor(bottomRightBtnsTable);
    }

    public void createPauseMenu(int width, int height, int size){

        Skin skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"), new TextureAtlas(Gdx.files.internal("data/ui/buttons.atlas")));

        UIButton continueButton = createButton("continue.up", "continue.up", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPauseVisibility(false);
            }
        });

        UIButton restartButton = createButton("reset.up", "reset.up", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPauseVisibility(false);
                InputManager.getInstance().callRestart();
            }
        });

        UIButton backButton = createButton("back.up", "back.up", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPauseVisibility(false);

            }
        });

        pauseWindow = new Window("", skin);
        pauseWindow.setSize(width, height);
        pauseWindow.setPosition(width / 2 - pauseWindow.getWidth() / 2, height / 2 - pauseWindow.getHeight() / 2);
        pauseWindow.add(continueButton).width(width/3).height(size * 0.8f).padTop(10).row();
        pauseWindow.add(restartButton).width(width/3).height(size * 0.8f).padTop(10).row();
        pauseWindow.add(backButton).width(width/3).height(size * 0.8f).padTop(10).row();
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

    private UIButton createButton(TextButtonStyle btnStyle){
        return new UIButton(btnStyle);
    }
	
	public void update(float delta){

        jumpBtn.setDisabled(InputManager.getInstance().playerOneActive);

        stage.act(delta);
        stage.draw();
	}
}
