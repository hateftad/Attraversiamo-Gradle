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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class UserInterface {

    private Stage m_stage;
    private Skin m_skin;


    public UserInterface(){
		m_stage = new Stage();
        loadSkin();
	}

    private void loadSkin(){
        TextureAtlas m_atlas = new TextureAtlas(Gdx.files.internal("data/ui/buttons.atlas"));
        m_skin = new Skin(m_atlas);
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
                InputManager.getInstance().callRestart();
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });

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

        final TextButtonStyle btnStyleFirst = new TextButtonStyle();
        final TextButtonStyle btnStyleSecond = new TextButtonStyle();
        btnStyleFirst.up = m_skin.getDrawable("change1.up");
        btnStyleSecond.up = m_skin.getDrawable("change2.up");
        final UIButton charSwitchBtn = createButton(btnStyleFirst);
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
                if(InputManager.getInstance().playerOneActive){
                    charSwitchBtn.setStyle(btnStyleSecond);
                } else {
                    charSwitchBtn.setStyle(btnStyleFirst);
                }
            }
        });

		int size = Gdx.graphics.getWidth() / 10;

        Table bottomLeftBtnsTable = new Table();
        Table bottomRightBtnsTable = new Table();
        Table charSwitchTable = new Table();
        Table topBtnsTable = new Table();
		
		bottomLeftBtnsTable.setFillParent(true);
		bottomLeftBtnsTable.bottom().left();
		bottomLeftBtnsTable.add(leftBtn).bottom().left().space(20).width(size).height(size).padRight(20).padLeft(20);
		bottomLeftBtnsTable.add(rightBtn).bottom().left().width(size).height(size);
		
		bottomRightBtnsTable.setFillParent(true);
		bottomRightBtnsTable.bottom().right().setHeight(size);
		bottomRightBtnsTable.add(actionBtn).bottom().right().space(20).width(size).height(size);
		bottomRightBtnsTable.add(jumpBtn).bottom().right().padRight(size).width(size).height(size);

        charSwitchTable.setFillParent(true);
        charSwitchTable.bottom().right().padBottom(size);
        charSwitchTable.add(charSwitchBtn).bottom().right().width(size * 1.5f).height(size * 1.5f);
		
		topBtnsTable.setFillParent(true);
		topBtnsTable.top().left();
		topBtnsTable.add(menuBtn).width(100).height(100).padLeft(10).padTop(10);
		
		m_stage.addActor(topBtnsTable);
		m_stage.addActor(bottomLeftBtnsTable);
		m_stage.addActor(bottomRightBtnsTable);
        m_stage.addActor(charSwitchTable);

		if(Gdx.app.getType() != ApplicationType.Desktop){
			Gdx.input.setInputProcessor(m_stage);
		}
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
