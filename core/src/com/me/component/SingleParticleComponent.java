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

    private int id = 0;
    private boolean enabled;

    public SingleParticleComponent(String effect, int id, Vector2 position) {
        super(effect, 10);
        setPosition(Converters.ToWorld(position));
        this.id = id;
    }

    @Override
    public void onNotify(TaskEvent event) {
        if (event.getEventType() == GameEventType.GroundTouch && id == event.getEventId()) {
            if (enabled) {
                start();
            }
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
