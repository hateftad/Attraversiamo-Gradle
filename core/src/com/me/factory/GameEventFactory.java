package com.me.factory;

import com.badlogic.gdx.physics.box2d.Body;
import com.me.events.GameEvent;
import com.me.events.HorizontalButtonEvent;
import com.me.events.TaskEvent;
import com.me.events.VerticalButtonEvent;
import com.me.level.tasks.BodyInfo;
import com.me.loaders.RubeScene;

/**
 * Created by hateftadayon on 7/12/15.
 */
public class GameEventFactory {

    public GameEvent createFromBodyInfo(RubeScene scene, Body body) {
        BodyInfo bodyInfo = new BodyInfo(scene.getCustom(body, "taskFinishers", 0), scene.getCustom(body, "taskId", 0), scene.getCustom(body, "eventType", ""));
        switch (bodyInfo.getEventType()) {
            case Door:
            case InsideFinishArea:
                return new TaskEvent(bodyInfo);
            case HorizontalButton:
                return new HorizontalButtonEvent(bodyInfo);
            case VerticalButton:
                return new VerticalButtonEvent(bodyInfo);
            case GroundTouch:
                return new TaskEvent(bodyInfo);
            default:
                return new GameEvent(bodyInfo.getEventType());
        }

    }
}
