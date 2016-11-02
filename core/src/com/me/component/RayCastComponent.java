package com.me.component;

import com.badlogic.gdx.math.Vector2;
import com.me.physics.*;

import java.util.List;

/**
 * Created by hateftadayon on 06/10/15.
 */
public class RayCastComponent extends BaseComponent {

    private RayCastListener raycastCallback;
    private RaySet rays;
    private Vector2 normal = new Vector2(0, 1);

    public RayCastComponent(RaySet raySet, RayCastListener rayCastCallback) {
        this.raycastCallback = rayCastCallback;
        this.rays = raySet;
    }

    public RayCastListener getRaycastCallback() {
        return raycastCallback;
    }

    public Vector2 getStartPoint() {
        return rays.getStartPoint();
    }

    public List<Vector2> getEndPoints() {
        return rays.getEndPoints();
    }

    public Vector2 getNormal() {
        return normal;
    }

    public void reset() {
        raycastCallback.reset();
    }

    public boolean hasCollided() {
        return raycastCallback.hasCollided();
    }

    public void update() {
        rays.updatePoints();
    }

    public Box2dLocation getTarget() {
        return raycastCallback.getTarget();
    }

    public long getCollisionTime(){
        return raycastCallback.getCollisionTime();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }


    public void clearTarget() {
        raycastCallback.clearTarget();
    }
}