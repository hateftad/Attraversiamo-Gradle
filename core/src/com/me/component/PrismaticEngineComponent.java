package com.me.component;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.me.component.interfaces.ButtonStateObserverComponent;
import com.me.events.*;
import com.me.utils.Direction;

/**
 * Created by hateftadayon on 25/01/16.
 */
public class PrismaticEngineComponent extends BaseComponent implements ButtonStateObserverComponent {

    private PrismaticJoint wheelJoint;
    private int eventId;
    private static final int SPEED = 3;

    public PrismaticEngineComponent(int taskId, Joint wheelJoint){
        eventId = taskId;
        this.wheelJoint = (PrismaticJoint) wheelJoint;
    }

    @Override
    public void onNotify(ButtonEvent event) {
        if(event.getEventType() == GameEventType.VerticalButton) {
            if (eventId == event.getEventId()) {
                VerticalButtonEvent buttonEvent = (VerticalButtonEvent) event;
                buttonEvent.update();
                if(buttonEvent.getDirection() == Direction.Up){
                    setVelocity(SPEED);
                } else if(buttonEvent.getDirection() == Direction.Down){
                    setVelocity(-SPEED);
                }
            }
        }
    }

    public void setVelocity(float velocity){
        wheelJoint.setMotorSpeed(velocity);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {
        wheelJoint.setMotorSpeed(0);
    }

    @Override
    public void onNotify(TaskEvent event) {

    }
}
