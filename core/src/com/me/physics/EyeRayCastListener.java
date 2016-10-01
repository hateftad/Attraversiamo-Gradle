package com.me.physics;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.me.component.PhysicsComponent;

/**
 * Created by hateftadayon on 9/28/16.
 */
public class EyeRayCastListener implements RayCastListener {

    private boolean collided;
    private Box2dLocation target;
    private long collisionTime;

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        Body bodyA = fixture.getBody();
        Entity entity = (Entity) bodyA.getUserData();
        PhysicsComponent component = entity.getComponent(PhysicsComponent.class);
        RBUserData other = component.getRBUserData(fixture.getBody());
        if (other.getType() == RBUserData.Type.Torso) {
            collisionTime = System.currentTimeMillis();
            collided = true;
            if (target == null) {
                target = new Box2dLocation(bodyA.getPosition(), bodyA.getAngle());
            } else {
                target.setOrientation(bodyA.getAngle());
                target.setPosition(bodyA.getPosition());
            }
        }

        return 1;
    }

    public boolean hasCollided() {
        return collided;
    }

    @Override
    public void reset() {

    }

    public Box2dLocation getTarget(){
        return target;
    }

    @Override
    public void clearTarget() {
        target = null;
        collided = false;
    }

    public long getCollisionTime() {
        return collisionTime;
    }

    public void setCollisionTime(long collisionTime) {
        this.collisionTime = collisionTime;
    }
}
