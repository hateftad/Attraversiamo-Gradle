package com.me.component;

import com.me.component.interfaces.TelegramEventObserverComponent;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;
import com.me.events.TelegramEvent;
import com.me.events.states.PlayerState;

public class PlayerComponent extends BaseComponent implements TelegramEventObserverComponent {

    public boolean isMoving() {
        return m_state == PlayerState.Walking ||
                m_state == PlayerState.Running ||
                m_state == PlayerState.Jogging;
    }

    public boolean isJumping() {
        return m_state == PlayerState.Jumping ||
                m_state == PlayerState.UpJump;
    }

    public boolean isHanging() {
        return m_state == PlayerState.Hanging;
    }

    public boolean isLyingDown(){
        return m_state == PlayerState.LyingDown || m_state == PlayerState.LieDown;
    }

    public boolean shouldBeIdle(){
        return !isJumping() && !isHanging() && !isClimbingLedge() && !isLyingDown();
    }

    public boolean isClimbingLedge() {
        return m_state == PlayerState.ClimbingLedge;
    }

    public boolean isPullingUp() {
        return m_state == PlayerState.PullUp;
    }

    public enum PlayerNumber {
        NONE,
        ONE, TWO, THREE
    }

    private PlayerNumber m_playerNr;

    private PlayerState m_state = PlayerState.Idle;

    private boolean m_facingLeft;

    private boolean m_active;

    private boolean m_onGround;

    private boolean m_canDeactivate;

    private boolean m_isFinishing;

    private boolean m_finishFacingLeft;

    private boolean m_isFinishedAnimating;

    public PlayerComponent(PlayerNumber player, boolean finishFacingLeft) {
        m_finishFacingLeft = finishFacingLeft;
        m_playerNr = player;
        setFacingLeft(true);
        setState(PlayerState.Idle);
    }

    public PlayerState getState() {
        return m_state;
    }

    public boolean canBeControlled(){
        return m_state != PlayerState.LyingDown &&
                m_state != PlayerState.StandUp &&
                m_state != PlayerState.PressButton;
    }

    public void setState(PlayerState m_state) {
        this.m_state = m_state;
    }

    public void setCanBecomeInactive(boolean state) {
        m_canDeactivate = state;
    }

    public boolean canDeActivate() {
        return m_canDeactivate;
    }

    public boolean isFacingLeft() {
        return m_facingLeft;
    }

    public void setFacingLeft(boolean m_facingLeft) {
        this.m_facingLeft = m_facingLeft;
    }

    public boolean isActive() {
        return m_active;
    }

    public void setActive(boolean active) {
        m_active = active;
    }

    public boolean isOnGround() {
        return m_onGround;
    }

    public void setOnGround(boolean onGround) {
        this.m_onGround = onGround;
    }

    public boolean isFinishing() {
        return m_isFinishing;
    }

    public PlayerNumber getPlayerNr() {
        return m_playerNr;
    }


    @Override
    public void dispose() {

    }

    @Override
    public void onNotify(TaskEvent event) {
        if (event.getEventType() == GameEventType.AllReachedEnd) {
            setFacingLeft(m_finishFacingLeft);
            m_isFinishing = true;
        }
    }

    @Override
    public void onNotify(TelegramEvent event) {
        if (event.getEventType() == GameEventType.HoldingHandsFollowing || event.getEventType() == GameEventType.HoldingHandsLeading) {
            //setState(State.WAITTILDONE);
        }
    }

    public void setIsFinishedAnimating(boolean isFinished) {
        m_isFinishedAnimating = isFinished;
    }

    @Override
    public void restart() {
        m_facingLeft = false;
        m_isFinishedAnimating = false;
    }
}
