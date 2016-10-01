package com.me.component;

import com.me.component.interfaces.TelegramEventObserverComponent;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;
import com.me.events.TelegramEvent;
import com.me.events.states.PlayerState;

public class PlayerComponent extends BaseComponent implements TelegramEventObserverComponent {

    private boolean finished;

    public boolean isMoving() {
        return state == PlayerState.Walking ||
                state == PlayerState.Running ||
                state == PlayerState.Jogging;
    }

    public boolean isJumping() {
        return state == PlayerState.Jumping ||
                state == PlayerState.UpJump;
    }

    public boolean isHanging() {
        return state == PlayerState.Hanging;
    }

    public boolean isLyingDown() {
        return state == PlayerState.LyingDown;
    }

    public boolean lyingDown() {
        return state == PlayerState.LieDown;
    }

    public boolean isLanding() {
        return state == PlayerState.Landing || state == PlayerState.RunLanding;
    }

    public boolean isFalling() {
        return state == PlayerState.Falling || state == PlayerState.RunFalling;
    }

    public boolean shouldBeIdle() {
        return !isJumping() &
                !isHanging() &
                !isClimbing() &
                !isLyingDown() &
                !lyingDown() &
                !isPullingUp() &
                !isPressingButton() &
                !isCrawling() &
                !isFinishing() &
                !isPullingLedge() &
                !isSwingingCage() &
                !isDrowning() &
                !isLanding();

    }

    public boolean isHoldingCage(){
        return state == PlayerState.HoldingCage;
    }

    public boolean isSwingingCage(){
        return state == PlayerState.Swinging;
    }

    public boolean isIdle(){
        return state == PlayerState.Idle;
    }

    public boolean isClimbing() {
        return state == PlayerState.ClimbingLedge || state == PlayerState.ClimbBox;
    }

    public boolean isPullingUp() {
        return state == PlayerState.PullUp;
    }

    public boolean isCrawling() {
        return crawling() || isLyingDown() || lyingDown();
    }

    public boolean crawling() {
        return state == PlayerState.Crawl;
    }

    public boolean isPressingButton() {
        return state == PlayerState.PressButton;
    }

    public boolean isGettingUp() {
        return state == PlayerState.StandUp;
    }

    public boolean isPullingLedge() {
        return state == PlayerState.PullingLedge;
    }

    public boolean isDrowning() {
        return state == PlayerState.Drowning;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isUpJumping() {
        return state == PlayerState.UpJump;
    }

    public boolean isDropping() {
        return state == PlayerState.Falling;
    }

    public boolean isRunning() {
        return state == PlayerState.Running;
    }

    public boolean isClimbingBox() {
        return state == PlayerState.ClimbBox;
    }

    public enum PlayerNumber {
        NONE,
        ONE, TWO, THREE, AI
    }

    private PlayerNumber playerNr;

    private PlayerState state = PlayerState.Idle;

    private boolean facingLeft;

    private boolean active;

    private boolean canDeactivate;

    private boolean isFinishing;

    private boolean finishFacingLeft;

    public PlayerComponent(PlayerNumber player, boolean finishFacingLeft) {
        this.finishFacingLeft = finishFacingLeft;
        this.playerNr = player;
        this.setFacingLeft(true);
        this.setState(PlayerState.Idle);
    }

    public PlayerState getState() {
        return state;
    }

    public boolean canBeControlled() {
        return state != PlayerState.LyingDown &&
                state != PlayerState.StandUp &&
                state != PlayerState.PressButton;
    }

    public boolean isSuckingIn() {
        return state == PlayerState.SuckIn;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void setCanBecomeInactive(boolean state) {
        canDeactivate = state;
    }

    public boolean canDeActivate() {
        return canDeactivate;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFinishing() {
        return isFinishing;
    }

    public PlayerNumber getPlayerNr() {
        return playerNr;
    }


    @Override
    public void dispose() {

    }

    @Override
    public void onNotify(TaskEvent event) {
        if (event.getEventType() == GameEventType.AllReachedEnd) {
            setFacingLeft(finishFacingLeft);
            isFinishing = true;
        }
    }

    @Override
    public void onNotify(TelegramEvent event) {
        if (event.getEventType() == GameEventType.HoldingHandsFollowing ||
                event.getEventType() == GameEventType.HoldingHandsLeading) {
            //setState();
        }
    }

    @Override
    public void restart() {
        facingLeft = false;
        setState(PlayerState.Idle);
    }
}
