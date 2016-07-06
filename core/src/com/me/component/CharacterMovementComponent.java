package com.me.component;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.me.component.interfaces.TaskEventObserverComponent;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;

/**
 * Created by hateftadayon on 7/21/15.
 */
public class CharacterMovementComponent extends BaseComponent implements TaskEventObserverComponent {

    private RevoluteJoint wheelJoint;

    public CharacterMovementComponent(Joint wheelJoint){
       this.wheelJoint = (RevoluteJoint) wheelJoint;
    }

    public void setVelocity(float velocity){
        wheelJoint.enableMotor(true);
        wheelJoint.setMotorSpeed(velocity);
    }

    public void standStill(){
        wheelJoint.enableMotor(true);
        wheelJoint.setMotorSpeed(0);
    }

    @Override
    public void onNotify(TaskEvent event) {
        if(event.getEventType() == GameEventType.AllReachedEnd){
            standStill();
        }
    }

    public float getSpeed(){
        return wheelJoint.getMotorSpeed();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void restart() {

    }
}
