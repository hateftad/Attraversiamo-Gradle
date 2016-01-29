package com.me.component;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.me.component.interfaces.ButtonStateObserverComponent;
import com.me.events.ButtonEvent;
import com.me.events.GameEventType;
import com.me.events.HorizontalButtonEvent;
import com.me.utils.Direction;

/**
 * Created by hateftadayon on 25/01/16.
 */
public class PrismaticEngineComponent extends BaseComponent implements ButtonStateObserverComponent {

    private PrismaticJoint m_wheelJoint;
    private int m_eventId;
    private static final int SPEED = 3;

    public PrismaticEngineComponent(int taskId, Joint wheelJoint){
        m_eventId = taskId;
        m_wheelJoint = (PrismaticJoint) wheelJoint;
        m_wheelJoint.setMaxMotorForce(3000);
    }

    @Override
    public void onNotify(ButtonEvent event) {
        if(event.getEventType() == GameEventType.VerticalButton) {
            if (m_eventId == event.getEventId()) {
                HorizontalButtonEvent buttonEvent = (HorizontalButtonEvent) event;
                if(buttonEvent.getDirection() == Direction.Up){
                    setVelocity(SPEED);
                } else if(buttonEvent.getDirection() == Direction.Down){
                    setVelocity(-SPEED);
                }
            }
        }
    }

    public void setVelocity(float velocity){
        m_wheelJoint.setMotorSpeed(velocity);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }
}
