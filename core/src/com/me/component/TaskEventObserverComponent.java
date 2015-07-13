package com.me.component;

import com.artemis.Entity;
import com.me.event.TaskEvent;

/**
 * Created by hateftadayon on 7/12/15.
 */
public abstract class TaskEventObserverComponent extends BaseComponent {
    public abstract void onNotify(Entity entity, TaskEvent event);
}
