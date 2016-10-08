package com.me.ai.utils;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;
import com.me.component.SteeringEntity;
import com.me.physics.Box2dLocation;

/**
 * Created by hateftadayon on 10/1/16.
 */
public class BehaviourFactory {

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
}
