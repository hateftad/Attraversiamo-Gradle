package com.me.component;

import com.me.component.interfaces.TaskEventObserverComponent;
import com.me.events.GameEventType;
import com.me.events.TaskEvent;

/**
 * Created by hateftadayon on 7/11/15.
 */
public class LevelComponent extends BaseComponent implements TaskEventObserverComponent {


    private boolean m_isFinished;

    public LevelComponent(){

    }

    public boolean isFinished(){
        return m_isFinished;
    }

    @Override
    public void onNotify(TaskEvent event) {
        if(event.getEventType() == GameEventType.LevelFinished){
            m_isFinished = true;
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void restart() {

    }
}
