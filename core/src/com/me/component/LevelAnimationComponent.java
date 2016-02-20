package com.me.component;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.component.interfaces.ButtonStateObserverComponent;
import com.me.events.ButtonEvent;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;
import com.me.events.VerticalButtonEvent;
import com.me.events.states.PlayerState;
import com.me.utils.Direction;

/**
 * Created by hateftadayon on 7/13/15.
 */
public class LevelAnimationComponent extends AnimationComponent implements ButtonStateObserverComponent {


    private int m_eventId;
    public LevelAnimationComponent(String atlas, String skeleton, float scale) {
        super(atlas, skeleton, scale);
    }

    public LevelAnimationComponent(String atlas, String skeleton, float scale, int taskId) {
        super(atlas, skeleton, scale);
        m_eventId = taskId;
    }

    @Override
    public void onNotify(ButtonEvent event) {
        if(event.getEventType() == GameEventType.VerticalButton) {
            if (m_eventId == event.getEventId()) {
                VerticalButtonEvent buttonEvent = (VerticalButtonEvent) event;
                if(buttonEvent.getDirection() == Direction.Up){
                    setAnimationState(PlayerState.Up);
                } else if(buttonEvent.getDirection() == Direction.Down){
                    setAnimationState(PlayerState.Down);
                }
            }
        }
    }

    @Override
    public void onNotify(TaskEvent event) {

    }

    @Override
    public void setAnimationState(PlayerState state) {
        if (state != m_previousState) {
            setState(state);
            switch (state) {
                case Idle:
                    playAnimation("idle", true);
                    break;
                case Down:
                    playAnimation("down", false);
                    break;
                case Up:
                    playAnimation("up", false);
                    break;
                default:
                    break;
            }
            m_skeleton.setToSetupPose();
        }
        m_previousState = state;
    }

    @Override
    public void update(SpriteBatch sb, float dt) {
        m_animationState.update(dt);
        m_animationState.apply(m_skeleton);
        m_skeleton.update(dt);
        m_skeleton.updateWorldTransform();
        m_renderer.draw(sb, m_skeleton);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void restart() {

    }
}
