package com.me.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.me.listeners.LevelEventListener;

public class InputManager {

    public static int left = 0, right = 1, restart = 2, down = 3, jump = 4, rag = 5, first = 6, action = 7, skinChange = 8;

    public boolean shouldLockCamera() {
        return shouldLockCamera;
    }

    public void setShouldLockCamera(boolean shouldLockCamera) {
        this.shouldLockCamera = shouldLockCamera;
    }

    public enum PlayerSelection {
        ONE,
        TWO
    }

    public PlayerSelection playerSelected;
    public boolean playerOneActive;
    private ArrayList<LevelEventListener> levelListeners;
    private boolean shouldLockCamera = true;

    private boolean[] button = new boolean[10];

    private static InputManager instance = null;

    public static InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    public void addEventListener(LevelEventListener listener) {
        levelListeners.add(listener);
    }

    public void setSelectedPlayer(int nr, boolean active) {

        if (nr == 1 && active) {
            playerOneActive = true;
            playerSelected = PlayerSelection.ONE;
        } else if (nr == 2 && active) {
            playerOneActive = false;
            playerSelected = PlayerSelection.TWO;
        }
    }

    private InputManager() {

        levelListeners = new ArrayList<>();

        for (@SuppressWarnings("unused") boolean b : button) {
            b = false;
        }

    }

    public void characterSwitch() {
        setShouldLockCamera(true);
        button[first] = true;
        playerOneActive = !playerOneActive;
        playerSelected = (playerOneActive ? PlayerSelection.ONE : PlayerSelection.TWO);
    }

    public void reset() {

        for (@SuppressWarnings("unused") boolean b : button) {
            b = false;
        }
    }

    public void update() {
        playerSelected = (playerOneActive ? PlayerSelection.ONE : PlayerSelection.TWO);
    }

    public void callRestart() {
        for (LevelEventListener listener : levelListeners) {
            listener.onRestartLevel();
        }
        reset();
    }

    public boolean isDown(int nr) {
        return button[nr];
    }

    private String skinName = "color";

    public String toggleSkins() {
        if (skinName.equals("color")) {
            skinName = "silhouette";
            return skinName;
        } else if (skinName.equals("silhouette")) {
            skinName = "color";
            return skinName;
        }
        return skinName;
    }

    public String getSkinName() {
        return skinName;
    }

    public void keyDown(int keycode) {
        setShouldLockCamera(true);

        if (keycode == Input.Keys.A) {
            button[left] = true;
        }
        if (keycode == Input.Keys.D) {
            button[right] = true;
        }

        if (keycode == Input.Keys.W) {
            button[restart] = true;
        }
        if (keycode == Input.Keys.S) {
            button[down] = true;
        }
        if (keycode == Input.Keys.SPACE) {
            button[jump] = true;
        }
        if (keycode == Input.Keys.F) {
            button[rag] = true;
        }
        if (keycode == Input.Keys.C) {
            button[first] = true;
            playerOneActive = !playerOneActive;
        }
        if (keycode == Input.Keys.F) {
            button[action] = true;
        }

        if (keycode == Input.Keys.R) {
            callRestart();
        }
        if (keycode == Input.Keys.Q) {
            button[skinChange] = true;
            toggleSkins();
        }


    }

    public void keyUp(int keycode) {

        if (keycode == Input.Keys.A) {
            button[left] = false;
        }
        if (keycode == Input.Keys.D) {

            button[right] = false;
        }

        if (keycode == Input.Keys.W) {
            button[restart] = false;
        }
        if (keycode == Input.Keys.S) {
            button[down] = false;
        }
        if (keycode == Input.Keys.SPACE) {
            button[jump] = false;
        }

        if (keycode == Input.Keys.G) {
            button[rag] = false;
        }

        if (keycode == Input.Keys.C) {
            button[first] = false;
        }

        if (keycode == Input.Keys.F) {
            button[action] = false;
        }
        if (keycode == Input.Keys.Q) {
            button[skinChange] = false;
        }

    }

}
