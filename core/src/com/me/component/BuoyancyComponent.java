package com.me.component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.me.component.interfaces.ButtonStateObserverComponent;
import com.me.events.ButtonEvent;
import com.me.events.GameEventType;
import com.me.events.HorizontalButtonEvent;
import com.me.events.TaskEvent;
import com.me.utils.Direction;


public class BuoyancyComponent extends BaseComponent implements ButtonStateObserverComponent {

	private ObjectMap<String, BuoyancyControllerConfig> controllerInfo = new ObjectMap<>();
    private int eventId;

	//pass in fluid velocity
	public BuoyancyComponent(int taskId){
        eventId = taskId;
	}

	public void addControllerInfo(String name, Vector2 fluidVelocity, float linearDrag, float angularDrag){
		controllerInfo.put(name, new BuoyancyControllerConfig(fluidVelocity, linearDrag, angularDrag));
	}

	public ObjectMap getControllerInfo(){
		return controllerInfo;
	}

    public BuoyancyControllerConfig getController(String name){
        return controllerInfo.get(name);
    }

	@Override
	public void dispose() {

	}

	@Override
	public void restart() {
		for(BuoyancyControllerConfig config : controllerInfo.values()){
            config.reset();
        }
	}

    @Override
    public void onNotify(ButtonEvent event) {
        if(event.getEventType() == GameEventType.HorizontalButton){
            if(eventId == event.getEventId()){
                HorizontalButtonEvent buttonEvent = (HorizontalButtonEvent) event;
                buttonEvent.update();
                //get fluid velocity from other component
                Vector2 fluidVelocity = getController(WorldObjectComponent.WorldObject).getFluidVelocity();
                if(buttonEvent.getDirection() == Direction.Left){
                    getController(WorldObjectComponent.WorldObject).setFluidVelocity(-3, fluidVelocity.y);
                } else if(buttonEvent.getDirection() == Direction.Right){
                    getController(WorldObjectComponent.WorldObject).setFluidVelocity(3, fluidVelocity.y);
                }
            }
        }
    }

    @Override
    public void onNotify(TaskEvent event) {

    }

    public class BuoyancyControllerConfig {
		private float angularDrag;
		private float linearDrag;
		private Vector2 fluidVelocity;

		public BuoyancyControllerConfig(Vector2 fluidVelocity, float linearDrag, float angularDrag){
			this.angularDrag = angularDrag;
			this.linearDrag = linearDrag;
			this.fluidVelocity = fluidVelocity;
		}

		public float getAngularDrag() {
			return angularDrag;
		}

		public float getLinearDrag() {
			return linearDrag;
		}

		public Vector2 getFluidVelocity() {
			return fluidVelocity;
		}

        public void setFluidVelocity(float x, float y){
            fluidVelocity.set(x, y);
        }

        public void reset(){
            //setFluidVelocity(0, 1);
        }
	}
}
