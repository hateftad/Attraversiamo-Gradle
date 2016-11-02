package com.me.ai.state;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.me.ai.utils.BehaviourFactory;
import com.me.component.AIComponent;
import com.me.physics.Box2dLocation;

/**
 * Created by hateftadayon on 10/1/16.
 */
public enum StateMachineState implements State<AIComponent> {

    WANDER() {
        @Override
        public void enter(AIComponent entity) {
            System.out.println("Seek : Enter");
            entity.setSteeringBehavior(BehaviourFactory.createWander(entity.getSteeringEntity()));
        }

        @Override
        public void update(AIComponent entity) {
            if (entity.isEnemySeen()) {
                entity.getStateMachine().changeState(StateMachineState.ATTACK);
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
    ATTACK() {
        @Override
        public void enter(AIComponent entity) {
            Box2dLocation target = entity.getTarget();
            System.out.println("ATTACK : Enter" + target);
            entity.setSteeringBehavior(BehaviourFactory.createSeek(entity.getSteeringEntity(), entity.getTarget()));
        }

        @Override
        public void update(AIComponent entity) {
            if (!entity.isEnemySeen()) {
                entity.getStateMachine().changeState(StateMachineState.WANDER);
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
    },
    JUMP(){
        @Override
        public void enter(AIComponent entity) {
            System.out.println("JUMP : Enter");
            entity.setSteeringBehavior(BehaviourFactory.createJump(entity.getSteeringEntity(),
                    entity.getJumpDescriptor(),
                    entity.getJumpCallback(),
                    new Vector2(0, -14)));
            entity.setTarget(null);
        }

        @Override
        public void update(AIComponent entity) {
            //System.out.println("ATTACK : update");
            if(entity.isEnemySeen()){
                entity.getStateMachine().changeState(StateMachineState.ATTACK);
            }
        }

        @Override
        public void exit(AIComponent entity) {
            System.out.println("JUMP : Exit");
            entity.setSteeringBehavior(null);
        }

        @Override
        public boolean onMessage(AIComponent entity, Telegram telegram) {
            return false;
        }
    },

    WAITING() {
        @Override
        public void enter(AIComponent entity) {

        }

        @Override
        public void update(AIComponent entity) {
            //System.out.println("ATTACK : update");
            if (entity.isEnemySeen()) {
                entity.getStateMachine().changeState(StateMachineState.ATTACK);
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
