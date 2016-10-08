package com.me.component;

import com.artemis.Entity;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.me.component.interfaces.TelegramEventObserverComponent;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;
import com.me.events.TelegramEvent;
import com.me.events.states.PlayerState;
import com.me.physics.Box2dLocation;

/**
 * Created by hateftadayon on 10/5/16.
 */
public class PlayerAIComponent extends BaseComponent implements TelegramEventObserverComponent {

    private SteeringEntity steeringEntity;
    private PlayerComponent.PlayerNumber playerNumber;
    private Box2dLocation target;
    private boolean activate;

    public PlayerAIComponent(SteeringEntity steeringEntity, PlayerComponent.PlayerNumber playerNumber) {
        this.steeringEntity = steeringEntity;
        this.playerNumber = playerNumber;
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void restart() {
        // TODO Auto-generated method stub
        target = null;
    }

    public SteeringBehavior getSteeringBehavior() {
        return steeringEntity.getSteeringBehavior();
    }

    public void setSteeringBehavior(SteeringBehavior steeringBehavior) {
        this.steeringEntity.setSteeringBehavior(steeringBehavior);
    }

    public SteeringEntity getSteeringEntity() {
        return steeringEntity;
    }

    public void setSteeringEntity(SteeringEntity steeringEntity) {
        this.steeringEntity = steeringEntity;
    }

    public StateMachine getStateMachine() {
        return null;//stateMachine;
    }

    public boolean isEnemySeen() {
        return target != null;
    }

    public void setTarget(Box2dLocation target) {
        this.target = target;
    }

    public Box2dLocation getTarget() {
        return target;
    }

    public boolean isBeingControlled() {
        return target != null;
    }

    public boolean shouldBeControlled(){
        return target != null && activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    @Override
    public void onNotify(TaskEvent event) {

    }

    @Override
    public void onNotify(TelegramEvent event) {
        Entity entity = event.getEntity();
        PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
        if (playerNumber != playerComponent.getPlayerNr()) {
            if (event.getEventType() == GameEventType.HoldingHandsFollowing) {
                setActivate(true);
//                setAnimationState(PlayerState.HoldHandLeading);
            } else if (event.getEventType() == GameEventType.HoldingHandsLeading) {
                setActivate(true);
//                setAnimationState(PlayerState.HoldHandFollowing);
            }
        }
    }
}
