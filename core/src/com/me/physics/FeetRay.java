package com.me.physics;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by hateftadayon on 9/28/16.
 */
public class FeetRay extends RaySet {

    public FeetRay(Vector2 startPoint, int rayLength) {
        super(startPoint, rayLength);
    }

    protected void init() {
        endPoints = new ArrayList<>();
        endPoints.add(new Vector2(startPoint.x - rayLength, startPoint.y));
        endPoints.add(new Vector2(startPoint.x - rayLength, startPoint.y - rayLength));
        endPoints.add(new Vector2(startPoint.x, startPoint.y - rayLength));
        endPoints.add(new Vector2(startPoint.x + rayLength, startPoint.y - rayLength));
        endPoints.add(new Vector2(startPoint.x + rayLength, startPoint.y));
    }

    public void updatePoints() {
        endPoints.get(0).set(startPoint.x - rayLength, startPoint.y);
        endPoints.get(1).set(startPoint.x - rayLength, startPoint.y - rayLength);
        endPoints.get(2).set(startPoint.x, startPoint.y - rayLength);
        endPoints.get(3).set(startPoint.x + rayLength, startPoint.y - rayLength);
        endPoints.get(4).set(startPoint.x + rayLength, startPoint.y);
    }
}