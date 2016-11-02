package com.me.component;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Jump;
import com.badlogic.gdx.math.Vector2;
import com.me.ai.state.StateMachineState;
import com.me.ai.utils.BehaviourFactory;
import com.me.events.states.PlayerState;
import com.me.physics.Box2dLocation;

public class AIComponent extends BaseComponent implements Telegraph {

    private SteeringEntity steeringEntity;
    private Box2dLocation target;
    private StateMachine<AIComponent, StateMachineState> stateMachine;
    private float elapsedTime;
    private boolean shouldJump;
    private float maxVerticalVel;
    private Jump.JumpDescriptor jumpDescriptor;
    private PlayerState state;

    public AIComponent(SteeringEntity steeringEntity) {
        this.stateMachine = new DefaultStateMachine<>(this, StateMachineState.SEEK);
        this.stateMachine.setInitialState(StateMachineState.SEEK);
        this.steeringEntity = steeringEntity;
        this.steeringEntity.setSteeringBehavior(BehaviourFactory.createWander(steeringEntity));
    }

    public void update(float delta) {
        elapsedTime += delta;
        if (elapsedTime > 0.8f) {
            stateMachine.update();
            elapsedTime = 0;
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void restart() {
        target = null;
        stateMachine.changeState(StateMachineState.SEEK);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return stateMachine.handleMessage(msg);
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
        return stateMachine;
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

    private Jump.JumpCallback jumpCallback = new Jump.JumpCallback() {

        @Override
        public void reportAchievability(boolean achievable) {
            System.out.println("Jump Achievability = " + achievable);
        }

        @Override
        public void takeoff(float maxVerticalVelocity, float time) {
            System.out.println("Take off!!! "+maxVerticalVelocity);
            setMaxVerticalVel(maxVerticalVelocity);
        }

    };

    public Jump.JumpCallback getJumpCallback() {
        return jumpCallback;
    }

    public boolean shouldJump() {
        return shouldJump;
    }

    public void setShouldJump(boolean shouldJump) {
        this.shouldJump = shouldJump;
    }

    public float getMaxVerticalVel() {
        return maxVerticalVel;
    }

    private void setMaxVerticalVel(float maxVerticalVel) {
        this.maxVerticalVel = maxVerticalVel;
    }

    public Jump.JumpDescriptor getJumpDescriptor() {
        return jumpDescriptor;
    }

    public void setJumpDescriptor(Jump.JumpDescriptor<Vector2> jumpDescriptor) {
        this.jumpDescriptor = jumpDescriptor;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public PlayerState getState(){
        return state;
    }
}
