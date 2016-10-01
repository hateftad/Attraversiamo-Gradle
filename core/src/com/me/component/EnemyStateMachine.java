package com.me.component;

import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.me.ai.state.EnemyState;

/**
 * Created by hateftadayon on 9/28/16.
 */
public class EnemyStateMachine {

    private StateMachine<AIComponent, EnemyState> stateMachine;

    public EnemyStateMachine(StateMachine stateMachine){
        this.stateMachine = stateMachine;
    }

    public boolean handleMessage(Telegram msg) {
        return stateMachine.handleMessage(msg);
    }

    public void update (float delta) {
        stateMachine.update();
    }

    public StateMachine getStateMachine(){
        return stateMachine;
    }
}
