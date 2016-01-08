package com.me.component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.me.physics.JointFactory;

public class JointComponent extends BaseComponent{

	private Joint m_pJoint;
	
	private Joint m_wJoint;
	
	private Joint m_dJoint;
	
	private JointDef m_pJointDef;
	
	private JointDef m_wJointDef;
	
	public boolean m_created;
	
	public boolean m_destroyed;
	
	private boolean setToDestroy;

	private String name;
	
	public JointComponent(String name){
		this.name = name;
	}

	public void setPrismJoint(Joint joint){
		m_pJoint = joint;
	}

	public Joint getPrismJoint(){
		return m_pJoint;
	}
	
	public void setWeldJoint(Joint joint){
		m_wJoint = joint;
	}
	
	public Joint getWeldJoint(){
		return m_wJoint;
	}
	
	public void setWJoint(JointDef jDef){
		m_wJointDef = jDef;
	}

	public JointDef getWJointDef(){
		return m_wJointDef;
	}
	
	public void setPJointDef(JointDef jDef){
		m_pJointDef = jDef;
	}

	public JointDef getPJointDef(){
		return m_pJointDef;
	}
	
	public Joint getDJoint() {
		return m_dJoint;
	}

	public void setDJoint(Joint m_dJoint) {
		this.m_dJoint = m_dJoint;
	}
	
	public void createEdgeHang(Body b1, Body b2){
		m_pJointDef = JointFactory.getInstance().createWeldJoint(
				b1, b2, 
				Vector2.Zero, 
				Vector2.Zero,
				true);
		m_created = true;
		m_destroyed = false;
	}
	
	public void createLadderHang(Body b1, Body b2, float uL){
		m_pJointDef = JointFactory.getInstance().createPrismJoint(
				b1, b2, 
				Vector2.Zero, 
				Vector2.Zero,
				uL,-20,true, false, new Vector2(0,1));
		m_created = true;
		m_destroyed = false;
	}
	
	public void createHandHang(Body b1, Body b2, boolean left){
		m_wJointDef = JointFactory.getInstance().createPrismJoint(b1, b2, Vector2.Zero, 
				new Vector2((left ? -1.2f :1.2f),4.3f),3,0, true, false, new Vector2(0,1));
		m_created = true;
		m_destroyed = false;
	}

    public void createHangJoint(){
        if(getPrismJoint() == null) {
            setPrismJoint(JointFactory.getInstance().createJoint(getPJointDef()));
        }
    }

    public void destroyHangJoint(){
        if(getPrismJoint() != null) {
            JointFactory.getInstance().destroyJoint(getPrismJoint());
            m_pJoint = null;
        }
    }

	public boolean isFullLength(){
		if(m_pJoint!=null){
			if(m_pJoint.getType() == JointType.PrismaticJoint){
				PrismaticJoint j = (PrismaticJoint) m_pJoint;
				return j.getJointTranslation() >= j.getUpperLimit();
			}
		}
		return false;
	}

	public boolean hasMotor(){
		return m_pJoint != null;
	}

	public boolean shouldDestroy(){
		return setToDestroy;
	}

	@Override
	public void dispose() {
			
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		if(m_pJoint != null){
			JointFactory.getInstance().destroyJoint(m_pJoint);
		}
		if(m_wJoint != null){
			JointFactory.getInstance().destroyJoint(m_wJoint);
		}
		if(m_dJoint != null){
			JointFactory.getInstance().destroyJoint(m_dJoint);
		}
	}

    public boolean isHanging() {
        return m_pJoint != null;
    }
}
