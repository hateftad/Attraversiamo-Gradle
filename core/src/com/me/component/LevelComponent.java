package com.me.component;

import com.me.component.interfaces.TaskEventObserverComponent;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;

/**
 * Created by hateftadayon on 7/11/15.
 */
public class LevelComponent extends TaskComponent implements TaskEventObserverComponent {

    public LevelComponent(int finishers){
        super(finishers);
    }

    @Override
    public void onNotify(TaskEvent event) {
        if(event.getEventType() == GameEventType.LevelFinished){
            if (!m_finishers.containsKey(event.getPlayerNr())) {
                m_finishers.put(event.getPlayerNr(), true);
            }
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }
}
