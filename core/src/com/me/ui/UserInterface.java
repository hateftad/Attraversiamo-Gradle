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

    private Stage m_stage;
    private Skin m_skin;
    private int m_nrOfPlayers;
    private Window pauseWindow;


    public UserInterface(Level level){
		m_stage = new Stage();
        loadSkin();
        m_nrOfPlayers = level.getNumberOfFinishers();
        for (PlayerConfig playerConfig : level.getPlayerConfigs()) {

        }
    }

    private void loadSkin(){
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/ui/buttons.atlas"));
        m_skin = new Skin(atlas);
    }
	
	public void init(){

        UIButton leftBtn = createButton("left.up", new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyDown(Input.Keys.A);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyUp(Input.Keys.A);
            }
        });

        UIButton rightBtn = createButton("right.up",new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyDown(Input.Keys.D);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyUp(Input.Keys.D);
            }
        });

        UIButton menuBtn = createButton("menu.up", new InputListener() {
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
		topBtnsTable.add(menuBtn).width(100).height(100).padLeft(10).padTop(10);
		
		m_stage.addActor(topBtnsTable);
		m_stage.addActor(bottomLeftBtnsTable);


		if(Gdx.app.getType() != ApplicationType.Desktop){
			Gdx.input.setInputProcessor(m_stage);
		}

        createPauseMenu(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), size);
	}

    private void setupBottomRightButtons(int size){

        UIButton jumpBtn = createButton("up.up", new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyDown(Input.Keys.SPACE);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyUp(Input.Keys.SPACE);
            }
        });

        UIButton actionBtn = createButton("action.up", new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyDown(Input.Keys.F);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                InputManager.getInstance().keyUp(Input.Keys.F);
            }
        });

        Table bottomRightBtnsTable = new Table();
        if(m_nrOfPlayers > 1) {
            final TextButtonStyle btnStyleFirst = new TextButtonStyle();
            final TextButtonStyle btnStyleSecond = new TextButtonStyle();
            btnStyleFirst.up = m_skin.getDrawable("change1.up");
            btnStyleSecond.up = m_skin.getDrawable("change2.up");

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
            m_stage.addActor(charSwitchTable);
            charSwitchTable.setFillParent(true);
            charSwitchTable.bottom().right().padBottom(size);
            charSwitchTable.add(charSwitchBtn).bottom().right().width(size * 1.5f).height(size * 1.5f);
        }
        bottomRightBtnsTable.setFillParent(true);
        bottomRightBtnsTable.bottom().right().setHeight(size);
        bottomRightBtnsTable.add(actionBtn).bottom().right().space(20).width(size).height(size);
        bottomRightBtnsTable.add(jumpBtn).bottom().right().padRight(size).width(size).height(size);

        m_stage.addActor(bottomRightBtnsTable);
    }

    public void createPauseMenu(int width, int height, int size){

        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"), new TextureAtlas(Gdx.files.internal("data/uiskin.atlas")));

        TextButton continueButton = new TextButton("CONTINUE", skin);
		continueButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setPauseVisibility(false);
			}
		});

        TextButton restartButton = new TextButton("RESTART", skin);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPauseVisibility(false);
                InputManager.getInstance().callRestart();
            }
        });

        pauseWindow = new Window("", skin);
        pauseWindow.setSize(width, height);
        pauseWindow.setPosition(width / 2 - pauseWindow.getWidth() / 2, height / 2 - pauseWindow.getHeight() / 2);
        pauseWindow.add(continueButton).width(size * 1.5f).height(size * 1.5f).row();
        pauseWindow.add(restartButton).width(size * 1.5f).height(size * 1.5f).row();
        m_stage.addActor(pauseWindow);
        pauseWindow.setVisible(false);
    }

    public void setPauseVisibility(boolean visibility){
        pauseWindow.setVisible(visibility);
    }

    private UIButton createButton(String drawableName, InputListener inputListener){
        TextButtonStyle btnStyle = new TextButtonStyle();
        btnStyle.up = m_skin.getDrawable(drawableName);
        UIButton button = createButton(btnStyle);
        button.addListener(inputListener);
        return button;
    }

    private UIButton createButton(TextButtonStyle btnStyle){
        return new UIButton(btnStyle);
    }
	
	public void update(float delta){
        m_stage.act(delta);
        m_stage.draw();
	}
}
