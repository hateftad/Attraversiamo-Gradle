package com.me.component;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.me.component.interfaces.ButtonStateObserverComponent;
import com.me.events.ButtonEvent;
import com.me.events.GameEventType;
import com.me.events.HorizontalButtonEvent;
import com.me.events.TaskEvent;
import com.me.utils.Direction;

/**
 * Created by hateftadayon on 10/24/15.
 */
public class RevoluteEngineComponent extends BaseComponent implements ButtonStateObserverComponent {

    private RevoluteJoint wheelJoint;
    private int eventId;
    private static final int SPEED = 3;

    public RevoluteEngineComponent(int taskId, Joint wheelJoint){
        this.eventId = taskId;
        this.wheelJoint = (RevoluteJoint) wheelJoint;
        this.wheelJoint.setMaxMotorTorque(3000);
    }

    @Override
    public void onNotify(ButtonEvent event) {
        if(event.getEventType() == GameEventType.HorizontalButton) {
            if (eventId == event.getEventId()) {
                HorizontalButtonEvent buttonEvent = (HorizontalButtonEvent) event;
                if(buttonEvent.getDirection() == Direction.Left){
                    setVelocity(SPEED);
                } else if(buttonEvent.getDirection() == Direction.Right){
                    setVelocity(-SPEED);
                }
            }
        }
    }

    public void setVelocity(float velocity){
        wheelJoint.setMotorSpeed(velocity);
    }

    public void standStill(){
        wheelJoint.enableMotor(true);
        wheelJoint.setMotorSpeed(0);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {
        standStill();
    }

    @Override
    public void onNotify(TaskEvent event) {

    }
}
