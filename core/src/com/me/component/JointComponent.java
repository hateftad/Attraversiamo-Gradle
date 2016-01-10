package com.me.component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.me.physics.JointFactory;
import com.me.utils.Converters;

public class JointComponent extends BaseComponent{

	private Joint m_joint;
	
	private JointDef m_jointDef;

	public boolean m_created;

	public boolean m_destroyed;
	
	public JointComponent(){

	}

	public void setPrismJoint(Joint joint){
		m_joint = joint;
	}

	public Joint getJoint(){
		return m_joint;
	}
	
	public JointDef getJointDef(){
		return m_jointDef;
	}
	
	public void createEdgeHang(Body b1, Body b2){
		m_jointDef = JointFactory.getInstance().createWeldJoint(
                b2, b1,
                Converters.ToBox(b2.getPosition()),
                Converters.ToBox(b2.getPosition()),
                true);
		m_created = true;
		m_destroyed = false;
	}

    public void createHangJoint(){
        if(getJoint() == null) {
            setPrismJoint(JointFactory.getInstance().createJoint(getJointDef()));
        }
    }

    public void destroyHangJoint(){
        if(getJoint() != null) {
            JointFactory.getInstance().destroyJoint(getJoint());
            m_joint = null;
        }
    }

	public boolean hasMotor(){
		return m_joint != null;
	}

	@Override
	public void dispose() {
			
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		if(m_joint != null){
			JointFactory.getInstance().destroyJoint(m_joint);
            m_joint = null;
		}
	}

    public boolean isHanging() {
        return m_joint != null;
    }
}
