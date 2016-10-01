package com.me.physics;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.me.component.PhysicsComponent;

/**
 * Created by hateftadayon on 9/28/16.
 */
public class FeetRayCastListener implements RayCastListener {

    private boolean collided;

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        Body bodyA = fixture.getBody();
        Entity entity = (Entity) bodyA.getUserData();
        PhysicsComponent component = entity.getComponent(PhysicsComponent.class);
        RBUserData other = component.getRBUserData(fixture.getBody());
        if(other.getType() == RBUserData.Type.Ground || other.getType() == RBUserData.Type.Box || other.getType() == RBUserData.Type.CageHatch){
            collided = true;
        }

        return 1;
    }

    @Override
    public Box2dLocation getTarget() {
        return null;
    }

    public boolean hasCollided(){
        return collided;
    }

    @Override
    public void reset() {
        collided = false;
    }

    @Override
    public long getCollisionTime() {
        return 0;
    }

    @Override
    public void clearTarget() {

    }
}
