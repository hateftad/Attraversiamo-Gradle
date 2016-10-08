package com.me.component;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;
import com.me.ai.state.EnemyState;
import com.me.ai.utils.BehaviourFactory;
import com.me.physics.Box2dLocation;

public class AIComponent extends BaseComponent implements Telegraph {

    private SteeringEntity steeringEntity;
    private Box2dLocation target;
    private StateMachine<AIComponent, EnemyState> stateMachine;
    private float elapsedTime;

    public AIComponent(SteeringEntity steeringEntity){
        stateMachine = new DefaultStateMachine<>(this, EnemyState.SEEK);
        stateMachine.setInitialState(EnemyState.SEEK);
        this.steeringEntity = steeringEntity;
        steeringEntity.setSteeringBehavior(BehaviourFactory.createWander(steeringEntity));
    }

    public void update(float delta){
        elapsedTime += delta;
        if (elapsedTime > 0.8f) {
            stateMachine.update();
            elapsedTime = 0;
        }
    }

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
        target = null;
        stateMachine.changeState(EnemyState.SEEK);
	}

    @Override
    public boolean handleMessage(Telegram msg) {
        return stateMachine.handleMessage(msg);
    }

    public SteeringBehavior getSteeringBehavior(){
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

    public StateMachine getStateMachine(){
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
}
