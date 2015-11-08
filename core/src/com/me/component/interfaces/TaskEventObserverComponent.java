package com.me.component.interfaces;

import com.me.events.TaskEvent;

/**
 * Created by hateftadayon on 7/12/15.
 */
public interface TaskEventObserverComponent {
    void onNotify(TaskEvent event);
}
