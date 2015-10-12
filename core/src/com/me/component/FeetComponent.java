package com.me.component;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.me.physics.RBUserData;

/**
 * Created by hateftadayon on 06/10/15.
 */
public class FeetComponent extends BaseComponent {

    private static final int RAY_LENGTH = 1;
    private RaycastListener m_raycastCallback;
    private boolean m_collided;
    private Vector2 m_pointOne;
    private String m_name;

    public FeetComponent(String name) {
        m_raycastCallback = new RaycastListener();
        m_name = name;
    }

    public String getBodyName(){
        return m_name;
    }

    public RaycastListener getRaycastCallback(){
        return m_raycastCallback;
    }

    public Vector2 getPointOne(){
        return m_pointOne;
    }

    public Vector2 getPointTwo(){
        Vector2 endPoint = m_pointOne.cpy();
        endPoint.set(endPoint.x, endPoint.y - RAY_LENGTH);
        return endPoint;
    }

    public void reset(){
        m_collided = false;
    }

    public boolean hasCollided(){
        return m_collided;
    }

    public void update(Body body){
        m_pointOne = body.getPosition().cpy();
    }

    class RaycastListener implements RayCastCallback {

        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            Body bodyA = fixture.getBody();
            Entity entity = (Entity) bodyA.getUserData();
            PhysicsComponent component = entity.getComponent(PhysicsComponent.class);
            RBUserData other = component.getRBUserData(fixture.getBody());
            if(other.getType() == RBUserData.Type.Ground || other.getType() == RBUserData.Type.Box){
                m_collided = true;
            }

            return 1;
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }

}
