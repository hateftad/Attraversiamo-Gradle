package com.me.component;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.me.component.interfaces.ButtonStateObserverComponent;
import com.me.event.ButtonEvent;
import com.me.event.GameEventType;
import com.me.event.HorizontalButtonEvent;
import com.me.utils.Direction;


public class BuoyancyComponent extends BaseComponent implements ButtonStateObserverComponent {

	private ObjectMap<String, BuoyancyControllerConfig> m_controllerInfo = new ObjectMap<String, BuoyancyControllerConfig>();
    private int m_eventId;

	//pass in fluid velocity
	public BuoyancyComponent(int taskId){
        m_eventId = taskId;
	}

	public void addControllerInfo(String name, Vector2 fluidVelocity, float linearDrag, float angularDrag){
		m_controllerInfo.put(name, new BuoyancyControllerConfig(fluidVelocity, linearDrag, angularDrag));
	}

	public ObjectMap getControllerInfo(){
		return m_controllerInfo;
	}

    public BuoyancyControllerConfig getController(String name){
        return m_controllerInfo.get(name);
    }

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void restart() {
		for(BuoyancyControllerConfig config : m_controllerInfo.values()){
            config.reset();
        }
	}

    @Override
    public void onNotify(Entity entity, ButtonEvent event) {
        if(event.getEventType() == GameEventType.HorizontalButton){
            if(m_eventId == event.getEventId()){
                HorizontalButtonEvent buttonEvent = (HorizontalButtonEvent) event;
                buttonEvent.update();
                //get fluid velocity from other component
                if(buttonEvent.getDirection() == Direction.Left){
                    getController(WorldObjectComponent.WorldObject).setFluidVelocity(-3, 1);
                } else if(buttonEvent.getDirection() == Direction.Right){
                    getController(WorldObjectComponent.WorldObject).setFluidVelocity(3, 1);
                }
            }
        }
    }

    public class BuoyancyControllerConfig {
		private float m_angularDrag;
		private float m_linearDrag;
		private Vector2 m_fluidVelocity;

		public BuoyancyControllerConfig(Vector2 fluidVelocity, float linearDrag, float angularDrag){
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

        public void reset(){
            setFluidVelocity(0, 1);
        }
	}
}
