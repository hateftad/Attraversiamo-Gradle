package com.me.component;


import com.me.ui.InputManager;

public class KeyInputComponent extends BaseComponent {

    public boolean m_left;
    public boolean m_right;
    public boolean m_down;
    public boolean m_up;
    public boolean m_jump;
    public float m_percentageX;
    public float m_percentageY;

    public void set(boolean left, boolean right, boolean up, boolean down, boolean jump) {
        m_left = left;
        m_right = right;
        m_up = up;
        m_down = down;
        m_jump = jump;

    }

    public void update(InputManager manager) {
        m_left = manager.isDown(InputManager.left);
        m_right = manager.isDown(InputManager.right);
        m_jump = manager.isDown(InputManager.jump);
        m_percentageX = manager.getPercentageX();
        m_percentageY = manager.getPercentageY();

    }

    public boolean moved() {
        return (m_percentageX < 0) || (m_percentageX > 0) || (m_jump);
    }

    public boolean isMoving() {
        return ((m_percentageX < 0) || (m_percentageX > 0));
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
        m_left = false;
        m_right = false;
        m_down = false;
        m_up = false;
        m_jump = false;
    }

}
