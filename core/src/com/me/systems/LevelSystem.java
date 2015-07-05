package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.me.component.*;
import com.me.component.ParticleComponent.ParticleType;
import com.me.level.tasks.LevelTask;
import com.me.manager.LevelManager;
import com.me.level.tasks.LevelTask.TaskType;
import com.me.listeners.LevelEventListener;
import com.me.manager.ScriptManager;
import com.me.utils.LevelConfig;

public class LevelSystem extends EntityProcessingSystem{

	private float inc = 0.0f;
	private LevelEventListener m_levelListener;
	private ScriptManager m_scriptMgr;
	private LevelManager m_levelManager;
	private boolean m_enable;

	@Mapper ComponentMapper<LightComponent> m_lightComps;
	@Mapper ComponentMapper<PlayerComponent> m_players;
	@Mapper ComponentMapper<TouchComponent> m_touch;
	@Mapper ComponentMapper<ParticleComponent> m_particles;
	@Mapper ComponentMapper<JointComponent> m_joints;
	@Mapper ComponentMapper<BuoyancyComponent> m_buoyancyComps;
    @Mapper ComponentMapper<DirectionComponent> m_direction;
    @Mapper ComponentMapper<TaskComponent> m_taskComps;


	@SuppressWarnings("unchecked")
	public LevelSystem(LevelEventListener listener) {
		super(Aspect.getAspectForAll(TriggerComponent.class));
		m_levelListener = listener;
	}

	public void setLevelManager(LevelManager levelManager){
		//m_scriptMgr = new ScriptManager("data/script.lua");
		//m_scriptMgr.runScriptFunction("init", levelCfg);
        m_levelManager = levelManager;
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

		if(m_players.has(e)){
			checkFinished(e);
		}

		if(m_particles.has(e)){
			updateParticles(m_particles.get(e));
		}
		
		if(m_joints.has(e)){
            // make door component
			JointComponent joint = m_joints.get(e);
			if(joint.hasMotor()){
				if(m_levelManager.isTaskDoneForAll(TaskType.OpenDoor)){
					joint.enableMotor(true);
				}
			}
		}
        if(m_buoyancyComps.has(e)) {
            updateBuoyancy(e);
        }
	}

	@Override
	protected boolean checkProcessing() {
		return m_enable;
	}

	private void updateLights(LightComponent light){
		if(light.getName().equals("portalLight")){
			float a = light.getAlpha();
			if(!m_levelManager.isTaskDoneForAll(TaskType.ReachedEnd)){
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

		if(particle.getType() == ParticleType.PORTAL){
			if(m_levelManager.isTaskDoneForAll(TaskType.ReachedEnd) && !m_levelManager.isLevelFinished()){
                particle.start();
                m_levelManager.setFinishedLevel();
			}
			if(particle.isCompleted() && m_levelManager.isTaskDoneForAll(TaskType.ReachedEnd)){
				m_levelListener.onFinishedLevel(m_levelManager.getLevelNumber());
			}
		}
	}
	
	private void checkFinished(Entity e){
        PlayerComponent player = m_players.get(e);
        TouchComponent touch = m_touch.get(e);
        TaskComponent taskComponent = m_taskComps.get(e);
        if(touch.endReached()){
            m_levelManager.doneTask(player.getPlayerNr(), taskComponent.getTask());
        } else {
            m_levelManager.unDoneTask(player.getPlayerNr(), TaskType.ReachedEnd);
        }

		if(m_levelManager.isTaskDoneForAll(TaskType.ReachedEnd)){
			player.setFacingLeft(m_levelManager.charactersFinishingLeft());
			if(!m_levelManager.levelHasPortal() && player.isFinishedAnimating()){
				m_levelListener.onFinishedLevel(m_levelManager.getLevelNumber());
			}
		}
	}

	private void updateBuoyancy(Entity e){
        if(m_levelManager.isTaskDoneForAll(TaskType.WaterEngine)) {
            BuoyancyComponent.BuoyancyControllerInfo info = m_buoyancyComps.get(e).getController(WorldObjectComponent.WorldObject);
            if(info != null) {
                DirectionComponent directionComponent = m_direction.get(e);
                if (directionComponent.getDirection() == DirectionComponent.Direction.Left) {
                    info.setFluidVelocity(-3, 1);
                }  else if (directionComponent.getDirection() == DirectionComponent.Direction.Right) {
                    info.setFluidVelocity(3, 1);
                }
            }
        }
	}
	
	public LevelManager getLevelManager(){
		return m_levelManager;
	}

}
