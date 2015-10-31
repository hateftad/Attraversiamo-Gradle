package com.me.component;

import com.artemis.Entity;
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
            PrismaticJoint j = (PrismaticJoint) m_joint;
            j.enableMotor(true);
        }
    }

    @Override
    public void onNotify(Entity entity, TaskEvent event) {
        if(event.getEventType().equals(GameEventType.Door)){
            if(event.getEventId() == m_taskId) {
                PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
                if (!m_finishers.contains(playerComponent.getPlayerNr(), false)) {
                    m_finishers.add(playerComponent.getPlayerNr());
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
