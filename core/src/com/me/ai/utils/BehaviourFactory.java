package com.me.ai.utils;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Jump;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.limiters.LinearLimiter;
import com.badlogic.gdx.math.Vector2;
import com.me.component.SteeringEntity;
import com.me.physics.Box2dLocation;

/**
 * Created by hateftadayon on 10/1/16.
 */
public class BehaviourFactory {

    static final Jump.GravityComponentHandler<Vector2> GRAVITY_COMPONENT_HANDLER = new Jump.GravityComponentHandler<Vector2>() {

        @Override
        public float getComponent (Vector2 vector) {
            return vector.y;
        }

        @Override
        public void setComponent (Vector2 vector, float value) {
            vector.y = value;
        }
    };

    public static Wander createWander(SteeringEntity steeringEntity){
        return new Wander<>(steeringEntity) //
                .setWanderOffset(10) //
                .setWanderOrientation(30) //
                .setWanderRadius(10) //
                .setWanderRate(0.1f);
    }

    public static Seek createSeek(SteeringEntity steeringEntity, Box2dLocation target){
        Seek<Vector2> seek = new Seek<>(steeringEntity, target);
        return seek;
    }

    public static Arrive createArrive(SteeringEntity steeringEntity, Box2dLocation target){
        final Arrive<Vector2> arriveSB = new Arrive<Vector2>(steeringEntity, target)
                .setTimeToTarget(0.1f) //
                .setArrivalTolerance(0.001f) //
                .setDecelerationRadius(3);
        return arriveSB;
    }

    public static Jump createJump(SteeringEntity steeringEntity, Jump.JumpDescriptor jumpDescriptor, Jump.JumpCallback jumpCallback, Vector2 gravity){
        Jump<Vector2> jumpSB = new Jump<Vector2>(steeringEntity, jumpDescriptor, gravity, GRAVITY_COMPONENT_HANDLER, jumpCallback) //
                .setMaxVerticalVelocity(10)
                .setTakeoffTolerance(370)
                .setTimeToTarget(0.1f);
        jumpSB.setLimiter(new LinearLimiter(steeringEntity.getMaxLinearAcceleration(), steeringEntity.getMaxLinearSpeed() * 3));
        return jumpSB;
    }
}
