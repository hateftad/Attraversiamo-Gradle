package com.me.component.interfaces;

import com.artemis.Entity;
import com.me.events.TaskEvent;

/**
 * Created by hateftadayon on 7/12/15.
 */
public interface TaskEventObserverComponent extends GameEventObserverComponent {
    void onNotify(TaskEvent event);
}
