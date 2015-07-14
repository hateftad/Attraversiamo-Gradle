package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.graphics.Color;
import com.me.component.*;
import com.me.event.GameEvent;
import com.me.event.GameEventType;
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
	@Mapper ComponentMapper<ReachEndComponent> m_reachEndComps;
	@Mapper ComponentMapper<LevelComponent> m_levelComp;
	@Mapper ComponentMapper<ParticleComponent> m_particleComps;


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
	
	private void checkFinished(Entity entity){

        ReachEndComponent reachEndComponent = m_reachEndComps.get(entity);
        if (reachEndComponent.allFinished() && !m_currentLevel.isFinished()) {
            notifyObservers(GameEventType.AllReachedEnd);
            m_currentLevel.setFinished(true);
        }

        LevelComponent levelComponent = m_levelComp.get(entity);
        if (levelComponent.isFinished()) {
            if(!m_particleComps.has(entity)) {
                levelFinished();
            } else {
                ParticleComponent particleComponent = m_particleComps.get(entity);
                if(particleComponent.isCompleted()){
                    levelFinished();
                }
            }
        }
	}

    private void levelFinished(){
        m_levelListener.onFinishedLevel(m_currentLevel.getLevelNumber());
    }

}
