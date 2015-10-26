package com.me.component;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.me.component.interfaces.ButtonStateObserverComponent;
import com.me.event.ButtonEvent;
import com.me.event.GameEventType;
import com.me.event.HorizontalButtonEvent;
import com.me.utils.Direction;

/**
 * Created by hateftadayon on 10/24/15.
 */
public class TwoWayEngineComponent extends BaseComponent implements ButtonStateObserverComponent {

    private RevoluteJoint m_wheelJoint;
    private int m_eventId;
    private static final int SPEED = 3;

    public TwoWayEngineComponent(int taskId, Joint wheelJoint){
        m_eventId = taskId;
        m_wheelJoint = (RevoluteJoint) wheelJoint;
        m_wheelJoint.setMaxMotorTorque(3000);
    }

    @Override
    public void onNotify(Entity entity, ButtonEvent event) {
        if(event.getEventType() == GameEventType.HorizontalButton) {
            if (m_eventId == event.getEventId()) {
                HorizontalButtonEvent buttonEvent = (HorizontalButtonEvent) event;
                if(buttonEvent.getDirection() == Direction.Left){
                    setVelocity(SPEED);
                } else if(buttonEvent.getDirection() == Direction.Right){
                    setVelocity(-SPEED);
                }
            }
        }
        System.out.println(m_wheelJoint.getMotorSpeed());
    }

    public void setVelocity(float velocity){
        m_wheelJoint.setMotorSpeed(velocity);
    }

    public void standStill(){
        m_wheelJoint.enableMotor(true);
        m_wheelJoint.setMotorSpeed(0);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }
}
