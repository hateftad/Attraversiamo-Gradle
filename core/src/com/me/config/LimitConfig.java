package com.me.config;

/**
 * Created by hateftadayon on 10/15/16.
 */
public class LimitConfig {

    private final float walkLimit;
    private final float jumpLimit;
    private final float ladderLimit;
    private final float pushLimit;
    private float maxLinearSpeed;
    private float crawLimit;
    private float maxLinearAcceleration;
    private float maxSpeed;
    public LimitConfig(float walkLimit, float jumpLimit, float ladderLimit, float pushLimit, float crawLimit, float maxLinearAcceleration, float maxSpeed, float maxLinearSpeed) {

        this.walkLimit = walkLimit;
        this.jumpLimit = jumpLimit;
        this.ladderLimit = ladderLimit;
        this.pushLimit = pushLimit;
        this.crawLimit = crawLimit;
        this.maxLinearAcceleration = maxLinearAcceleration;
        this.maxSpeed = maxSpeed;
        this.maxLinearSpeed = maxLinearSpeed;
    }

    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getWalkLimit() {
        return walkLimit;
    }

    public float getJumpLimit() {
        return jumpLimit;
    }

    public float getLadderLimit() {
        return ladderLimit;
    }

    public float getPushLimit() {
        return pushLimit;
    }
}
