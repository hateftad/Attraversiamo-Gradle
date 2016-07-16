package com.me.component;

import com.me.ui.UserInterface;

/**
 * Created by hateftadayon on 7/16/16.
 */
public class HudComponent extends BaseComponent {

    private UserInterface userInterface;

    public HudComponent(UserInterface userInterface){
        this.userInterface = userInterface;
    }

    public void update(float delta){
        userInterface.update(delta);
    }

    @Override
    public void dispose() {
        userInterface.dispose();
    }

    public boolean restartPressed(){
        return userInterface.restartPressed();
    }

    public void setRestartPRessed(boolean restart){
        userInterface.setRestartPressed(restart);
    }

    @Override
    public void restart() {

    }

    public boolean isPaused() {
        return userInterface.isPaused();
    }
}
