package com.me.factory;

import com.badlogic.gdx.physics.box2d.Body;
import com.me.event.GameEvent;
import com.me.event.HorizontalButtonEvent;
import com.me.event.TaskEvent;
import com.me.level.tasks.BodyInfo;
import com.me.loaders.RubeScene;

/**
 * Created by hateftadayon on 7/12/15.
 */
public class GameEventFactory {

    public GameEvent createFromBodyInfo(RubeScene scene, Body body){
        BodyInfo bodyInfo = new BodyInfo(scene.getCustom(body, "taskFinishers", 0), scene.getCustom(body, "taskId", 0), scene.getCustom(body, "eventType", ""));
        switch (bodyInfo.getEventType()){
            case Door:
            case InsideFinishArea:
                return new TaskEvent(bodyInfo);
            case HorizontalButton:
                return new HorizontalButtonEvent(bodyInfo);
            default: return new GameEvent(bodyInfo.getEventType());
        }

    }
}