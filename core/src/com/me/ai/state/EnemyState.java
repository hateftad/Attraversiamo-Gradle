package com.me.ai.state;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.me.ai.utils.BehaviourFactory;
import com.me.component.AIComponent;
import com.me.physics.Box2dLocation;

/**
 * Created by hateftadayon on 10/1/16.
 */
public enum EnemyState implements State<AIComponent> {

    SEEK() {
        @Override
        public void enter(AIComponent entity) {
            System.out.println("Seek : Enter");
            entity.setSteeringBehavior(BehaviourFactory.createWander(entity.getSteeringEntity()));
        }

        @Override
        public void update(AIComponent entity) {
            if(entity.isEnemySeen()){
                entity.getStateMachine().changeState(EnemyState.ATTACK);
            }
//            System.out.println("Seek : update");
        }

        @Override
        public void exit(AIComponent entity) {
            System.out.println("Seek : exit");
        }

        @Override
        public boolean onMessage(AIComponent entity, Telegram telegram) {
            return false;
        }
    },
    ATTACK(){
        @Override
        public void enter(AIComponent entity) {
            Box2dLocation target = entity.getTarget();
            System.out.println("ATTACK : Enter" + target);
            entity.setSteeringBehavior(BehaviourFactory.createSeek(entity.getSteeringEntity(), entity.getTarget()));
        }

        @Override
        public void update(AIComponent entity) {
            //System.out.println("ATTACK : update");
            if (!entity.isEnemySeen()){
                entity.getStateMachine().changeState(EnemyState.SEEK);
            }
        }

        @Override
        public void exit(AIComponent entity) {
            System.out.println("ATTACK : exit");
            entity.setSteeringBehavior(null);
        }

        @Override
        public boolean onMessage(AIComponent entity, Telegram telegram) {
            return false;
        }
    }

}
