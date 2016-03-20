package com.me.level.tasks;

import com.me.events.GameEventType;


/**
 * Created by hateftadayon on 6/9/15.
 */
public class BodyInfo {

    private int taskFinishers;

    public int getEventId() {
        return taskId;
    }

    private int taskId;
    private GameEventType taskType;

    public BodyInfo(int taskFinishers, int taskId, String taskType) {
        this.taskFinishers = taskFinishers;
        this.taskId = taskId;
        setTaskType(taskType);
    }

    private void setTaskType(String taskType) {
        if ("Door".equalsIgnoreCase(taskType)) {
            this.taskType = GameEventType.Door;
        } else if ("WaterEngine".equalsIgnoreCase(taskType)) {
            this.taskType = GameEventType.WaterEngine;
        } else if ("ReachEnd".equalsIgnoreCase(taskType)) {
            this.taskType = GameEventType.InsideFinishArea;
        } else if ("HorizontalButton".equalsIgnoreCase(taskType)) {
            this.taskType = GameEventType.HorizontalButton;
        } else if ("GroundTouch".equalsIgnoreCase(taskType)){
            this.taskType = GameEventType.GroundTouch;
        } else if ("VerticalButton".equalsIgnoreCase(taskType)) {
            this.taskType = GameEventType.VerticalButton;
        } else if ("Particle".equalsIgnoreCase(taskType)){
            this.taskType = GameEventType.Particle;
        } else if ("PortalReachEnd".equalsIgnoreCase(taskType)){
            this.taskType = GameEventType.PortalParticle;
        } else if ("PortalParticleFinish".equalsIgnoreCase(taskType)){
            this.taskType = GameEventType.PortalParticleFinish;
        }
    }

    public int getTaskFinishers() {
        return taskFinishers;
    }

    public GameEventType getEventType() {
        return taskType;
    }

}
