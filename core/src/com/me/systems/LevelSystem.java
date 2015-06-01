package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.me.component.*;
import com.me.manager.LevelManager.LevelTaskType;
import com.me.component.ParticleComponent.ParticleType;
import com.me.manager.LevelManager;
import com.me.tasks.CharacterTask.TaskType;
import com.me.listeners.LevelEventListener;
import com.me.manager.ScriptManager;
import com.me.utils.LevelConfig;

public class LevelSystem extends EntityProcessingSystem{

	private float inc = 0.0f;
	private LevelEventListener m_levelListener;
	private int m_levelNr;
	private LevelConfig m_levelConfig;
	private ScriptManager m_scriptMgr;
	private LevelManager m_levelManager;
	private boolean m_enable;

	@Mapper ComponentMapper<LightComponent> m_lightComps;
	@Mapper ComponentMapper<PlayerComponent> m_players;
	@Mapper ComponentMapper<TouchComponent> m_touch;
	@Mapper ComponentMapper<ParticleComponent> m_particles;
	@Mapper ComponentMapper<JointComponent> m_joints;
	@Mapper ComponentMapper<BuoyancyComponent> m_buoyancyComps;


	@SuppressWarnings("unchecked")
	public LevelSystem(LevelEventListener listener) {
		super(Aspect.getAspectForAll(TriggerComponent.class));
		m_levelListener = listener;
	}

	public void setLevelConfig(LevelConfig levelCfg){
		m_levelConfig = levelCfg;
		m_levelNr = levelCfg.getLevelNr();
		//m_scriptMgr = new ScriptManager("data/script.lua");
		//m_scriptMgr.runScriptFunction("init", levelCfg);
        m_levelManager = levelCfg.getLevelManager();
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
			checkFinished(m_players.get(e), m_touch.get(e));
		}

		if(m_particles.has(e)){
			updateParticles(m_particles.get(e));
		}
		
		if(m_joints.has(e)){
			JointComponent joint = m_joints.get(e);
			if(joint.hasMotor()){
				if(m_levelManager.isTaskDoneForAll(TaskType.OpenDoor)){
					joint.enableMotor(true);
				}
			}
		}
        if(m_buoyancyComps.has(e)) {
            updateBuoyancy();
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
			if(m_levelManager.isTaskDoneForAll(TaskType.ReachedEnd) && !m_levelManager.isTaskDone(LevelTaskType.LevelFinished)){
                particle.start();
                m_levelManager.doneTask(LevelTaskType.LevelFinished);
			}
			if(particle.isCompleted() && m_levelManager.isTaskDoneForAll(TaskType.ReachedEnd)){
				m_levelListener.onFinishedLevel(m_levelNr);
			}
		}
	}
	
	private void checkFinished(PlayerComponent player, TouchComponent touch){
		
		if(touch.m_endReach == 1){
			player.doneTask(TaskType.ReachedEnd);
		} else if (touch.m_endReach == 0){
			player.unDoneTask(TaskType.ReachedEnd);
		}
		if(m_levelManager.isTaskDoneForAll(TaskType.ReachedEnd)){
			player.setFacingLeft(m_levelManager.m_finishFacingLeft);
			if(!m_levelManager.m_hasPortal && player.isFinishedAnimating()){
				m_levelListener.onFinishedLevel(m_levelNr);
			}
		}
	}

	private void updateBuoyancy(){
        //if()
	}
	
	public LevelManager getLevelManager(){
		return m_levelManager;
	}

}
