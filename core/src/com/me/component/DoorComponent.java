package com.me.component;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;
import com.me.physics.JointFactory;

/**
 * Created by hateftadayon on 7/9/15.
 */
public class DoorComponent extends TaskComponent {

    private boolean opened;
    private Joint joint;
    private int taskId;

    public DoorComponent(int finishers, int taskId){
        super(finishers);
        this.taskId = taskId;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setPrismJoint(Joint joint){
        this.joint = joint;
    }

    public Joint getPrismJoint(){
        return joint;
    }

    private void openDoor(){
        if(joint != null){
            PrismaticJoint prismaticJoint = (PrismaticJoint) joint;
            prismaticJoint.enableMotor(true);
        }
    }

    @Override
    public void onNotify(TaskEvent event) {
        if(event.getEventType().equals(GameEventType.Door)){
            if(event.getEventId() == taskId) {
                if (!finishers.containsKey(event.getPlayerNr())) {
                    finishers.put(event.getPlayerNr(), true);
                }
                if (allFinished()) {
                    openDoor();
                }
            }
        }
    }

    @Override
    public void dispose() {
        if(joint != null){
            JointFactory.getInstance().destroyJoint(joint);
            opened = true;
        }
    }

    @Override
    public void restart() {

    }
}
