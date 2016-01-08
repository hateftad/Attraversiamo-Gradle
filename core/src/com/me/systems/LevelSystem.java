package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.graphics.Color;
import com.me.component.*;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;
import com.me.level.Level;
import com.me.listeners.LevelEventListener;
import com.me.manager.ScriptManager;

public class LevelSystem extends GameEntityProcessingSystem {

    private LevelEventListener m_levelListener;
    private ScriptManager m_scriptMgr;
    private Level m_currentLevel;
    private boolean m_enable;

    @Mapper
    ComponentMapper<ReachEndComponent> m_reachEndComps;
    @Mapper
    ComponentMapper<SingleParticleComponent> m_particleComps;


    @SuppressWarnings("unchecked")
    public LevelSystem(LevelEventListener listener) {
        super(Aspect.getAspectForAll(TriggerComponent.class));
        m_levelListener = listener;
    }

    public void setCurrentLevel(Level level) {
        m_currentLevel = level;
    }

    public Level getCurrentLevel() {
        return m_currentLevel;
    }

    public void setProcessing(boolean enable) {
        m_enable = enable;
    }

    @Override
    protected void process(Entity e) {

        //m_scriptMgr.update();

        if (m_reachEndComps.has(e)) {
            checkFinished(e);
        }

    }

    @Override
    protected boolean checkProcessing() {
        return m_enable;
    }


    private void checkFinished(Entity entity) {

        ReachEndComponent reachEndComponent = m_reachEndComps.get(entity);
        if (reachEndComponent.allFinished() && !m_currentLevel.isFinished()) {
            notifyObservers(new TaskEvent(GameEventType.AllReachedEnd));
            m_currentLevel.setFinished(true);
        }

        if (m_currentLevel.isFinished()) {
            if (m_particleComps.has(entity)) {
                SingleParticleComponent particleComponent = m_particleComps.get(entity);
                if (particleComponent.isPortalComplete()) {
                    levelFinished();
                }
            } else {
                levelFinished();
            }
        }
    }

    private void levelFinished() {
        m_levelListener.onFinishedLevel(m_currentLevel.getNextLevel());
    }

}
