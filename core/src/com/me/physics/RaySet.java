package com.me.physics;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Created by hateftadayon on 9/28/16.
 */
public abstract class RaySet {
    List<Vector2> endPoints;
    Vector2 startPoint;
    int rayLength;

    public RaySet(Vector2 startPoint, int rayLength) {
        this.startPoint = startPoint;
        this.rayLength = rayLength;
        init();
    }

    protected abstract void init();
    public abstract void updatePoints();

    public Vector2 getStartPoint() {
        return startPoint;
    }

    public List<Vector2> getEndPoints() {
        return endPoints;
    }
}
