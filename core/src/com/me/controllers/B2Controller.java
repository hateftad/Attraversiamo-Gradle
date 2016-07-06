package com.me.controllers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class B2Controller {
    protected Array<Body> bodyList;
    public int mControllerType;
    boolean mIsActive; // defines whether or not the controller is presently active

    /*
     * Controller types currently implemented
     */
    public static final int UNKNOWN_CONTROLLER = 0;
    public static final int BUOYANCY_CONTROLLER = 1;
    public static final int GRAVITY_CONTROLLER = 2;

    public void step(float timeStep) {
    }

    // public void draw(DebugDraw debugDraw) { }

    public void addBody(Body body) {
        if (bodyList == null) {
            bodyList = new Array<>();
        }

        // if the list does not already contain this body...
        if (!bodyList.contains(body, true)) {
            bodyList.add(body);
        }
    }

    public void removeBody(Body body) {
        if (bodyList != null) {
            bodyList.removeValue(body, true);
        }
    }

    public void clear() {
        if (bodyList != null) {
            bodyList.clear();
        }
        bodyList = null;
    }

    public Array<Body> getBodyList() {
        return bodyList;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public void setIsActive(boolean isActive) {
        mIsActive = isActive;
    }

}
