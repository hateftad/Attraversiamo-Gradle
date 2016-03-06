package com.me.component;

import com.badlogic.gdx.math.Vector2;
import com.me.component.interfaces.ParticleEventObserverComponent;
import com.me.component.interfaces.TaskEventObserverComponent;
import com.me.events.GameEventType;
import com.me.events.ParticleEvent;
import com.me.events.TaskEvent;
import com.me.utils.Converters;

/**
 * Created by hateftadayon on 10/26/15.
 */
public class SingleParticleComponent extends ParticleComponent implements TaskEventObserverComponent {

    private int m_id;
    private boolean enabled;

    public SingleParticleComponent(String effect, ParticleType type) {
        super(effect, type, 10);
    }

    public SingleParticleComponent(String effect, int id, Vector2 feet) {
        super(effect, ParticleType.SINGLE, 10);
        setPosition(Converters.ToWorld(feet));
        m_id = id;
    }

    @Override
    public void onNotify(TaskEvent event) {
        if (event.getEventType() == GameEventType.GroundTouch && m_id == event.getEventId()) {
            if (m_type == ParticleType.SINGLE) {
                if(enabled) {
                    start();
                }
            }
        } else if (event.getEventType() == GameEventType.AllReachedEnd) {
            if (m_type == ParticleType.PORTAL) {
                start();
            }
        }
    }

    public boolean isPortalComplete(){
        return m_type == ParticleType.PORTAL && isCompleted();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
