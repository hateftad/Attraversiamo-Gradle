package com.me.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.me.listeners.LevelEventListener;

public class InputManager {

    public static int left = 0, right = 1, restart = 2, down = 3, jump = 4, rag = 5, first = 6, action = 7, skinChange = 8;

    public static final int PlayerOne = 1, PlayerTwo = 2;
    private int selectedIndex;

    public boolean shouldLockCamera() {
        return shouldLockCamera;
    }

    public void setShouldLockCamera(boolean shouldLockCamera) {
        this.shouldLockCamera = shouldLockCamera;
    }

    public class PlayerSelection {
        public PlayerSelection(int id, boolean active) {
            this.id = id;
            this.active = active;
        }

        private final int id;
        private boolean active;

        public int getId() {
            return id;
        }

        public boolean isActive() {
            return active;
        }
    }

    private ArrayList<LevelEventListener> levelListeners;
    private boolean shouldLockCamera = true;

    private boolean[] button = new boolean[10];
    private Array<PlayerSelection> playerSelections = new Array<>();

    private static InputManager instance = null;

    public static InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
            instance.initPlayerSelections();
        }
        return instance;
    }

    private void initPlayerSelections() {
        playerSelections = new Array<>();
        playerSelections.add(new PlayerSelection(PlayerOne, false));
        playerSelections.add(new PlayerSelection(PlayerTwo, false));
    }

    public void addEventListener(LevelEventListener listener) {
        levelListeners.add(listener);
    }

    public void setSelectedPlayer(int nr, boolean active) {
        playerSelections.get(nr - 1).active = active;
        selectedIndex = nr - 1;
    }

    private InputManager() {
        levelListeners = new ArrayList<>();
        reset();
    }

    public void characterSwitch() {
        setShouldLockCamera(true);
        button[first] = true;
        nextCharacter();
        reset();
    }

    private void nextCharacter() {
        if (selectedIndex + 1 > playerSelections.size) {
            playerSelections.get(selectedIndex - 1).active = false;
            selectedIndex = 1;
            playerSelections.get(selectedIndex - 1).active = true;
        } else {
            playerSelections.get(selectedIndex - 1).active = false;
            selectedIndex += 1;
            playerSelections.get(selectedIndex - 1).active = true;
        }

    }

    public int getSelected() {
        for (PlayerSelection playerSelection : playerSelections) {
            if (playerSelection.active) {
                return playerSelection.id;
            }
        }
        return 0;
    }

    public void reset() {
        for (int i = 0; i < button.length; i++) {
            button[i] = false;
        }
    }

    public void update() {
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
            characterSwitch();
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
