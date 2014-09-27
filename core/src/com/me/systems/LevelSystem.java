package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.me.component.JointComponent;
import com.me.component.LevelComponent;
import com.me.component.LightComponent;
import com.me.component.ParticleComponent;
import com.me.component.QueueComponent;
import com.me.component.ParticleComponent.ParticleType;
import com.me.component.PlayerComponent;
import com.me.component.QueueComponent.QueueType;
import com.me.component.TouchComponent;
import com.me.component.TriggerComponent;
import com.me.listeners.LevelEventListener;
import com.me.scripting.ScriptManager;
import com.me.ui.InputManager;
import com.me.utils.LevelConfig;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class LevelSystem extends EntityProcessingSystem{

	private float inc = 0.0f;
	private LevelEventListener m_levelListener;
	private int m_levelNr;
	private LevelConfig m_levelConfig;
	private ScriptManager m_scriptMgr;
	private LevelComponent m_levelComponent;
	private int first = 0, second = 0;
	private boolean m_enable;

	@Mapper ComponentMapper<LightComponent> m_lightComps;
	@Mapper ComponentMapper<PlayerComponent> m_players;
	@Mapper ComponentMapper<TouchComponent> m_touch;
	@Mapper ComponentMapper<ParticleComponent> m_particles;
	@Mapper ComponentMapper<JointComponent> m_joints;
	@Mapper ComponentMapper<QueueComponent> m_queue;


	@SuppressWarnings("unchecked")
	public LevelSystem(LevelEventListener listener) {
		super(Aspect.getAspectForAll(TriggerComponent.class));
		m_levelListener = listener;
	}

	public void setLevelConfig(LevelConfig levelCfg){
		m_levelConfig = levelCfg;
		m_levelNr = levelCfg.getLevelNr();
		m_scriptMgr = new ScriptManager("data/script.lua");
		m_scriptMgr.runScriptFunction("init", levelCfg);
		m_levelComponent = levelCfg.getLevelComponent();
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

		if(first + second == m_levelComponent.m_nrOfFinishers){
			m_levelComponent.m_finished = true;
		}
		if(m_joints.has(e)){
			JointComponent joint = m_joints.get(e);
			joint.update(world.delta);
			if(joint.shouldDestroy()){
				if(m_queue.has(e)){
					QueueComponent queue = m_queue.get(e);
					queue.type = QueueType.JOINT;
				}	
			}
		}
	}

	@Override
	protected boolean checkProcessing() {
		return m_enable;
	}

	private void updateLights(LightComponent light){
		if(light.getName().equals("portalLight")){
			float a = light.getAlpha();
			if(!m_levelComponent.m_finished){
				if(a >= 1){
					light.setColor(Color.RED);
					inc = -0.008f;
				}else if(a < 0.1f){
					inc = 0.008f;
				}
				light.setAlpha(a + inc);

			}else{
				light.setAlpha(1);
				light.setColor(Color.GREEN);
				world.getSystem(CameraSystem.class).getRayHandler().setAmbientLight(1f);
			}

		}
	}
	
	private void updateParticles(ParticleComponent particle){

		if(particle.getType() == ParticleType.PORTAL){
			if(particle.isSetToStart() && m_levelComponent.m_finished){
				particle.start();
				particle.setToStart(false);
			}
			if(particle.isCompleted() && m_levelComponent.m_finished){
				m_levelListener.onFinishedLevel(m_levelNr);
			}
		}
	}
	
	private void checkFinished(PlayerComponent player, TouchComponent touch){
		if(player.getPlayerNr().equals("playerOne")){
			first = touch.m_endReach;
		} else{
			second = touch.m_endReach;
		}
		if(m_levelComponent.m_finished){
			player.setFacingLeft(m_levelComponent.m_finishFacingLeft);    
			if(!m_levelComponent.m_hasPortal && player.isFinishedAnimating()){
				m_levelListener.onFinishedLevel(m_levelNr);
			}
		}
	}
	
	public LevelComponent getLevelComponent(){
		return m_levelComponent;
	}

}
