package com.me.ai.state;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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
            Wander<Vector2> wander = new Wander<Vector2>(entity.getSteeringEntity()) //
                    .setFaceEnabled(true) // We want to use Face internally (independent facing is on)
                    .setAlignTolerance(12) // Used by Face
                    .setDecelerationRadius(12) // Used by Face
                    .setTimeToTarget(0.1f) // Used by Face
                    .setWanderOffset(30) //
                    .setWanderOrientation(30) //
                    .setWanderRadius(100) //
                    .setWanderRate(MathUtils.PI2 * 4);
            entity.setSteeringBehavior(wander);
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
            Seek<Vector2> seek = new Seek<Vector2>(entity.getSteeringEntity(), entity.getTarget());
            entity.setSteeringBehavior(seek);
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
