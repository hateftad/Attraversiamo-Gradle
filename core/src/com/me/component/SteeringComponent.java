package com.me.component;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.me.physics.Box2dLocation;
import com.me.utils.SteeringUtils;

/**
 * Created by hateftadayon on 9/28/16.
 */
public class SteeringComponent extends BaseComponent implements Steerable<Vector2> {


    private static final SteeringAcceleration<Vector2> steeringOutput =
            new SteeringAcceleration<Vector2>(new Vector2());

    private boolean tagged;
    private final float boundRadius;
    private SteeringBehavior<Vector2> steeringBehavior;
    private Vector2 position;
    private float orientation;
    private Vector2 linearVelocity = new Vector2();
    private float angularVelocity;
    private float maxSpeed;
    private boolean independentFacing;
    private float maxLinearSpeed;
    private float maxLinearAcceleration;
    private float maxAngularSpeed;
    private float maxAngularAcceleration;

    public SteeringComponent(Vector2 position, float boundRadius){
        this.position = position;
        this.boundRadius = boundRadius;
        maxLinearAcceleration = 2;
        maxSpeed = 2;
        maxLinearSpeed = 2;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }

    public void update (float deltaTime) {
        if (steeringBehavior != null) {
            // Calculate steering acceleration
            steeringBehavior.calculateSteering(steeringOutput);

			/*
			 * Here you might want to add a motor control layer filtering steering accelerations.
			 *
			 * For instance, a car in a driving game has physical constraints on its movement: it cannot turn while stationary; the
			 * faster it moves, the slower it can turn (without going into a skid); it can brake much more quickly than it can
			 * accelerate; and it only moves in the direction it is facing (ignoring power slides).
			 */

            // Apply steering acceleration
            applySteering(deltaTime);
        }

    }

    private void applySteering(float deltaTime) {
        boolean anyAccelerations = false;

        // Update position and linear velocity.
        if (!steeringOutput.linear.isZero()) {
            // this method internally scales the force by deltaTime
            linearVelocity.set(steeringOutput.linear.x, 0);
            anyAccelerations = true;
        }

//        // Update orientation and angular velocity
//        if (isIndependentFacing()) {
//            if (steeringOutput.angular != 0) {
//                // this method internally scales the torque by deltaTime
//                body.applyTorque(steeringOutput.angular, true);
//                anyAccelerations = true;
//            }
//        } else {
//            // If we haven't got any velocity, then we can do nothing.
//            Vector2 linVel = getLinearVelocity();
//            if (!linVel.isZero(getZeroLinearSpeedThreshold())) {
//                float newOrientation = vectorToAngle(linVel);
//                body.setAngularVelocity((newOrientation - getAngularVelocity()) * deltaTime); // this is superfluous if independentFacing is always true
//                body.setTransform(body.getPosition(), newOrientation);
//            }
//        }

        if (anyAccelerations) {
            // body.activate();

            // TODO:
            // Looks like truncating speeds here after applying forces doesn't work as expected.
            // We should likely cap speeds form inside an InternalTickCallback, see
            // http://www.bulletphysics.org/mediawiki-1.5.8/index.php/Simulation_Tick_Callbacks

            // Cap the linear speed
            Vector2 velocity = getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            float maxLinearSpeed = getMaxLinearSpeed();
            if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
                linearVelocity.set(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));
            }

//            // Cap the angular speed
//            float maxAngVelocity = getMaxAngularSpeed();
//            if (body.getAngularVelocity() > maxAngVelocity) {
//                body.setAngularVelocity(maxAngVelocity);
//            }
        }
    }

    @Override
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }

    @Override
    public float getBoundingRadius() {
        return boundRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0.001f;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return 0;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return 0;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        return position;
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
    public float vectorToAngle(Vector2 vector) {

        return SteeringUtils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return SteeringUtils.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Box2dLocation();
    }

    public SteeringBehavior<Vector2> getSteeringBehavior () {
        return steeringBehavior;
    }

    public void setSteeringBehavior (SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

    public boolean isIndependentFacing() {
        return independentFacing;
    }
}
