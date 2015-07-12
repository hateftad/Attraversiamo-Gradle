package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.graphics.Color;
import com.me.component.*;
import com.me.interfaces.GameEvent;
import com.me.interfaces.GameEventType;
import com.me.level.Level;
import com.me.listeners.LevelEventListener;
import com.me.manager.ScriptManager;

public class LevelSystem extends GameEntityProcessingSystem{

	private float inc = 0.0f;
	private LevelEventListener m_levelListener;
	private ScriptManager m_scriptMgr;
    private Level m_currentLevel;
	private boolean m_enable;

	@Mapper ComponentMapper<LightComponent> m_lightComps;
	@Mapper ComponentMapper<PlayerComponent> m_players;
	@Mapper ComponentMapper<TouchComponent> m_touch;
	@Mapper ComponentMapper<ParticleComponent> m_particles;
	@Mapper ComponentMapper<JointComponent> m_joints;
	@Mapper ComponentMapper<BuoyancyComponent> m_buoyancyComps;
	@Mapper ComponentMapper<ReachEndComponent> m_reachEndComps;
    @Mapper ComponentMapper<DirectionComponent> m_direction;
    @Mapper ComponentMapper<EventComponent> m_taskComps;


	@SuppressWarnings("unchecked")
	public LevelSystem(LevelEventListener listener) {
		super(Aspect.getAspectForAll(TriggerComponent.class));
		m_levelListener = listener;
	}

    public void setCurrentLevel(Level level){
        m_currentLevel = level;
    }

    public Level getCurrentLevel(){
        return m_currentLevel;
    }

	public void setProcessing(boolean enable){
		m_enable = enable;
	}

	@Override
	protected void process(Entity e) {

		//m_scriptMgr.update();

		if(m_lightComps.has(e)){
			updateLights(m_lightComps.get(e));
		}

        if(m_reachEndComps.has(e)) {
			checkFinished(e);
		}

		if(m_particles.has(e)){
			updateParticles(m_particles.get(e));
		}
		
	}

	@Override
	protected boolean checkProcessing() {
		return m_enable;
	}

	private void updateLights(LightComponent light){
		if(light.getName().equals("portalLight")){
			float a = light.getAlpha();

			if(!m_currentLevel.isFinished()){
				if(a >= 1){
					light.setColor(Color.RED);
					inc = -0.01f;
				}else if(a < 0.1f){
					inc = 0.01f;
				}
				light.setAlpha(a + inc);

			} else {
				light.setAlpha(1);
				light.setColor(Color.GREEN);
				world.getSystem(CameraSystem.class).getRayHandler().setAmbientLight(1f);
			}

		}
	}
	
	private void updateParticles(ParticleComponent particle){

		if(particle.getType() == ParticleComponent.ParticleType.PORTAL){
			if(m_currentLevel.isFinished()){
                particle.start();
			}
			if(particle.isCompleted() && m_currentLevel.isFinished()){
				m_levelListener.onFinishedLevel(m_currentLevel.getLevelNumber());
			}
		}

	}
	
	private void checkFinished(Entity entity){

        ReachEndComponent reachEndComponent = m_reachEndComps.get(entity);
        if (reachEndComponent.allFinished()) {
            notifyObservers(entity, new GameEvent(GameEventType.AllReachedEnd));
        }
        //if(!m_levelManager.levelHasPortal() && player.isFinishedAnimating()){
        //	m_levelListener.onFinishedLevel(m_levelManager.getLevelNumber());
        //}

        /*
        if(m_levelManager.isTaskDoneForAll(TaskType.WaterEngine)) {
            if(m_buoyancyComps.has(e)) {
                BuoyancyComponent.BuoyancyControllerInfo info = m_buoyancyComps.get(e).getController(WorldObjectComponent.WorldObject);
                if (info != null) {
                    DirectionComponent directionComponent = m_direction.get(e);
                    if (directionComponent.getDirection() == DirectionComponent.Direction.Left) {
                        info.setFluidVelocity(-3, 1);
                    } else if (directionComponent.getDirection() == DirectionComponent.Direction.Right) {
                        info.setFluidVelocity(3, 1);
                    }
                }
            }
        }
        */
	}

}
