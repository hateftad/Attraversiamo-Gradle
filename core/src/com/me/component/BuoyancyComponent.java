package com.me.component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.HashMap;

public class BuoyancyComponent extends BaseComponent {

	private ObjectMap<String, BuoyancyControllerInfo> controllerInfo = new ObjectMap<String, BuoyancyControllerInfo>();

	public BuoyancyComponent(){
	}

	public void addControllerInfo(String name, Vector2 fluidVelocity, float linearDrag, float angularDrag){
		controllerInfo.put(name, new BuoyancyControllerInfo(fluidVelocity, linearDrag, angularDrag));
	}

	public ObjectMap getControllerInfo(){
		return controllerInfo;
	}

    public BuoyancyControllerInfo getController(String name){
        return controllerInfo.get(name);
    }

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub

	}

	public class BuoyancyControllerInfo{
		private float m_angularDrag;
		private float m_linearDrag;
		private Vector2 m_fluidVelocity;

		public BuoyancyControllerInfo(Vector2 fluidVelocity, float linearDrag, float angularDrag){
			m_angularDrag = angularDrag;
			m_linearDrag = linearDrag;
			m_fluidVelocity = fluidVelocity;
		}

		public float getAngularDrag() {
			return m_angularDrag;
		}

		public float getLinearDrag() {
			return m_linearDrag;
		}

		public Vector2 getFluidVelocity() {
			return m_fluidVelocity;
		}

        public void setFluidVelocity(float x, float y){
            m_fluidVelocity.set(x, y);
        }
	}
}
