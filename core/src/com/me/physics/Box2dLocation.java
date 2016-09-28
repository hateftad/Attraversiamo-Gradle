package com.me.physics;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.me.utils.SteeringUtils;

/**
 * Created by hateftadayon on 9/28/16.
 */
public class Box2dLocation implements Location<Vector2> {

    private Vector2 position;
    private float orientation;

    public Box2dLocation(Vector2 position, float angle) {
        this.position = position;
        this.orientation = angle;
    }

    public Box2dLocation() {
        this.position = new Vector2();
        this.orientation = 0;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position){
        this.position = position;
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Box2dLocation();
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return SteeringUtils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return SteeringUtils.angleToVector(outVector, angle);
    }

}

