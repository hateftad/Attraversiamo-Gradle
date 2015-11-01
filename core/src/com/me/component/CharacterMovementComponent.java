package com.me.component;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.me.component.interfaces.GameEventObserverComponent;
import com.me.component.interfaces.TaskEventObserverComponent;
import com.me.events.GameEvent;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;

/**
 * Created by hateftadayon on 7/21/15.
 */
public class CharacterMovementComponent extends BaseComponent implements GameEventObserverComponent {

    private RevoluteJoint m_wheelJoint;

    public CharacterMovementComponent(Joint wheelJoint){
        m_wheelJoint = (RevoluteJoint) wheelJoint;
    }

    public void setVelocity(float velocity){
        m_wheelJoint.enableMotor(true);
        m_wheelJoint.setMotorSpeed(velocity);
    }

    public void standStill(){
        m_wheelJoint.enableMotor(true);
        m_wheelJoint.setMotorSpeed(0);
    }

    @Override
    public void onNotify(GameEventType type) {
        if(type == GameEventType.AllReachedEnd){
            standStill();
        }
    }

    public float getSpeed(){
        return m_wheelJoint.getMotorSpeed();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void restart() {

    }
}
