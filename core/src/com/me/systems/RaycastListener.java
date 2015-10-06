package com.me.systems;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

/**
 * Created by hateftadayon on 06/10/15.
 */
public class RaycastListener implements RayCastCallback {
    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        Body bodyA = fixture.getBody();
        Entity entity = (Entity) bodyA.getUserData();

        return 0;
    }
}
