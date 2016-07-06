package com.me.component;


import com.me.ui.InputManager;

public class KeyInputComponent extends BaseComponent {

    public boolean left;
    public boolean right;
    public boolean down;
    public boolean up;
    public boolean jump;

    public void set(boolean left, boolean right, boolean up, boolean down, boolean jump) {
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.jump = jump;

    }

    public void update(InputManager manager) {
        left = manager.isDown(InputManager.left);
        right = manager.isDown(InputManager.right);
        jump = manager.isDown(InputManager.jump);
    }

    public boolean moved() {
        return (left) || (right) || (jump);
    }

    public boolean isMoving() {
        return (left || right);
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
    }

    @Override
    public void restart() {
        reset();
    }

    public void reset() {
        left = false;
        right = false;
        down = false;
        up = false;
        jump = false;
    }

}
