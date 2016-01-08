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

    private boolean m_opened;
    private Joint m_joint;
    private int m_taskId;

    public DoorComponent(int finishers, int taskId){
        super(finishers);
        m_taskId = taskId;
    }

    public boolean isOpened() {
        return m_opened;
    }

    public void setPrismJoint(Joint joint){
        m_joint = joint;
    }

    public Joint getPrismJoint(){
        return m_joint;
    }

    private void openDoor(){
        if(m_joint != null){
            PrismaticJoint prismaticJoint = (PrismaticJoint) m_joint;
            prismaticJoint.enableMotor(true);
        }
    }

    @Override
    public void onNotify(TaskEvent event) {
        if(event.getEventType().equals(GameEventType.Door)){
            if(event.getEventId() == m_taskId) {
                if (!m_finishers.containsKey(event.getPlayerNr())) {
                    m_finishers.put(event.getPlayerNr(), true);
                }
                if (allFinished()) {
                    openDoor();
                }
            }
        }
    }

    @Override
    public void dispose() {
        if(m_joint != null){
            JointFactory.getInstance().destroyJoint(m_joint);
            m_opened = true;
        }
    }

    @Override
    public void restart() {

    }
}
