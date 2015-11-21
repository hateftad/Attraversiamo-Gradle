package com.me.component;

import com.me.component.interfaces.TaskEventObserverComponent;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;

public class PlayerComponent extends BaseComponent implements TaskEventObserverComponent {


    public enum State {
        WALKING, IDLE, JUMPING, DYING, JUMPED, HANGING, CRAWLING, LYINGDOWN, GETTINGUP, WAITTILDONE
    }

    public enum PlayerNumber {
        ONE, TWO, THREE
    }

    private PlayerNumber m_playerNr;

    private State m_state = State.IDLE;

    private boolean m_facingLeft;

    private boolean m_active;

    private boolean m_onGround;

    private boolean m_canDeactivate;

    private boolean m_isFinishing;

    private boolean m_finishFacingLeft;

    private boolean m_isFinishedAnimating;

    public PlayerComponent(String player, boolean finishFacingLeft) {
        m_finishFacingLeft = finishFacingLeft;
        setPlayerNr(player);
        setFacingLeft(true);
        setState(State.IDLE);
    }

    public State getState() {
        return m_state;
    }

    public void setState(State m_state) {
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

    public void setPlayerNr(String playerNr) {
        if (playerNr.equalsIgnoreCase("player_one")) {
            m_playerNr = PlayerNumber.ONE;
        } else if (playerNr.equalsIgnoreCase("player_two")) {
            m_playerNr = PlayerNumber.TWO;
        }
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

    public void setIsFinishedAnimating(boolean isFinished) {
        m_isFinishedAnimating = isFinished;
    }

    @Override
    public void restart() {
        m_facingLeft = false;
        m_isFinishedAnimating = false;
    }
}
