package com.me.physics;

import com.badlogic.gdx.physics.box2d.RayCastCallback;

/**
 * Created by hateftadayon on 9/28/16.
 */
public interface RayCastListener extends RayCastCallback {

    Box2dLocation getTarget();
    boolean hasCollided();
    void reset();
    long getCollisionTime();

    void clearTarget();
}
