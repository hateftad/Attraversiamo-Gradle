package com.me.component;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.me.physics.RBUserData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hateftadayon on 06/10/15.
 */
public class FeetComponent extends BaseComponent {

    private static final int RAY_LENGTH = 1;
    private RaycastListener m_raycastCallback;
    private boolean m_collided;
    private RaySet m_rays;
    private String m_name;
    private Vector2 m_normal = new Vector2(0, 1);

    public FeetComponent(String name) {
        m_raycastCallback = new RaycastListener();
        m_name = name;
        m_rays = new RaySet();
    }

    public String getBodyName(){
        return m_name;
    }

    public RaycastListener getRaycastCallback(){
        return m_raycastCallback;
    }

    public List<Vector2> getStartPoints(){
        return m_rays.startPoints;
    }

    public List<Vector2> getEndPoints(){
        return m_rays.endPoints;
    }

    public Vector2 getNormal(){
        return m_normal;
    }

    public void reset(){
        m_collided = false;
    }

    public boolean hasCollided(){
        return m_collided;
    }

    public void update(Body body){
        m_rays.updatePoints(body.getPosition());
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
            m_normal = normal;
            return 1;
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }

    private class RaySet{
        private List<Vector2> startPoints;
        private List<Vector2> endPoints;
        public RaySet(){
            init();
        }

        private void init(){
            startPoints = new ArrayList<Vector2>();
            startPoints.add(new Vector2());
            startPoints.add(new Vector2());
            startPoints.add(new Vector2());

            endPoints = new ArrayList<Vector2>();
            endPoints.add(new Vector2());
            endPoints.add(new Vector2());
            endPoints.add(new Vector2());
        }

        private void updatePoints(Vector2 bodyPosition){
            Vector2 startCpy = bodyPosition.cpy();
            Vector2 left = startPoints.get(0).set(startCpy.x - 0.2f, startCpy.y);
            Vector2 middle = startPoints.get(1).set(startCpy.x, startCpy.y);
            Vector2 right = startPoints.get(2).set(startCpy.x + 0.2f, startCpy.y);


            endPoints.get(0).set(left.x, left.y - RAY_LENGTH);
            endPoints.get(1).set(middle.x, middle.y - RAY_LENGTH);
            endPoints.get(2).set(right.x, right.y - RAY_LENGTH);
        }
    }

}
