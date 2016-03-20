package com.me.component;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.me.component.interfaces.TaskEventObserverComponent;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;
import com.me.physics.RBUserData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hateftadayon on 06/10/15.
 */
public class FeetComponent extends BaseComponent {

    private static final int RAY_LENGTH = 1;
    private RaycastListener raycastCallback;
    private boolean collided;
    private RaySet rays;
    private String name;
    private Vector2 normal = new Vector2(0, 1);

    public FeetComponent(String name) {
        raycastCallback = new RaycastListener();
        this.name = name;
        rays = new RaySet();
    }

    public String getBodyName(){
        return name;
    }

    public RaycastListener getRaycastCallback(){
        return raycastCallback;
    }

    public List<Vector2> getStartPoints(){
        return rays.startPoints;
    }

    public List<Vector2> getEndPoints(){
        return rays.endPoints;
    }

    public Vector2 getNormal(){
        return normal;
    }

    public void reset(){
        collided = false;
    }

    public boolean hasCollided(){
        return collided;
    }

    public void update(Body body){
        rays.updatePoints(body.getPosition());
    }

    class RaycastListener implements RayCastCallback {

        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            Body bodyA = fixture.getBody();
            Entity entity = (Entity) bodyA.getUserData();
            PhysicsComponent component = entity.getComponent(PhysicsComponent.class);
            RBUserData other = component.getRBUserData(fixture.getBody());
            if(other.getType() == RBUserData.Type.Ground || other.getType() == RBUserData.Type.Box || other.getType() == RBUserData.Type.CageHatch){
                collided = true;
            }
            normal = normal;
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
            startPoints = new ArrayList<>();
            startPoints.add(Vector2.Zero);
            startPoints.add(Vector2.Zero);
            startPoints.add(Vector2.Zero);

            endPoints = new ArrayList<>();
            endPoints.add(new Vector2(0, 0));
            endPoints.add(new Vector2(0, 0));
            endPoints.add(new Vector2(0, 0));
        }

        private void updatePoints(Vector2 bodyPosition){
            Vector2 startCpy = bodyPosition.cpy();
            Vector2 left = startPoints.get(0).set(startCpy.x, startCpy.y);
            Vector2 middle = startPoints.get(1).set(startCpy.x, startCpy.y);
            Vector2 right = startPoints.get(2).set(startCpy.x, startCpy.y);

            endPoints.get(0).set(left.x - 0.4f, left.y - RAY_LENGTH + 0.1f);
            endPoints.get(1).set(middle.x, middle.y - RAY_LENGTH);
            endPoints.get(2).set(right.x + 0.4f, right.y - RAY_LENGTH + 0.1f);
        }
    }

}
