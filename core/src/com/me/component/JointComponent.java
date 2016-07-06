package com.me.component;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.me.physics.JointFactory;
import com.me.utils.Converters;

public class JointComponent extends BaseComponent{

	private Joint joint;
	
	private JointDef jointDef;

	public boolean created;

	public boolean destroyed;
	
	public JointComponent(){

	}

	public void setPrismJoint(Joint joint){
		this.joint = joint;
	}

	public Joint getJoint(){
		return joint;
	}
	
	public JointDef getJointDef(){
		return jointDef;
	}
	
	public void createEdgeHang(Body b1, Body b2){
		jointDef = JointFactory.getInstance().createWeldJoint(
                b2, b1,
                Converters.ToBox(b2.getPosition()),
                Converters.ToBox(b2.getPosition()),
                true);
		created = true;
		destroyed = false;
	}

    public void createHangJoint(){
        if(getJoint() == null) {
            setPrismJoint(JointFactory.getInstance().createJoint(getJointDef()));
        }
    }

    public void destroyHangJoint(){
        if(getJoint() != null) {
            JointFactory.getInstance().destroyJoint(getJoint());
            joint = null;
        }
    }

	public boolean hasMotor(){
		return joint != null;
	}

	@Override
	public void dispose() {
			
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		if(joint != null){
			JointFactory.getInstance().destroyJoint(joint);
            joint = null;
		}
	}

    public boolean isHanging() {
        return joint != null;
    }
}
