package com.me.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.me.component.*;
import com.me.events.*;
import com.me.level.Level;
import com.me.listeners.LevelEventListener;
import com.me.manager.ScriptManager;

public class LevelSystem extends GameEntityProcessingSystem {


    private ScriptManager scriptMgr;
    private Level currentLevel;
    private boolean enable;

    @Mapper
    ComponentMapper<ReachEndComponent> reachEndComps;
    @Mapper
    ComponentMapper<LevelComponent> levelComps;


    @SuppressWarnings("unchecked")
    public LevelSystem() {
        super(Aspect.getAspectForAll(LevelComponent.class));
    }

    public void setCurrentLevel(Level level) {
        currentLevel = level;
    }

    public void setProcessing(boolean enable) {
        this.enable = enable;
    }

    @Override
    protected void process(Entity e) {
        if (reachEndComps.has(e)) {
            checkFinished(e);
        }
    }

    @Override
    protected boolean checkProcessing() {
        return enable;
    }


    private void checkFinished(Entity entity) {

        LevelComponent levelComponent = levelComps.get(entity);
        ReachEndComponent reachEndComponent = reachEndComps.get(entity);
        if (reachEndComponent.allFinished() && !currentLevel.isFinished()) {
            notifyObservers(new TaskEvent(GameEventType.AllReachedEnd));
            GameEvent event = reachEndComponent.getEndEvent();
            event.notify((GameEntityWorld) world);
            currentLevel.setFinished(true);
        }

        if (levelComponent.allFinished()) {
            levelFinished();
        }

    }

    private void levelFinished() {
        notifyObservers(new LevelEvent(LevelEventType.OnFinished, currentLevel));
    }

}
