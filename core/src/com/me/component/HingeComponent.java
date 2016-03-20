package com.me.component;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;

/**
 * Created by hateftadayon on 1/8/16.
 */
public class HingeComponent extends TaskComponent {

    private Joint joint;

    public HingeComponent(int finishers, Joint joint) {
        super(finishers);
        this.joint = joint;
    }

    private void openDoor(){
        if(joint != null){
            RevoluteJoint revoluteJoint = (RevoluteJoint) joint;
            revoluteJoint.enableLimit(false);
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void onNotify(TaskEvent event) {
        if(event.getEventType() == GameEventType.PullingLedge){
            openDoor();
        }
    }
}
